/**
 * Copyright 2020 Alexander Danielsson, Elvira Dzidic, Andreas Grunde, Elizabeth Inersjö, André Lindgren, Edvard von Pfaler,  Eskil Åslund.
 * Using the GPL-3.0-or-later license.
 *
 *
 * This file is part of CodeOrDie.
 *
 * CodeOrDie is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Foobar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CodeOrDie.  If not, see <https://www.gnu.org/licenses/>.
 *
 * --------------------------------------------------------------------------------------------------------------------------------------------
 */
import java.awt.*;
import java.awt.image.*;
import java.util.HashMap;


/**
 * Classes to handle codeblock used by player
 * Code handles compile and execution of codeBlocks
 * <p>
 * Currently implemeted blocks:
 * CodeBlock			- Only used for start
 * ShootBlock			- Shoots
 * MoveBlock			- Move (Forward, up, down, left, right)
 * RotatePlayerBlock	- Rotates a number of degrees
 * RotateEnemyBlock		- Rotates towards enemy
 * <p>
 * Not finished blocks:
 * Branch - if/else, while-loop
 * Variables
 * Arithmetics - Math
 * More player functions
 */

/**
 *
 */
enum EntityType {
    ENEMY, PICKUP
}

/**
 * @class Code = A wrapper object ment to hold compiled code and run it
 * How To:
 * 1. Use the empty constructor Code() to initialize a the wrapper class and its variables
 * 2. Create some blocks and put them in the order you want them
 * 3. Use compile(CodeBlock start) to create a copy of your code that will be executed
 * 4. Call execute() every tick (executes up to MAX_STEPS_ALLOWED codeblocks per tick, stops at time consuming blocks)
 * <p>
 * Functions:
 * Code() - Constructor
 * compile(CodeBlock start) - Creates a copy of all linked codes starting at this block, and stores them in this instance of Code
 * execute() - Executes a number of codes
 */
public class Code {
    final int MAX_STEPS_ALLOWED = 2; //A constant that specifies how many non-time consuming player instructions that can be carried out in a single execute

    private CodeBlock start; //Where the top of the code is
    private CodeBlock end; //Where the last of the main code branch is (can be null if the start block is null)
    private CodeBlock current; //The instruction which is about to be executed

    private int steps; //The number of steps that has currently been taken

    private HashMap<String, Double> registers; //A HashMap holding all player variables

    public Code() {
        steps = 0;
        registers = new HashMap<String, Double>();
    }

    public void setVariable(String R, double var) {
        registers.put(R, var);
    }

    public double getVariable(String R) {
        return registers.getOrDefault(R, 0.0);
    }

    public CodeBlock getStart() {
        return start;
    }

    public CodeBlock getEnd() {
        return end;
    }

    public HashMap<String, Double> getHashmap() {
        return registers;
    }

    /**
     * "Compiles code" aka copies it
     *
     * @param start
     */
    public void compile(CodeBlock start) {
        registers.clear();
        this.start = copyCode(start);
        current = this.start;
    }

    /**
     * Recursive copy of all codeblocks.
     * Currently does not work on branches.
     *
     * @param block = current block to be copied
     * @return CodeBlock = the copied of the sent in block
     */
    private CodeBlock copyCode(CodeBlock block) {
        //System.out.println(block);
        if (block == null) {
            return null;
        }
        CodeBlock result = block.copy(registers);

        result.setNext(copyCode(result.getNext()));

        if (result.getNext() == null) {
            end = result;
        }

        //If this is a branch block, we need to recursively compile the branch aswell
        if (result instanceof BranchBlock) {
            Code branchCode = new Code(); //We create a new code object for the branch so it can be treated as its own code


            branchCode.compile(((BranchBlock) result).getBranch()); //Then we compile it

            ((BranchBlock) result).setBranch(branchCode.getStart()); //We then set the BranchBlock to point to the start of the branch code
            registers.putAll(branchCode.getHashmap());
            //Determine what the last CodeBlock in the branch code should point next to
            //The last block in an if branch points to the BranchBlocks next block in the mother branch
            //The last block in a while branch points back to the BranchBlock
            if (((BranchBlock) result).getType() == BranchType.IF) { //For If Branches
                if (branchCode.getEnd() != null) {
                    branchCode.getEnd().setNext(result.getNext());
                } else {
                    ((BranchBlock) result).setBranch(result.getNext());
                }
            } else if (((BranchBlock) result).getType() == BranchType.WHILE) { //For while branches
                if (branchCode.getEnd() != null) {
                    branchCode.getEnd().setNext(result);
                } else {
                    ((BranchBlock) result).setBranch(result);
                }
            }
        }

        return result;
    }

    /**
     * Executes a number of instructions starting at current
     * The function tries to execute up to MAX_STEPS_ALLOWED instructions, but ends after
     * a single time consuming (more than one tick) instruction is encountered
     */
    public void execute() {
        //If the number of steps taken this frame is less than the maximum, execute the instruction and recursively call this function again
        if (steps < MAX_STEPS_ALLOWED) {
            if (current != null) {
                CodeBlock tmp = current.run(registers); //Execute the instruction and retrieve the next instruction

                //If this wasn't the start block, we should count it towards the number of blocks executed
                if (current != start) steps++;

                if (current != tmp) { //If the next instruction is not the same as the current
                    current = tmp;
                    this.execute(); //Recursively call this function again to execute the next instruction
                } else { //If it is the same instruction, we just encountered a time consuming block
                    steps = 0;
                    return;
                }
            } else {
                //System.out.println("End of code " + System.currentTimeMillis());

                current = start; //loop around
            }
        } else {
            //Uncoment these rows if you notice that the player seems to stop for a short tick before starting to move again, or if the branch blocks crashes
            /*if (this != null) {
            } else this.execute(); */
            steps = 0;
            return;
        }
    }
}

/**
 * The BaseClass for CodeBlock, should probably not be used by the player. Currently only used to indicate the start of code
 * CodeBlock(x, y, w, h, color, string) - Construct a basic code block but doesn't add it to the scene automatically
 * CodeBlock(x, y, w, h, color, string, GameGraphics) - Constructs a basic code block and adds it to the scene and list of movable blocks
 * getNext() - Returns the block that should be executed next
 * setNext()
 * copy() - Creates a copy of this CodeBlock (the next CodeBlock will still point to the same CodeBlock)
 * run(registers) - Performs the CodeBlocks action and returns which CodeBlock should be execute next
 */
class CodeBlock extends Rectangle {
    protected CodeBlock prev;
    protected CodeBlock next;
    protected int ticks = 0;
    GameGraphics g;
    //TextString text;

    public CodeBlock(int x, int y, int w, int h, Color color) {
        super(x, y, w, h, color, Config.PRIO_CODEBLOCK);
    }

    public CodeBlock(int x, int y, int w, int h, BufferedImage im) {
        super(x, y, w, h, im, Config.PRIO_CODEBLOCK);
    }

    public CodeBlock(int x, int y, int w, int h, Color color, GameGraphics g) {
        super(x, y, w, h, color, Config.PRIO_CODEBLOCK);
        this.g = g;
        g.placeRectangle(this);
        g.codeBlocks.add(this);
    }

    public CodeBlock(int x, int y, int w, int h, BufferedImage im, GameGraphics g) {
        super(x, y, w, h, im, Config.PRIO_CODEBLOCK);
        this.g = g;
        g.placeRectangle(this);
        g.codeBlocks.add(this);
    }

    public CodeBlock getPrev() {
        return prev;
    }

    public CodeBlock getNext() {
        return next;
    }

    public void setPrev(CodeBlock prev) {
        this.prev = prev;
    }

    public void setNext(CodeBlock next) {
        this.next = next;
    }

    public CodeBlock copy(HashMap<String, Double> registers) {
        CodeBlock result = new CodeBlock(super.x, super.y, super.w, super.h, super.color);
        result.setNext(next);
        return result;
    }

    public CodeBlock run(HashMap<String, Double> registers) {
        return next;
    }

    public void update(boolean dragged) {
        g.removeRectangle(this);

        int prio = Config.PRIO_CODEBLOCK_DRAGGED;
        if (!dragged) {
            prio = Config.PRIO_CODEBLOCK;
        }
        this.setPrio(prio);
        g.placeRectangle(this);
    }

    public void place() {
        g.placeRectangle(this);
        g.codeBlocks.add(this);
    }
}

//Directions used by the MoveBlock
enum MoveBlockType {
    LEFT, RIGHT, UP, DOWN, FORWARD
}

/**
 * Codeblock that makes the player move.
 */
class MoveBlock extends CodeBlock {
    private Player target;
    private MoveBlockType type;

    /**
     * @param x      = x coordinate
     * @param y      = y coordinate
     * @param w      = width of block
     * @param h      = height of block
     * @param color  = colour of block
     * @param player = A player this block affects
     * @param type   = Which type of MoveBlock this is
     */
    public MoveBlock(int x, int y, int w, int h, Color color, Player player, MoveBlockType type) {
        super(x, y, w, h, color);
        target = player;
        this.type = type;
    }

    public MoveBlock(int x, int y, int w, int h, Color color, Player player, MoveBlockType type, GameGraphics g) {
        super(x, y, w, h, color, g);
        target = player;
        this.type = type;
    }

    public MoveBlock(int x, int y, int w, int h, BufferedImage im, Player player, MoveBlockType type, GameGraphics g) {
        super(x, y, w, h, im, g);
        target = player;
        this.type = type;
    }

    public CodeBlock copy(HashMap<String, Double> registers) {
        MoveBlock result = new MoveBlock(super.x, super.y, super.w, super.h, super.color, target, type);
        result.setNext(next);
        return result;
    }

    /**
     * Executes the MoveBlock after a set number of ticks, until then returning itself as the next block to be executed
     * Class variable type determines the behaviour of the executed MoveBlock
     */
    public CodeBlock run(HashMap<String, Double> registers) {
        //System.out.println(super.test + " ticks: " + ticks);

        switch (type) {
            case FORWARD:
                target.moveForward();
                break;
            case LEFT:
                target.moveLeft();
                break;
            case RIGHT:
                target.moveRight();
                break;
            case UP:
                target.moveUp();
                break;
            case DOWN:
                target.moveDown();
                break;
        }

        if (ticks < 10) { //TODO: Change to a on construction value
            ticks++;
            return this;
        } else {
            ticks = 0;
            return next;
        }
    }
}

/**
 * CodeBlock to make the player shoot
 * ShootBlock(x, y, w, h, color, player) - Does not add to scene
 * ShootBlock(x, y, w, h, color, player, GameGraphics) - Adds to scene
 */
class ShootBlock extends CodeBlock {
    private Player target;

    public ShootBlock(int x, int y, int w, int h, BufferedImage im, Player player, GameGraphics g) {
        super(x, y, w, h, im, g);
        target = player;
    }

    public ShootBlock(int x, int y, int w, int h, Color color, Player player) {
        super(x, y, w, h, color);
        target = player;
    }

    public ShootBlock(int x, int y, int w, int h, Color color, Player player, GameGraphics g) {
        super(x, y, w, h, color, g);
        target = player;
    }

    public CodeBlock copy(HashMap<String, Double> registers) {
        ShootBlock result = new ShootBlock(super.x, super.y, super.w, super.h, super.color, target);
        result.setNext(next);
        return result;
    }

    public CodeBlock run(HashMap<String, Double> registers) {
        if (target.shoot() || target.getAmmo() == 0) {
            return next;
        } else {
            return this;
        }
    }
}

/**
 * Codeblock that makes the player rotate a set number of degrees
 */
class RotatePlayerBlock extends CodeBlock {
    Player target;
    double degrees; //The total number of degrees that this block will rotate the target
    double degreesToRotate; //The number of degrees left to rotate
    String variable;
    String defaultKey;
    Button variableButton;

    public RotatePlayerBlock(int x, int y, int w, int h, Color color, Player player, String variable) {
        super(x, y, w, h, color);
        target = player;
        this.variable = variable;
    }

    public RotatePlayerBlock(int x, int y, int w, int h, BufferedImage im, Player player, GameGraphics g, String defaultKey) {
        super(x, y, w, h, im, g);
        target = player;
        this.variable = defaultKey;
        this.defaultKey = defaultKey;
        int xOffset1 = Config.ROTATEBUTTON_OFFSET_X1;
        int yOffset1 = Config.ROTATEBUTTON_OFFSET_Y1;
        variableButton = new Button(g, x + xOffset1, y + yOffset1, Config.ROTATEBUTTON_INPUT_W, Config.ROTATEBUTTON_INPUT_H, "0", Config.BRANCHBUTTON_COLOR, Config.PRIO_BRANCHBLOCK_BUTTON);
        variableButton.setXOffset(xOffset1);
        variableButton.setYOffset(yOffset1);
        g.placeButton(variableButton);
    }

    public CodeBlock copy(HashMap<String, Double> registers) {
        if (isNumeric(variableButton.getText())) {
            registers.put(defaultKey, Double.parseDouble(variableButton.getText()));
            variable = defaultKey;
        } else if (variableButton.getText() != null) {
            variable = variableButton.getText();
        }

        RotatePlayerBlock result = new RotatePlayerBlock(super.x, super.y, super.w, super.h, super.color, target, variable);
        result.setNext(next);
        result.degrees = 0;

        return result;
    }

    public CodeBlock run(HashMap<String, Double> registers) {
        if (degrees == 0) {
            degrees = registers.getOrDefault(variable, 0.0);
            degreesToRotate = degrees;
        }

        degreesToRotate = target.rotatePlayer(degreesToRotate); //Rotates as far as we could this tick, and then stores how much more we need to rotate
        if (degreesToRotate != 0) { //If we need to rotate more, return that we are the next block to be executed
            return this;
        } else { //Otherwise
            degrees = 0;
            return next; //Return the next block in line
        }
    }

    public Button getVariableButton() {
        return variableButton;
    }

    public boolean isNumeric(String string) {
        if (string == null) {
            return false;
        }
        try {
            Double.parseDouble(string);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public void update(boolean dragged) {
        g.removeRectangle(this);
        variableButton.remove();

        variableButton.setX(this.x + variableButton.getXOffset());
        variableButton.setY(this.y + variableButton.getYOffset());

        int prio = Config.PRIO_CODEBLOCK_DRAGGED;
        if (!dragged) {
            prio = Config.PRIO_CODEBLOCK;
        }
        this.setPrio(prio);

        prio = Config.PRIO_BRANCHBLOCK_BUTTON_DRAGGED;
        if (!dragged) {
            prio = Config.PRIO_BRANCHBLOCK_BUTTON;
        }
        variableButton.setPrio(prio);

        g.placeRectangle(this);
        variableButton.place();
    }

    public void place() {
        g.placeRectangle(this);
        g.placeButton(variableButton);
        g.codeBlocks.add(this);
    }


}

/**
 * CodeBlock that rotates the player towards an entity.
 * Specify a type of entity to rotate towards using the EntityType type
 * argument in one of the constructors.
 */
class RotateEntityBlock extends CodeBlock {
    Player target;
    double degreesToRotate; //The number of degrees left to rotate
    EntityType type;

    public RotateEntityBlock(int x, int y, int w, int h, Color color, Player player, EntityType type) {
        super(x, y, w, h, color);
        target = player;
        degreesToRotate = 0;
        this.type = type;
    }

    public RotateEntityBlock(int x, int y, int w, int h, Color color, Player player, EntityType type, GameGraphics g) {
        super(x, y, w, h, color, g);
        target = player;
        degreesToRotate = 0;
        this.type = type;
    }

    public RotateEntityBlock(int x, int y, int w, int h, BufferedImage im, Player player, EntityType type, GameGraphics g) {
        super(x, y, w, h, im, g);
        target = player;
        degreesToRotate = 0;
        this.type = type;
    }

    public CodeBlock copy(HashMap<String, Double> registers) {
        RotateEntityBlock result = new RotateEntityBlock(super.x, super.y, super.w, super.h, super.color, target, type);
        result.setNext(next);
        return result;
    }

    public CodeBlock run(HashMap<String, Double> registers) {
        if (degreesToRotate == 0) {
            switch (type) {
                case ENEMY:
                    degreesToRotate = target.rotateEnemy();
                    break;
                case PICKUP:
                    degreesToRotate = target.rotatePickup();
                    break;
            }
        } else {
            degreesToRotate = target.rotatePlayer(degreesToRotate); //Rotates as far as we could this tick, and then stores how much more we need to rotate
        }

        if (degreesToRotate != 0) { //If we need to rotate more, return that we are the next block to be executed
            return this;
        } else { //Otherwise
            degreesToRotate = 0; //Set the degreesToRotate back to the starting value so looping works
            return next; //Return the next block in line
        }
    }
}

enum Operator {
    EQUALS, NOT_EQUALS, LESS, GREATER, LESS_EQUALS, GREATER_EQUALS, BOOL
}

enum BranchType {
    IF, WHILE
}

/**
 * BranchBlock that simulates a while or if expression
 * The behaviour of the branch block is determined by its branchType and operator
 */
class BranchBlock extends CodeBlock {
    private CodeBlock branch;
    public int i = 0;
    private Button varLeftButton;
    private Button varRightButton;
    private Button operatorButton;
    String variableL;
    String variableR;
    String defaultKeyL;
    String defaultKey2;
    Operator operator;
    private BranchType branchType;
    private HashMap<String, Double> registers;
    Rectangle top;
    Rectangle side;
    Rectangle bot;

    public BranchBlock(int x, int y, int w, int h, BufferedImage imTop, BufferedImage imSide, BufferedImage imBottom, String variableL, String variableR, Operator operator, BranchType branchType, GameGraphics g) {
        super(x, y, w, h, new Color(0, 0, 0, 0), g);
        this.variableL = variableL;
        this.variableR = variableR;
        this.defaultKeyL = variableL;
        this.defaultKey2 = variableR;
        this.operator = operator;
        this.branchType = branchType;
        //Create a button for the variables name
        int xOffset1 = Config.BRANCHBUTTON_OFFSET_X1;
        int yOffset1 = Config.BRANCHBUTTON_OFFSET_Y1;
        varLeftButton = new Button(g, x + xOffset1, y + yOffset1, Config.BRANCHBUTTON_INPUT_W, Config.BRANCHBUTTON_INPUT_H, "0", Config.BRANCHBUTTON_COLOR, Config.PRIO_BRANCHBLOCK_BUTTON);
        varLeftButton.setXOffset(xOffset1);
        varLeftButton.setYOffset(yOffset1);
        g.placeButton(varLeftButton);

        //Create a button for the variable value
        int xOffset2 = Config.BRANCHBUTTON_OFFSET_X2;
        int yOffset2 = Config.BRANCHBUTTON_OFFSET_Y2;
        varRightButton = new Button(g, x + xOffset2, y + yOffset2, Config.BRANCHBUTTON_INPUT_W, Config.BRANCHBUTTON_INPUT_H, "0", Config.BRANCHBUTTON_COLOR, Config.PRIO_BRANCHBLOCK_BUTTON);
        varRightButton.setXOffset(xOffset2);
        varRightButton.setYOffset(yOffset2);
        g.placeButton(varRightButton);

        int xOffset3 = Config.BRANCHBUTTON_OFFSET_X3;
        int yOffset3 = Config.BRANCHBUTTON_OFFSET_Y3;
        operatorButton = new Button(g, x + xOffset3, y + yOffset3, Config.BRANCHBUTTON_W, Config.BRANCHBUTTON_H, "==", Config.BRANCHBUTTON_COLOR, Config.PRIO_BRANCHBLOCK_BUTTON);
        operatorButton.setXOffset(xOffset3);
        operatorButton.setYOffset(yOffset3);
        g.placeButton(operatorButton);

        top = new Rectangle(x, y, w, Config.BRANCHBLOCK_TOP_H, imTop, Config.PRIO_CODEBLOCK);
        g.placeRectangle(top);
        side = new Rectangle(x, y + Config.BRANCHBLOCK_TOP_H, Config.BRANCHBLOCK_SIDE_W, Config.BRANCHBLOCK_SIDE_DEFAULT_H, imSide, Config.PRIO_CODEBLOCK);
        g.placeRectangle(side);
        bot = new Rectangle(x, y + Config.BRANCHBLOCK_TOP_H + Config.BRANCHBLOCK_SIDE_DEFAULT_H, w, Config.BRANCHBLOCK_BOT_H, imBottom, Config.PRIO_CODEBLOCK);
        g.placeRectangle(bot);
    }

    public BranchBlock(int x, int y, int w, int h, BufferedImage im, String variableL, String variableR, Operator operator, BranchType branchType) {
        super(x, y, w, h, im);
        this.variableL = variableL;
        this.variableR = variableR;
        this.operator = operator;
        this.branchType = branchType;
    }


    public BranchType getType() {
        return branchType;
    }

    //Get a button, hardcoded rn
    public Button getVarLeftButton() {
        return varLeftButton;
    }

    //Get a button, hardcoded rn
    public Button getVarRightButton() {
        return varRightButton;
    }

    public Button getOperatorButton() {
        return operatorButton;
    }

    public Rectangle getTop() {
        return top;
    }

    public Rectangle getSide() {
        return side;
    }

    public Rectangle getBot() {
        return bot;
    }

    public void setOperator(Operator op) {
        this.operator = op;
    }

    //Set the first node of the branch
    public void setBranch(CodeBlock branch) {
        this.branch = branch;
    }

    public CodeBlock getBranch() {
        return branch;
    }

    public CodeBlock copy(HashMap<String, Double> registers) {
        //Need to use variables not get the name buttons
        if (isNumeric(varLeftButton.getText())) {
            variableL = defaultKeyL;
            registers.put(variableL, Double.parseDouble(varLeftButton.getText()));
        }
        if (isNumeric(varRightButton.getText())) {
            variableR = defaultKey2;
            registers.put(variableR, Double.parseDouble(varRightButton.getText()));
        }
        if (!isNumeric(varLeftButton.getText()) && varLeftButton.getText() != null) {
            variableL = varLeftButton.getText();
        }
        if (!isNumeric(varRightButton.getText()) && varRightButton.getText() != null) {
            variableR = varRightButton.getText();
        }

        BranchBlock result = new BranchBlock(super.x, super.y, super.w, super.h, super.im, variableL, variableR, operator, branchType);
        result.defaultKeyL = defaultKeyL;
        result.defaultKey2 = defaultKey2;
        result.setNext(next);
        result.setBranch(branch);
        return result;
    }

    public boolean isNumeric(String string) {
        if (string == null) {
            return false;
        }
        try {
            Double.parseDouble(string);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public CodeBlock run(HashMap<String, Double> registers) {
        boolean toBranch = false;

        double one = registers.getOrDefault(variableL, 0.0);
        double two = registers.getOrDefault(variableR, 0.0);
        double compareVar = one - two;

        //System.out.println();
        //System.out.println(System.currentTimeMillis() + " LEFT:  Name: " + variable1 + " Value: " + registers.getOrDefault(variable1, 0.0));
        //System.out.println(System.currentTimeMillis() + " RIGHT: Name: " + variable2 + " Value: " + registers.getOrDefault(variable2, 0.0));

        switch (operator) {
            case EQUALS:
                toBranch = compareVar == 0;
                break;
            case NOT_EQUALS:
                toBranch = compareVar != 0;
                break;
            case LESS:
                toBranch = compareVar < 0;
                break;
            case GREATER:
                toBranch = compareVar > 0;
                break;
            case LESS_EQUALS:
                toBranch = compareVar <= 0;
                break;
            case GREATER_EQUALS:
                toBranch = compareVar >= 0;
                break;
        }

        if (toBranch) {
            return branch;
        } else {
            return next;
        }
    }

    public void update(boolean dragged) {
        varLeftButton.remove();
        varRightButton.remove();
        operatorButton.remove();
        g.removeRectangle(top);
        g.removeRectangle(side);
        g.removeRectangle(bot);

        varLeftButton.setX(x + varLeftButton.getXOffset());
        varLeftButton.setY(y + varLeftButton.getYOffset());
        varRightButton.setX(x + varRightButton.getXOffset());
        varRightButton.setY(y + varRightButton.getYOffset());
        operatorButton.setX(x + operatorButton.getXOffset());
        operatorButton.setY(y + operatorButton.getYOffset());
        top.setX(x);
        top.setY(y);
        side.setX(x);
        side.setY(y + top.getHeight());
        bot.setX(x);
        side.setHeight(h - top.getHeight() - bot.getHeight());
        bot.setY(side.getY() + side.getHeight());

        int prio = Config.PRIO_BRANCHBLOCK_BUTTON_DRAGGED;
        if (!dragged) {
            prio = Config.PRIO_BRANCHBLOCK_BUTTON;
        }
        varLeftButton.setPrio(prio);
        varRightButton.setPrio(prio);
        operatorButton.setPrio(prio);

        prio = Config.PRIO_CODEBLOCK_DRAGGED;
        if (!dragged) {
            prio = Config.PRIO_CODEBLOCK;
        }
        top.setPrio(prio);
        side.setPrio(prio);
        bot.setPrio(prio);

        varLeftButton.place();
        varRightButton.place();
        operatorButton.place();
        g.placeRectangle(top);
        g.placeRectangle(side);
        g.placeRectangle(bot);
    }

    public void place() {
        g.placeButton(varLeftButton);
        g.placeButton(varRightButton);
        g.placeButton(operatorButton);
        g.placeRectangle(top);
        g.placeRectangle(side);
        g.placeRectangle(bot);
        g.codeBlocks.add(this);
    }
}

enum GetType {
    X,
    Y,
    AMMO,
    HEALTH
}

class GetBlock extends CodeBlock {
    public GetType type;
    private Player player;
    private Button varButton;
    private Button changeButton;
    private String resultVariable;

    public GetBlock(int x, int y, int w, int h, BufferedImage im, Player player, GetType type, String resultVariable) {
        super(x, y, w, h, im);
        this.type = type;
        this.player = player;
        this.resultVariable = resultVariable;
    }

    public GetBlock(int x, int y, int w, int h, BufferedImage im, Player player, GetType type, GameGraphics g) {
        super(x, y, w, h, im, g);
        this.type = type;
        this.player = player;
        this.resultVariable = "R";
        int xOffset1 = Config.GETBUTTON_OFFSET_X1;
        int yOffset1 = Config.GETBUTTON_OFFSET_Y1;
        varButton = new Button(g, x + xOffset1, y + yOffset1, Config.GETBUTTON_INPUT_W, Config.BRANCHBUTTON_H, "R", Config.BRANCHBUTTON_COLOR, Config.PRIO_BRANCHBLOCK_BUTTON);
        varButton.setXOffset(xOffset1);
        varButton.setYOffset(yOffset1);
        g.placeButton(varButton);
        int xOffset2 = Config.GETBUTTON_OFFSET_X2;
        int yOffset2 = Config.GETBUTTON_OFFSET_Y2;
        changeButton = new Button(g, x + xOffset2, y + yOffset2, Config.GETBUTTON_INPUT_W, Config.BRANCHBUTTON_H, "Player X", Config.BRANCHBUTTON_COLOR, Config.PRIO_BRANCHBLOCK_BUTTON);
        changeButton.setXOffset(xOffset2);
        changeButton.setYOffset(yOffset2);
        g.placeButton(changeButton);
    }

    public Button getVarButton() {
        return varButton;
    }

    public Button getChangeButton() {
        return changeButton;
    }

    public GetType getType() {
        return type;
    }

    public void setType(GetType type) {
        this.type = type;
    }

    public CodeBlock copy(HashMap<String, Double> register) {
        resultVariable = varButton.getText();
        GetBlock result = new GetBlock(super.x, super.y, super.w, super.h, super.im, player, type, resultVariable);
        result.setNext(next);
        return result;
    }

    public CodeBlock run(HashMap<String, Double> register) {
        double result = 0.0;
        //System.out.println(type);
        switch (type) {
            case X:
                result = player.getX() - Config.PLAYAREA_X;
                break;
            case Y:
                result = player.getY() - Config.PLAYAREA_Y;
                break;
            case AMMO:
                result = player.getAmmo();
                break;
            case HEALTH:
                result = player.getHealth();
                break;
        }

        register.put(resultVariable, result);
        return next;
    }

    public void update(boolean dragged) {
        g.removeRectangle(this);
        varButton.remove();
        changeButton.remove();

        varButton.setX(x + varButton.getXOffset());
        varButton.setY(y + varButton.getYOffset());
        changeButton.setX(x + changeButton.getXOffset());
        changeButton.setY(y + changeButton.getYOffset());

        int prio = Config.PRIO_CODEBLOCK_DRAGGED;
        if (!dragged) {
            prio = Config.PRIO_CODEBLOCK;
        }
        this.setPrio(prio);

        prio = Config.PRIO_BRANCHBLOCK_BUTTON_DRAGGED;
        if (!dragged) {
            prio = Config.PRIO_BRANCHBLOCK_BUTTON;
        }
        varButton.setPrio(prio);
        changeButton.setPrio(prio);

        g.placeRectangle(this);
        varButton.place();
        changeButton.place();
    }

    public void place() {
        g.placeRectangle(this);
        g.placeButton(varButton);
        g.placeButton(changeButton);
        g.codeBlocks.add(this);
    }
}

class SetBlock extends CodeBlock {
    String variableL;
    String variableR;
    String defaultKey;
    private Button setLeftButton;
    private Button setRightButton;

    public SetBlock(int x, int y, int w, int h, BufferedImage im, String defaultKey, String variableL, String variableR) {
        super(x, y, w, h, im);
        this.defaultKey = defaultKey;
        this.variableL = variableL;
        this.variableR = variableR;
    }

    public SetBlock(int x, int y, int w, int h, BufferedImage im, String defaultKey, GameGraphics g) {
        super(x, y, w, h, im, g);
        this.variableL = "R";
        this.defaultKey = defaultKey;
        this.variableR = defaultKey;
        int xOffset1 = Config.SETBUTTON_OFFSET_X1;
        int yOffset1 = Config.SETBUTTON_OFFSET_Y1;
        setLeftButton = new Button(g, x + xOffset1, y + yOffset1, Config.SETBUTTON_INPUT_W, Config.BRANCHBUTTON_H, "R", Config.BRANCHBUTTON_COLOR, Config.PRIO_BRANCHBLOCK_BUTTON);
        setLeftButton.setXOffset(xOffset1);
        setLeftButton.setYOffset(yOffset1);
        g.placeButton(setLeftButton);
        int xOffset2 = Config.SETBUTTON_OFFSET_X2;
        int yOffset2 = Config.SETBUTTON_OFFSET_Y2;
        setRightButton = new Button(g, x + xOffset2, y + yOffset2, Config.SETBUTTON_INPUT_W, Config.BRANCHBUTTON_H, "0", Config.BRANCHBUTTON_COLOR, Config.PRIO_BRANCHBLOCK_BUTTON);
        setRightButton.setXOffset(xOffset2);
        setRightButton.setYOffset(yOffset2);
        g.placeButton(setRightButton);
    }

    public Button getSetLeftButton() {
        return setLeftButton;
    }

    public Button getSetRightButton() {
        return setRightButton;
    }

    public CodeBlock copy(HashMap<String, Double> register) {
        if (setLeftButton.getText() != null) {
            variableL = setLeftButton.getText();
        }
        if (isNumeric(setRightButton.getText())) {
            register.put(defaultKey, Double.parseDouble(setRightButton.getText()));
            variableR = defaultKey;
        } else if (setRightButton.getText() != null) {
            variableR = setRightButton.getText();
        }

        SetBlock result = new SetBlock(super.x, super.y, super.w, super.h, super.im, defaultKey, variableL, variableR);
        result.setNext(next);

        return result;
    }

    public CodeBlock run(HashMap<String, Double> register) {
        register.put(variableL, register.getOrDefault(variableR, 0.0));

        return next;
    }

    private boolean isNumeric(String string) {
        if (string == null) {
            return false;
        }
        try {
            Double.parseDouble(string);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public void update(boolean dragged) {
        g.removeRectangle(this);
        setRightButton.remove();
        setLeftButton.remove();

        setRightButton.setX(this.x + setRightButton.getXOffset());
        setRightButton.setY(this.y + setRightButton.getYOffset());
        setLeftButton.setX(this.x + setLeftButton.getXOffset());
        setLeftButton.setY(this.y + setLeftButton.getYOffset());

        int prio = Config.PRIO_CODEBLOCK_DRAGGED;
        if (!dragged) {
            prio = Config.PRIO_CODEBLOCK;
        }
        this.setPrio(prio);

        prio = Config.PRIO_BRANCHBLOCK_BUTTON_DRAGGED;
        if (!dragged) {
            prio = Config.PRIO_BRANCHBLOCK_BUTTON;
        }
        setRightButton.setPrio(prio);
        setLeftButton.setPrio(prio);

        g.placeRectangle(this);
        setRightButton.place();
        setLeftButton.place();
    }

    public void place() {
        g.placeRectangle(this);
        g.placeButton(setLeftButton);
        g.placeButton(setRightButton);
        g.codeBlocks.add(this);
    }
}

enum ArithmeticType {
    ADD, SUB, DIV, MUL
}

class ArithmeticBlock extends CodeBlock {
    private ArithmeticType type;
    String variable1;
    String variable2;
    String resultVariable;
    String defaultKey1;
    String defaultKey2;
    String defaultKeyR;
    private Button var1Button;
    private Button var2Button;
    private Button resultButton;
    private Button operatorButton;

    public ArithmeticBlock(int x, int y, int w, int h, BufferedImage im, ArithmeticType type) {
        super(x, y, w, h, im);
        this.type = type;
    }

    public ArithmeticBlock(int x, int y, int w, int h, BufferedImage im, ArithmeticType type, GameGraphics g) {
        super(x, y, w, h, im, g);
        this.type = type;
    }

    public ArithmeticBlock(int x, int y, int w, int h, BufferedImage im, ArithmeticType type, String variable1, String variable2, String result) {
        super(x, y, w, h, im);
        this.type = type;
        this.variable1 = variable1;
        this.variable2 = variable2;
        this.resultVariable = result;
    }

    public ArithmeticBlock(int x, int y, int w, int h, BufferedImage im, ArithmeticType type, GameGraphics g, String variable1, String variable2, String result) {
        super(x, y, w, h, im, g);
        this.type = type;
        this.variable1 = variable1;
        this.variable2 = variable2;
        this.resultVariable = result;
        defaultKey1 = variable1;
        defaultKey2 = variable2;
        defaultKeyR = result;

        int xOffset1 = Config.MATHBUTTON_OFFSET_X1;
        int yOffset1 = Config.MATHBUTTON_OFFSET_Y1;
        var1Button = new Button(g, x + xOffset1, y + yOffset1, Config.MATHBUTTON_INPUT_W, Config.MATHBUTTON_INPUT_H, "0", Config.BRANCHBUTTON_COLOR, Config.PRIO_BRANCHBLOCK_BUTTON);
        var1Button.setXOffset(xOffset1);
        var1Button.setYOffset(yOffset1);
        g.placeButton(var1Button);

        int xOffset2 = Config.MATHBUTTON_OFFSET_X2;
        int yOffset2 = Config.MATHBUTTON_OFFSET_Y2;
        var2Button = new Button(g, x + xOffset2, y + yOffset2, Config.MATHBUTTON_INPUT_W, Config.MATHBUTTON_INPUT_H, "0", Config.BRANCHBUTTON_COLOR, Config.PRIO_BRANCHBLOCK_BUTTON);
        var2Button.setXOffset(xOffset2);
        var2Button.setYOffset(yOffset2);
        g.placeButton(var2Button);

        int xOffset3 = Config.MATHBUTTON_OFFSET_X3;
        int yOffset3 = Config.MATHBUTTON_OFFSET_Y3;
        resultButton = new Button(g, x + xOffset3, y + yOffset3, Config.MATHBUTTON_INPUT_W, Config.MATHBUTTON_INPUT_H, "R", Config.BRANCHBUTTON_COLOR, Config.PRIO_BRANCHBLOCK_BUTTON);
        resultButton.setXOffset(xOffset3);
        resultButton.setYOffset(yOffset3);
        g.placeButton(resultButton);

        int xOffset4 = Config.MATHBUTTON_OFFSET_X4;
        int yOffset4 = Config.MATHBUTTON_OFFSET_Y4;
        operatorButton = new Button(g, x + xOffset4, y + yOffset4, Config.MATHBUTTON_W, Config.MATHBUTTON_H, "+", Config.BRANCHBUTTON_COLOR, Config.PRIO_BRANCHBLOCK_BUTTON);
        operatorButton.setXOffset(xOffset4);
        operatorButton.setYOffset(yOffset4);
        g.placeButton(operatorButton);


    }

    public Button getVar1Button() {
        return var1Button;
    }

    public Button getVar2Button() {
        return var2Button;
    }

    public Button getResultButton() {
        return resultButton;
    }

    public Button getOperatorButton() {
        return operatorButton;
    }

    public void setOperator(ArithmeticType type) {
        this.type = type;
    }

    public CodeBlock copy(HashMap<String, Double> registers) {
        //Need to use variables not get the name buttons
        if (isNumeric(var1Button.getText())) {
            variable1 = defaultKey1;
            registers.put(variable1, Double.parseDouble(var1Button.getText()));
        } else if (var1Button.getText() != null) {
            variable1 = var1Button.getText();
        }

        if (isNumeric(var2Button.getText())) {
            variable2 = defaultKey2;
            registers.put(variable2, Double.parseDouble(var2Button.getText()));
        } else if (var2Button.getText() != null) {
            variable2 = var2Button.getText();
        }

        if (resultButton.getText() != null) {
            resultVariable = resultButton.getText();
        }

        ArithmeticBlock result = new ArithmeticBlock(super.x, super.y, super.w, super.h, super.im, type, variable1, variable2, resultVariable);
        result.setNext(next);

        return result;
    }

    public boolean isNumeric(String string) {
        if (string == null) {
            return false;
        }
        try {
            Double.parseDouble(string);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public CodeBlock run(HashMap<String, Double> registers) {

        switch (type) {
            case ADD:
                registers.put(resultVariable, registers.getOrDefault(variable1, 0.0) + registers.getOrDefault(variable2, 0.0));
                break;
            case SUB:
                registers.put(resultVariable, registers.getOrDefault(variable1, 0.0) - registers.getOrDefault(variable2, 0.0));
                break;
            case DIV:
                registers.put(resultVariable, registers.getOrDefault(variable1, 0.0) / registers.getOrDefault(variable2, 0.0));
                break;
            case MUL:
                registers.put(resultVariable, registers.getOrDefault(variable1, 0.0) * registers.getOrDefault(variable2, 0.0));
                break;
        }

        return next;

    }

    public void update(boolean dragged) {
        g.removeRectangle(this);
        var1Button.remove();
        var2Button.remove();
        operatorButton.remove();
        resultButton.remove();

        var1Button.setX(this.x + var1Button.getXOffset());
        var1Button.setY(this.y + var1Button.getYOffset());
        var2Button.setX(this.x + var2Button.getXOffset());
        var2Button.setY(this.y + var2Button.getYOffset());
        operatorButton.setX(this.x + operatorButton.getXOffset());
        operatorButton.setY(this.y + operatorButton.getYOffset());
        resultButton.setX(this.x + resultButton.getXOffset());
        resultButton.setY(this.y + resultButton.getYOffset());

        int prio = Config.PRIO_CODEBLOCK_DRAGGED;
        if (!dragged) {
            prio = Config.PRIO_CODEBLOCK;
        }
        this.setPrio(prio);

        prio = Config.PRIO_BRANCHBLOCK_BUTTON_DRAGGED;
        if (!dragged) {
            prio = Config.PRIO_BRANCHBLOCK_BUTTON;
        }
        var1Button.setPrio(prio);
        var2Button.setPrio(prio);
        operatorButton.setPrio(prio);
        resultButton.setPrio(prio);

        g.placeRectangle(this);
        var1Button.place();
        var2Button.place();
        operatorButton.place();
        resultButton.place();
    }

    public void place() {
        g.placeRectangle(this);
        g.placeButton(var1Button);
        g.placeButton(var2Button);
        g.placeButton(resultButton);
        g.placeButton(operatorButton);
        g.codeBlocks.add(this);
    }
}
