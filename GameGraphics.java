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
import java.io.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.event.*;
import java.awt.image.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.imageio.*;
/*
A GameGraphics object creates a window in full screen and keeps track of
all objects put on it. The update() method moves everything accordingly.

*/

/* Image scaling is complicated, here is a guide:
 *  drawImage(Image image, x, y, lastX, lastY, startCropX, startCropY, endCropX, endCropY, Obeserver obs)
 *  First place the image on the x, y coordinates the top left corner should start in. The next two variables should be
 *  the last x, y coordinates of the pictures (where the bottom right corner is placed).
 *  The next 2 arguments should be 0 and the two after should be the size of the picture unless you want crop it. These 4 variables
 *  will crop the picture. The last thing should ALWAYS be null, we dont care about an observer.
 *  Elizabeth out.
 */

public class GameGraphics extends JPanel implements MouseWheelListener {
    public boolean FULLSCREEN = false;
    public JFrame screen;
    public Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    //public final Dimension DEFAULT_SCREENSIZE = new Dimension(1920, 1080);

    public double scale;

    MouseHandler m = new MouseHandler();
    public boolean clicking = false;
    public boolean rightClicking = false;
    boolean clickedButton = false;
    boolean pickedUp = false;
    boolean alreadyRemoved = false;
    CodeBlock selectedCodeBlock = null;

    GameLoop gameLoop;

    Sound s = new Sound();

    Player player;

    ArrayList<TextString> texts;
    ArrayList<Healthbar> healthbars;
    ArrayList<Enemy> enemies;
    ArrayList<Projectile> projectiles;
    ArrayList<Rectangle> rectangles;
    ArrayList<Line> lines;
    ArrayList<CodeBlock> codeBlocks = new ArrayList<CodeBlock>();
    ArrayList<Button> buttons;
    ArrayList<Pickup> pickups;
    ArrayList<Integer> prios;
    int iRe, iTe, iEn, iHe, iPi, iPr, iLi;

    public BufferedImage backgroundImage;
    public BufferedImage levelSelectImage;
    public BufferedImage redTank;
    public BufferedImage yellowTank;
    public BufferedImage purpleTank;
    public BufferedImage rotatedRed;
    public BufferedImage greenTank;
    public BufferedImage menuB, pauseB, playB, exitB, compileB, fullsB;
    public BufferedImage playerVar, px, py, phealth, pammo;
    public ArrayList<BufferedImage> codeBlockImage;
    public BufferedImage healthPickup, ammoPickup;

    public GameGraphics(GameLoop gameLoop) {
        this.gameLoop = gameLoop;
        this.texts = new ArrayList<TextString>();
        this.healthbars = new ArrayList<Healthbar>();
        this.enemies = new ArrayList<Enemy>();
        this.projectiles = new ArrayList<Projectile>();
        this.rectangles = new ArrayList<Rectangle>();
        this.lines = new ArrayList<Line>();
        this.buttons = new ArrayList<Button>();
        this.pickups = new ArrayList<Pickup>();
        this.addMouseWheelListener(this);

        System.out.println("Detected screen size:   " + screenSize);

        // Check which direction the screen constricts the most and change scale accordingly.
        double screenScaleW = screenSize.getWidth() / Config.DEFAULT_SCREENSIZE.getWidth();
        double screenScaleH = screenSize.getHeight() / Config.DEFAULT_SCREENSIZE.getHeight();
        if (screenScaleW < screenScaleH) {
            scale = screenScaleW;
            screenSize.setSize(screenSize.getWidth(), screenSize.getWidth() * (Config.DEFAULT_SCREENSIZE.getHeight() / Config.DEFAULT_SCREENSIZE.getWidth()));
        } else {
            scale = screenScaleH;
            screenSize.setSize(screenSize.getHeight() * (Config.DEFAULT_SCREENSIZE.getWidth() / Config.DEFAULT_SCREENSIZE.getHeight()), screenSize.getHeight());
        }

        //System.out.println("Scale: " + scale);
        //System.out.println("Set screen size:        " + screenSize);

    }

    /* ------------------------------------------------------------------------------------------------------------------------------------------------
     * Graphics stuff
     */

    /**
     * @param image           = image to rotate
     * @param rotationDegrees = how much to rotate in degrees
     */
    public static BufferedImage rotateImage(BufferedImage image, double rotationDegrees) {
        //if (rotationDegrees >= 1 || rotationDegrees != 360) { //Don't waste time calculating a new image if our rotation will just bring us back to where we were

        return image;
        //}
        //return image;
    }

    //is called when repaint is called
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, screenSize.width, screenSize.height);//background color
        g.setColor(Color.WHITE);
        drawEverything(g);
    }

    //Loads in all images used by the program and returns true if it succeeds. On failure, it returns false
    public boolean loadImages() {
        try {
            backgroundImage = ImageIO.read(getClass().getResource("background.jpg"));
            levelSelectImage = ImageIO.read(getClass().getResource("LevelSelect.jpg"));
            playerVar = ImageIO.read(getClass().getResource("block_player_var.png"));
            px = ImageIO.read(getClass().getResource("px.jpg"));
            py = ImageIO.read(getClass().getResource("py.jpg"));
            phealth = ImageIO.read(getClass().getResource("phealth.jpg"));
            pammo = ImageIO.read(getClass().getResource("pammo.jpg"));
            //redTank = ImageIO.read(new File("pixil-frame-red.png"));
            //redTank = ImageIO.read(new File("pixil-frame-red.png"));
            redTank = ImageIO.read(getClass().getResource("pixil-frame-red.png"));
            yellowTank = ImageIO.read(getClass().getResource("roundboi-yellow-cut.png"));
            greenTank = ImageIO.read(getClass().getResource("roundboi-green.png"));
            purpleTank = ImageIO.read(getClass().getResource("roundboi-purple.png"));
            //levelSelectBackground = ImageIO.read(new File("LevelSelect.jpg"));
            rotatedRed = rotateImage(redTank, 318.0100100239448);
            codeBlockImage = new ArrayList<BufferedImage>();
            menuB = ImageIO.read(getClass().getResource("bMenu.jpg"));
            pauseB = ImageIO.read(getClass().getResource("bPause.jpg"));
            playB = ImageIO.read(getClass().getResource("bPlay.jpg"));
            exitB = ImageIO.read(getClass().getResource("bExit.jpg"));
            compileB = ImageIO.read(getClass().getResource("bCompile.jpg"));
            fullsB = ImageIO.read(getClass().getResource("bFullScreen.jpg"));
            codeBlockImage.add(ImageIO.read(getClass().getResource("block_start.jpg")));
            codeBlockImage.add(ImageIO.read(getClass().getResource("block_move.jpg")));
            codeBlockImage.add(ImageIO.read(getClass().getResource("block_up.jpg")));
            codeBlockImage.add(ImageIO.read(getClass().getResource("block_down.jpg")));
            codeBlockImage.add(ImageIO.read(getClass().getResource("block_right.jpg")));
            codeBlockImage.add(ImageIO.read(getClass().getResource("block_left.jpg")));
            codeBlockImage.add(ImageIO.read(getClass().getResource("block_rotate.png")));
            codeBlockImage.add(ImageIO.read(getClass().getResource("block_target.jpg")));
            codeBlockImage.add(ImageIO.read(getClass().getResource("block_pickup.jpg")));
            codeBlockImage.add(ImageIO.read(getClass().getResource("block_fire.jpg")));
            codeBlockImage.add(ImageIO.read(getClass().getResource("block_if.png")));
            codeBlockImage.add(ImageIO.read(getClass().getResource("block_while.png")));
            codeBlockImage.add(ImageIO.read(getClass().getResource("block_math.png")));
            codeBlockImage.add(ImageIO.read(getClass().getResource("block_minus.jpg")));
            codeBlockImage.add(ImageIO.read(getClass().getResource("block_div.jpg")));
            codeBlockImage.add(ImageIO.read(getClass().getResource("block_mul.jpg")));
            codeBlockImage.add(ImageIO.read(getClass().getResource("block_var.png")));
            codeBlockImage.add(ImageIO.read(getClass().getResource("block_while_top.jpg")));
            codeBlockImage.add(ImageIO.read(getClass().getResource("block_while_side.jpg")));
            codeBlockImage.add(ImageIO.read(getClass().getResource("block_while_bottom.jpg")));
            codeBlockImage.add(ImageIO.read(getClass().getResource("block_if_top.jpg")));
            codeBlockImage.add(ImageIO.read(getClass().getResource("block_if_side.jpg")));
            codeBlockImage.add(ImageIO.read(getClass().getResource("block_if_bottom.jpg")));
            healthPickup = ImageIO.read(getClass().getResource("pixil-frame-health.png"));
            ammoPickup = ImageIO.read(getClass().getResource("projectile_011.png"));

            return true;
        } catch (IOException ex) {
            System.out.println(ex);
            return false;
        }
    }

    public void initilizeGraphics() {
        screen = new JFrame();
        //this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        screen.setSize(screenSize.width, screenSize.height);//scale to work for all screens
        screen.setLocation(0, 0);
        screen.setTitle("Code or Die");
        screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        // codearea = new CodeArea(300,300,300,400,Color.CYAN);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                m.setX((int) (e.getX() / scale)); //picks the block up in a nice and square fashion
                m.setY((int) (e.getY() / scale));
                if (SwingUtilities.isLeftMouseButton(e)) {
                    clicking = true;
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    rightClicking = true;
                }
            }

            //TODO what happens when presssing multiple buttons
            @Override
            public void mouseReleased(MouseEvent e) {
                clicking = false;
                rightClicking = false;
                alreadyRemoved = false;
                clickedButton = false;
                m.setX((int) (e.getX() / scale));//set the mousehandler x and y, aka. drop the block.
                m.setY((int) (e.getY() / scale));
            }
        });
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                m.setX((int) (e.getX() / scale)); //picks the block up in a nice and square fashion
                m.setY((int) (e.getY() / scale));
            }
        });
        screen.add(this);
        this.requestFocus();

        screen.setExtendedState(JFrame.MAXIMIZED_BOTH);

        if (FULLSCREEN) {
            screen.setUndecorated(true);
        }
        screen.setVisible(true);//solves a problem with the screen not always working

        /*if (loadImages()) {
            System.out.println("All images loaded correctly");
        }*/
    }

    public void repaintG() {
        repaint();
    }

    public void toggleFullscreen() {
        if (FULLSCREEN) {
            FULLSCREEN = false;
            screen.dispose();
            screen.setUndecorated(false);
            screen.setVisible(true);
            screen.requestFocusInWindow();
        } else {
            FULLSCREEN = true;
            screen.dispose();
            screen.setUndecorated(true);
            screen.setVisible(true);
            screen.requestFocusInWindow();
        }
    }

    public void update() { //call this to update the entire screen, duh
        removeBlocks();
        moveBlocks();
        checkButtons();
    }

    /* ------------------------------------------------------------------------------------------------------------------------------------------------
     * Draw everything in order
     */

    private synchronized void drawEverything(Graphics g) {
        iEn = enemies.size() - 1;
        iPr = projectiles.size() - 1;
        iHe = healthbars.size() - 1;
        iPi = pickups.size() - 1;
        iRe = rectangles.size() - 1;
        iTe = texts.size() - 1;
        iLi = lines.size() - 1;

        // Need a try because graphics can update on separate thread while a list is being modified.
        for (int prio = 20; prio >= 0; prio--) {

            // Rendering happens on separte thread. If an element is deleted the lists will be to short for the .get() call.
            // Using locks (synchronized keyword) right now. We will se about performance later.
            try {
                drawPlayer(g, prio);
                drawEnemies(g, prio);
                drawProjectiles(g, prio);
                drawHealthbars(g, prio);
                drawPickups(g, prio);
                drawRectangles(g, prio);
                drawTexts(g, prio);
                drawLines(g, prio);
            } catch (IndexOutOfBoundsException | NullPointerException ignored) {
                System.out.println("ERROR: Prio rendering error, synchronized did not work. Something deleted graphics without using synchronized remove functions");
            }
        }
    }

    private void drawPlayer(Graphics g, int prio) {
        if (player != null && player.getPrio() == prio) {
            double r = player.getSize() * scale;
            int x = (int) (player.getX() * scale - r);
            int y = (int) (player.getY() * scale - r);
            AffineTransform at = new AffineTransform();
            Image scaledImage = player.getImage().getScaledInstance((int) (player.getSize() * 2 * scale), (int) (player.getSize() * 2 * scale), Image.SCALE_DEFAULT);
            at.rotate(Math.toRadians(player.getDirection()), x + scaledImage.getWidth(null) / 2, y + scaledImage.getHeight(null) / 2);
            at.translate(x, y);
            Graphics2D g2d = (Graphics2D) g;

            g2d.drawImage(scaledImage, at, null);
        }
    }

    private void drawEnemies(Graphics g, int prio) {//draw every enemy
        while (iEn >= 0 && enemies.get(iEn).getPrio() == prio) {
            Enemy enemy = enemies.get(iEn);
            int r = (int) (enemy.getSize() * scale);
            int x = (int) (enemy.getX() * scale - r);
            int y = (int) (enemy.getY() * scale - r);
            AffineTransform at = new AffineTransform();
            Image scaledImage = enemy.getImage().getScaledInstance((int) (enemy.getSize() * 2 * scale), (int) (enemy.getSize() * 2 * scale), Image.SCALE_DEFAULT);
            at.rotate(Math.toRadians(enemy.playerDirection(player)), x + scaledImage.getWidth(null) / 2, y + scaledImage.getHeight(null) / 2);
            at.translate(x, y);
            Graphics2D g2d = (Graphics2D) g;
            g2d.drawImage(scaledImage, at, null);
            iEn--;
        }
    }

    private void drawProjectiles(Graphics g, int prio) {
        while (iPr >= 0 && projectiles.get(iPr).getPrio() == prio) {
            Projectile proj = projectiles.get(iPr);
            g.setColor(Color.YELLOW);
            g.fillOval((int) ((proj.getX() - proj.getSize()) * scale), (int) ((proj.getY() - proj.getSize()) * scale), (int) ((proj.getSize() * 2) * scale), (int) ((proj.getSize() * 2) * scale));
            iPr--;
        }
    }

    private void drawHealthbars(Graphics g, int prio) {
        while (iHe >= 0 && healthbars.get(iHe).getPrio() == prio) {
            Healthbar healthbar = healthbars.get(iHe);
            g.setColor(healthbar.getColor());
            g.fillRect((int) (healthbar.getX() * scale), (int) (healthbar.getY() * scale), (int) (healthbar.getWidth() * scale), (int) (healthbar.getHeight() * scale));
            iHe--;
        }
    }

    /*drawImage(Image image, x, y, lastX, lastY, startCropX, startCropY, endCropX, endCropY, Obeserver obs)
     *  First place the image on the x, y coordinates the top left corner should start in. The next two variables should be
     *  the last x, y coordinates of the pictures (where the bottom right corner is placed).
     *  The next 2 arguments should be 0 and the two after should be the size of the picture unless you want crop it. These 4 variables
     *  will crop the picture. The last thing should ALWAYS be null, we dont care about an observer.
     *  Elizabeth out.*/
    private void drawPickups(Graphics g, int prio) {
        while (iPi >= 0 && pickups.get(iPi).getPrio() == prio) {
            Pickup pickup = pickups.get(iPi);
            if (pickup instanceof PickupHealing) {
                g.drawImage(healthPickup, (int) (scale * (pickup.getX() - pickup.getSize())), (int) (scale * (pickup.getY() - pickup.getSize())),
                        (int) (scale * (pickup.getSize() + pickup.getX())), (int) (scale * (pickup.getSize() + pickup.getY())),
                        0, 0, healthPickup.getWidth(), healthPickup.getHeight(), null);
            } else if (pickup instanceof PickupAmmo) {
                g.drawImage(ammoPickup, (int) (scale * (pickup.getX() - pickup.getSize())), (int) (scale * (pickup.getY() - pickup.getSize())),
                        (int) (scale * (pickup.getSize() + pickup.getX())), (int) (scale * (pickup.getSize() + pickup.getY())),
                        0, 0, ammoPickup.getWidth(), ammoPickup.getHeight(), null);
            }
            iPi--;
        }
    }

    /* Image scaling is complicated, here is a guide:
     *  First place the image on the x, y coordinates the top left corner should start in. The next two variables should be
     *  the last x, y coordinates of the pictures (where the bottom right corner is placed).
     *  The next 2 arguments should be 0 and the two after should be the size of the picture unless you want crop it. These 4 variables
     *  will crop the picture. The last thing should ALWAYS be null, we dont care about an observer.
     *  Elizabeth out.
     */
    private void drawRectangles(Graphics g, int prio) {
        while (iRe >= 0 && rectangles.get(iRe).getPrio() == prio) {
            Rectangle rect = rectangles.get(iRe);
            if (rect.color == null) {
                g.drawImage(rect.im, (int) (rect.x * scale), (int) (rect.y * scale), (int) (rect.w * scale + rect.x * scale), (int) (rect.h * scale + rect.y * scale), 0, 0, rect.im.getWidth(), rect.im.getHeight(), null);
            } else {
                g.setColor(rect.color);
                g.fillRect((int) (rect.x * scale), (int) (rect.y * scale), (int) (rect.w * scale), (int) (rect.h * scale));
            }
            iRe--;
        }
    }

    private void drawTexts(Graphics g, int prio) {
        while (iTe >= 0 && texts.get(iTe).getPrio() == prio) {
            TextString textstr = texts.get(iTe);
            if (texts.get(iTe).color == null) g.setColor(Color.BLACK);
            else g.setColor(textstr.getColor());
            g.setFont(new Font(Config.FONTNAME, Font.PLAIN, (int) (textstr.fontSize * scale)));
            g.drawString(textstr.str, (int) (textstr.x * scale), (int) (textstr.y * scale));
            iTe--;
        }
    }

    private void drawLines(Graphics g, int prio) {
        while (iLi >= 0 && lines.get(iPr).getPrio() == prio) {
            Line line = lines.get(iLi);
            g.setColor(line.color);
            g.drawLine((int) (line.x1 * scale), (int) (line.y1 * scale), (int) (line.x2 * scale), (int) (line.y2 * scale));
            iLi--;
        }
    }


    /* ------------------------------------------------------------------------------------------------------------------------------------------------
     * Places objects in the list sorted. Upper layers (small prio-number, prio = 0 etc.) in the end of the list.
     * Currently linear insertion
     */

    public void placePlayer(Player player) {//there is only one player at a time
        this.player = player;
    }

    public void placeEnemy(Enemy enemy) {//add an enemy to the list
        int i = 0;
        while (i < enemies.size() && enemies.get(i).getPrio() < enemy.getPrio()) {
            i++;
        }
        enemies.add(i, enemy);
    }

    public void placeProjectile(Projectile proj) {
        int i = 0;
        while (i < projectiles.size() && projectiles.get(i).getPrio() < proj.getPrio()) {
            i++;
        }
        projectiles.add(i, proj);
    }

    public void placeHealthbar(Healthbar healthbar) {
        int i = 0;
        while (i < healthbars.size() && healthbars.get(i).getPrio() < healthbar.getPrio()) {
            i++;
        }
        healthbars.add(i, healthbar);
    }

    public void placePickup(Pickup pickup) {
        int i = 0;
        while (i < pickups.size() && pickups.get(i).getPrio() < pickup.getPrio()) {
            i++;
        }
        pickups.add(i, pickup);
    }

    public void placeRectangle(Rectangle rect) {
        int i = 0;
        while (i < rectangles.size() && rectangles.get(i).getPrio() < rect.getPrio()) {
            i++;
        }
        rectangles.add(i, rect);
    }

    public void placeText(TextString textstring) {
        int i = 0;
        while (i < texts.size() && texts.get(i).getPrio() < textstring.getPrio()) {
            i++;
        }
        texts.add(i, textstring);
    }

    public void placeLine(Line line) {
        int i = 0;
        while (i < lines.size() && lines.get(i).getPrio() < line.getPrio()) {
            i++;
        }
        lines.add(i, line);
    }

    public void placeButton(Button button) {
        buttons.add(button);
        this.placeRectangle(button.rect);
        if (button.text != null) {
            this.placeText(button.text);
        }
    }


    /* ------------------------------------------------------------------------------------------------------------------------------------------------
     * Removes choosen object
     *
     */
    synchronized public void removePlayer(Player player) {
        if (this.player == player) {
            this.player = null;
        }
    }

    synchronized public void removeEnemy(Enemy en) {//remove ONE enemy
        removeHealthbar(en.getHealthbar());
        enemies.remove(en);
    }

    synchronized public void removeProjectile(Projectile proj) {
        projectiles.remove(proj);
    }

    synchronized public void removeHealthbar(Healthbar healthbar) {
        healthbars.remove(healthbar);
    }

    synchronized public void removeText(TextString text) {
        texts.remove(text);
    }


    synchronized public void removeRectangle(Rectangle rect) {
        rectangles.remove(rect);
    }

    synchronized public void removeLine(Line line) {
        lines.remove(line);
    }

    synchronized public void removeButton(Button button) {
        buttons.remove(button);
        this.removeRectangle(button.rect);
        this.removeText(button.text);
    }

    synchronized public void removePickup(Pickup pickup) {
        pickups.remove(pickup);
    }

    synchronized public void removeCodeBlock(CodeBlock codeBlock) {
        // If it has accompaning buttons remove them
        if (codeBlock instanceof BranchBlock) {
            ((BranchBlock) codeBlock).getVarLeftButton().remove();
            ((BranchBlock) codeBlock).getVarRightButton().remove();
            ((BranchBlock) codeBlock).getOperatorButton().remove();
            removeRectangle(((BranchBlock) codeBlock).getTop());
            removeRectangle(((BranchBlock) codeBlock).getSide());
            removeRectangle(((BranchBlock) codeBlock).getBot());
        } else if (codeBlock instanceof GetBlock) {
            ((GetBlock) codeBlock).getVarButton().remove();
            ((GetBlock) codeBlock).getChangeButton().remove();
        } else if (codeBlock instanceof SetBlock) {
            ((SetBlock) codeBlock).getSetRightButton().remove();
            ((SetBlock) codeBlock).getSetLeftButton().remove();
        } else if (codeBlock instanceof ArithmeticBlock) {
            ((ArithmeticBlock) codeBlock).getVar1Button().remove();
            ((ArithmeticBlock) codeBlock).getVar2Button().remove();
            ((ArithmeticBlock) codeBlock).getResultButton().remove();
            ((ArithmeticBlock) codeBlock).getOperatorButton().remove();
        } else if (codeBlock instanceof RotatePlayerBlock) {
            ((RotatePlayerBlock) codeBlock).getVariableButton().remove();
        }
        removeRectangle(codeBlock);
        codeBlocks.remove(codeBlock);
    }


    /* ------------------------------------------------------------------------------------------------------------------------------------------------
     * Methods for the mouse
     *
     */
    private void checkButtons() {
        if (!clickedButton && this.isClicking()) {
            for (int i = 0; i < buttons.size(); i++) {
                Button button = buttons.get(i);
                // Are we pressing a button?
                if (this.m.isColliding(button.rect)) {
                    // But not on a rectangle
                    boolean codeBlockInWay = false;
                    for (int k = 0; k < codeBlocks.size(); k++) {
                        if (codeBlocks.get(k).getPrio() < button.getPrio()) {
                            if (this.m.isColliding(codeBlocks.get(k))) {
                                codeBlockInWay = true;
                                System.out.println("Rectangle on top");
                                break;
                            }
                        }
                    }
                    if (!codeBlockInWay) {
                        button.click();
                    }
                }
            }
            clickedButton = true;
        }
    }

    //lets the user click on and pick up a block
    private void moveBlocks() {
        for (int j = codeBlocks.size() - 1; j >= 0; j--) { //Go in reversed order as to make sure that the block on the top gets pciked up before one beneath it
            // Check if it's colliding with a codeBlock
            if (this.m.isColliding(codeBlocks.get(j)) && clicking && !pickedUp) {
                //Check if we are either picking up a normal CodeBlock or if we are clicking one of the non-transparent parts of a branch block
                if ((codeBlocks.get(j) instanceof BranchBlock)
                        && !(this.m.isColliding(((BranchBlock) codeBlocks.get(j)).getTop())
                        || this.m.isColliding(((BranchBlock) codeBlocks.get(j)).getSide())
                        || this.m.isColliding(((BranchBlock) codeBlocks.get(j)).getBot())
                )
                ) {
                    //Do nothing because we clicked the transparent area of a branch block
                } else {
                    // But not with a button
                    boolean buttonInWay = false;
                    for (int k = 0; k < buttons.size(); k++) {
                        if (buttons.get(k).getPrio() < codeBlocks.get(j).getPrio()) {
                            if (this.m.isColliding(buttons.get(k).rect)) {
                                buttonInWay = true;
                                break;
                            }
                        }
                    }
                    if (!buttonInWay) {
                        pickedUp = true;
                        selectedCodeBlock = codeBlocks.get(j);
                        selectedCodeBlock.update(true); // Set a temporary prio when picked up
                        scaleBranches(selectedCodeBlock, null, false, true); // Shrink branchblocks affected

                        //Check if there is a CodeBlock pointing at us and if so remove that pointer
                        if (selectedCodeBlock.getPrev() != null) {
                            //Check if the block pointing at us is poiting at us as its next, or if we are its branch
                            if (selectedCodeBlock.getPrev().getNext() == selectedCodeBlock) {
                                selectedCodeBlock.getPrev().setNext(null);
                            } else if (selectedCodeBlock.getPrev() instanceof BranchBlock) {
                                ((BranchBlock) selectedCodeBlock.getPrev()).setBranch(null);
                            }
                        }
                        selectedCodeBlock.setPrev(null); //Set our previous reference to be null for good measure
                        m.pickedUp(selectedCodeBlock);
                    }
                }
            }
            if (pickedUp) {
                moveBlocksFrom(selectedCodeBlock, selectedCodeBlock.getX(), selectedCodeBlock.getY(), true, true); // X and Y not needed when using mouse
            }

            //dropped the object, time to snap
            if (!this.isClicking()) {
                if (pickedUp) {
                    pickedUp = false;
                    moveBlocksFrom(selectedCodeBlock, selectedCodeBlock.getX(), selectedCodeBlock.getY(), true, false); // X and Y not needed when using mouse
                    snapBlocks(selectedCodeBlock);
                    selectedCodeBlock = null;//reset
                }
            }
        }
    }

    /* x is the x-coordinate where the blocks starting at current should
     * be placed. startY specifies where, in y-direction, the block current is placed. */
    private void moveBlocksFrom(CodeBlock current, int x, int startY, boolean useMouse, boolean dragging) {
        Rectangle previous = null;

        if (useMouse) {
            this.m.moveByMouse(current);
        }

        while (current != null) {
            if (!useMouse) {
                current.setX(x);
                if (previous == null) {
                    current.setY(startY);
                } else {
                    current.setY(previous.getY() + previous.getHeight());
                }

            } else {
                useMouse = false;
            }

            if (current instanceof BranchBlock) {
                BranchBlock b = (BranchBlock) current;
                if (b.getBranch() != null) {
                    moveBlocksFrom(b.getBranch(), x + b.getSide().getWidth(), b.getY() + b.getTop().getHeight(), false, dragging);
                }
            }

            current.update(dragging);

            previous = current;
            current = current.getNext();
        }
    }

    /**
     * Removes block that we have rightclicked on
     */
    private void removeBlocks() {
        for (int k = codeBlocks.size() - 1; k >= 1; k--) {   // IMPORTANT! Startblock == index 0 in list and we dont want to remove it that is why k >= 1
            // Remove only if:
            // 1. Rightclicking
            // 2. Have not already removed one block with this rightclick
            // 3a. Hits a non-branchblock or
            // 3b. Hits a branchblocks "sides"
            if ((rightClicking && !alreadyRemoved && !pickedUp) && (
                    (!(codeBlocks.get(k) instanceof BranchBlock) && this.m.isColliding(codeBlocks.get(k)))
                            || ((codeBlocks.get(k) instanceof BranchBlock) && (this.m.isColliding(((BranchBlock) codeBlocks.get(k)).getTop()) || this.m.isColliding(((BranchBlock) codeBlocks.get(k)).getSide()) || this.m.isColliding(((BranchBlock) codeBlocks.get(k)).getBot()))))) {
                alreadyRemoved = true;
                gameLoop.keyboard.setTyping(false);


                //b will be removed
                CodeBlock b = codeBlocks.get(k);

                // Shrink branchblocks
                scaleBranches(b, null, false, false);

                //Check if there is a CodeBlock pointing at us and if so remove that pointer
                if (b.getPrev() != null) {
                    //Check if the block pointing at us is poiting at us as its next, or if we are its branch
                    if (b.getPrev().getNext() == b) {
                        //The block we are removing is a branchblock
                        if (b instanceof BranchBlock) {
                            //Set next to start of branch
                            b.getPrev().setNext(((BranchBlock) b).getBranch());
                            //Goes to end of branch and sets its next to the next of the removed block
                            CodeBlock end = ((BranchBlock) b).getBranch();
                            // Add blocks in the branchblock if we have any
                            if (end != null) {
                                end.setPrev(b.getPrev());
                                while (end.getNext() != null) {
                                    end = end.getNext();
                                }
                            } else {
                                end = b.getPrev();
                            }
                            end.setNext(b.getNext());

                            //Sets the block below the removed block's prev to the end of the branch
                            if (b.getNext() != null) {
                                b.getNext().setPrev(end); // set next blocks prev to the previous block
                            }

                        } else {
                            b.getPrev().setNext(b.getNext());
                            if (b.getNext() != null) {
                                b.getNext().setPrev(b.getPrev()); // set next blocks prev to the previous block
                            }
                        }
                    } else if (b.getPrev() instanceof BranchBlock) { //The block before is a branchblock
                        //The block we are removing is a branchblock
                        if (b instanceof BranchBlock) {
                            //Set next to start of branch
                            ((BranchBlock) b.getPrev()).setBranch(((BranchBlock) b).getBranch());
                            //Goes to end of branch and sets its next to the next of the removed block
                            CodeBlock end = ((BranchBlock) b).getBranch();
                            if (end != null) {
                                end.setPrev(b.getPrev());
                                while (end.getNext() != null) {
                                    end = end.getNext();
                                }
                                end.setNext(b.getNext());
                            }

                            //Sets the block below the removed block's prev to the end of the branch
                            if (b.getNext() != null) {
                                b.getNext().setPrev(end); // set next blocks prev to the previous block
                            }

                        } else {
                            ((BranchBlock) b.getPrev()).setBranch(b.getNext());
                            if (b.getNext() != null) {
                                b.getNext().setPrev(b.getPrev()); // set next blocks prev to the previous block
                            }
                        }
                    }
                    moveBlocksFrom(b.getPrev(), b.getPrev().getX(), b.getPrev().getY(), false, false);
                } else {
                    if (b.getNext() != null) {
                        b.getNext().setPrev(null);
                    }
                    if (b instanceof BranchBlock) {
                        if (((BranchBlock) b).getBranch() != null) {
                            ((BranchBlock) b).getBranch().setPrev(null);
                        }
                    }
                }


                //Set our reference to be null for good measure
                b.setPrev(null);
                if (b instanceof BranchBlock) {
                    // Shrink branchblocks
                    if (((BranchBlock) b).getBranch() != null) {
                        scaleBranches(((BranchBlock) b).getBranch(), null, true, true);
                    }
                    ((BranchBlock) b).setBranch(null);
                }
                b.setNext(null);

                //Remove "real"
                gameLoop.codeBlocks.remove(b);
                //Removes graphically
                removeCodeBlock(b);
            }
        }
    }

    /*
     * Takes a block and the information fo whether that block was added to the code or removed
     * and then scales the rest of the code to fit the new block
     */
    private void scaleBranches(CodeBlock start, CodeBlock end, boolean add, boolean wholeTree) {
        CodeBlock current = start;
        CodeBlock prev = current.getPrev();

        // Include height from all blocks in chain up until end
        int addedHeight = 0;
        if (wholeTree) {
            CodeBlock b = current;
            boolean keepGoing = true;
            while (keepGoing) {
                if (b == end) {
                    keepGoing = false;
                }
                if (b != null) {
                    if (add) {
                        addedHeight += b.getHeight();
                    } else {
                        addedHeight -= b.getHeight();
                    }
                    b = b.getNext();
                }
            }
        } else {
            if (add) {
                addedHeight += current.getHeight();
            } else {
                addedHeight -= current.getHeight();
            }
        }

        /*
         * 1. Follow prev pointer till a BranchBlock that current is the branch of is found
         * 2. Update the height of the BranchBlock and move all children up or down (depending on the bool above)
         * 3. Repeat from step 1 till the begining of the code is found
         */
        while (prev != null) {
            if (prev instanceof BranchBlock) {
                // For each branchblock we are inside:
                if (((BranchBlock) prev).getBranch() == current) {
                    // 1. Change its height
                    prev.setHeight(prev.getHeight() + addedHeight);
                    prev.update(false);

                    // 2. Move blocks below the same height
                    moveBranch(prev.getNext(), addedHeight);
                }
            }

            current = prev;
            prev = prev.getPrev();
        }
    }

    private void moveBranch(CodeBlock current, int addedHeight) {
        while (current != null) {
            current.setY(current.getY() + addedHeight);
            current.update(false);
            if (current instanceof BranchBlock) {
                moveBranch(((BranchBlock) current).getBranch(), addedHeight);
            }
            current = current.getNext();
        }
    }

    //snap together two code blocks
    //we only compare right after we drop -  no need to compare things that doesn't move
    private void snapBlocks(CodeBlock selected) {
        for (int j = codeBlocks.size() - 1; j >= 0; j--) { //Go in reverse order so that it snaps to the upmost block
            CodeBlock compare = codeBlocks.get(j);
            if (selected != compare) {

                //add to the bottom
                if (selected.getX() + selected.getWidth() >= compare.getX()
                        && selected.getX() <= compare.getX() + compare.getWidth()
                        && selected.getY() <= (compare.getY() + compare.getHeight() + Config.BLOCK_SNAP)
                        && selected.getY() >= (compare.getY() + compare.getHeight() - Config.BLOCK_SNAP)
                ) {

                    pickedUp = false;
                    clicking = false;

                    //Allow placing blocks in the middle
                    CodeBlock end = selected;
                    while (end.getNext() != null) {
                        end = end.getNext();
                    }
                    end.setNext(compare.getNext());
                    if (compare.getNext() != null) {
                        compare.getNext().setPrev(end);
                    }
                    compare.setNext(selected);
                    selected.setPrev(compare); //TODO Do not allow placing blocks in the middle or push them down. Update prev and next and move blocks

                    // Extend branchblocks
                    scaleBranches(selected, end, true, true);

                    moveBlocksFrom(selected, compare.getX(), compare.getY() + compare.getHeight(), false, false);
                    break;
                }

                //add to the side
                if (compare instanceof BranchBlock) {
                    //Only if the branch is empty we will allow snapping to the side. Otherwise snap below the blocks in the branch
                    if (((BranchBlock) compare).getBranch() == null) {
                        if ((selected.getX() + selected.getWidth() / 2) >= (compare.getX())
                                && (selected.getX() + selected.getWidth() / 2) <= (compare.getX() + compare.getWidth())
                                && selected.getY() >= (compare.getY()) && selected.getY() <= (compare.getY() + 3 * compare.getHeight() / 4)) {

                            moveBlocksFrom(selected, ((BranchBlock) compare).getSide().getX() + ((BranchBlock) compare).getSide().getWidth(), ((BranchBlock) compare).getSide().getY(), false, false);

                            pickedUp = false;
                            clicking = false;

                            BranchBlock t = (BranchBlock) compare;
                            t.setBranch(selected);
                            selected.setPrev(compare);

                            // Extend branchblocks
                            scaleBranches(selected, null, true, true);
                            break;
                        }
                    }
                }
            }
        }
    }

    public boolean isClicking() {
        return clicking;
    }

    /* ------------------------------------------------------------------------------------------------------------------------------------------------
     * Remove everything
     *
     */

    public void removeEverything() {//completly REMOVE everything from the screen

        removePlayer(this.player);

        //Goes backwards because list gets smaller

        for (int i = buttons.size() - 1; i >= 0; i--) {
            removeButton(buttons.get(i));
        }

        for (int i = texts.size() - 1; i >= 0; i--) {
            removeText(texts.get(i));
        }

        for (int i = lines.size() - 1; i >= 0; i--) {
            removeLine(lines.get(i));
        }

        for (int i = enemies.size() - 1; i >= 0; i--) {
            removeEnemy(enemies.get(i));
        }

        for (int i = projectiles.size() - 1; i >= 0; i--) {
            removeProjectile(projectiles.get(i));
        }

        for (int i = rectangles.size() - 1; i >= 0; i--) {
            removeRectangle(rectangles.get(i));
        }

        for (int i = healthbars.size() - 1; i >= 0; i--) {
            removeHealthbar(healthbars.get(i));
        }

        for (int i = pickups.size() - 1; i >= 0; i--) {
            pickups.remove(i);
        }

        for (int i = codeBlocks.size() - 1; i >= 0; i--) {
            codeBlocks.remove(i);
        }
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        int scrollAmount = e.getWheelRotation() * Config.SCROLLSPEED;

        for (int i = 0; i < codeBlocks.size(); i++) {
            CodeBlock block = codeBlocks.get(i);
            block.setY(codeBlocks.get(i).getY() + scrollAmount); // Add scrolling direction to Settings?
            if (block instanceof ArithmeticBlock) {
                ArithmeticBlock b = (ArithmeticBlock) block;
                b.getVar1Button().setY(b.getVar1Button().getY() + scrollAmount);
                b.getVar2Button().setY(b.getVar2Button().getY() + scrollAmount);
                b.getResultButton().setY(b.getResultButton().getY() + scrollAmount);
                b.getOperatorButton().setY(b.getOperatorButton().getY() + scrollAmount);
            } else if (block instanceof BranchBlock) {
                BranchBlock b = (BranchBlock) block;
                b.getTop().setY(b.getTop().getY() + scrollAmount);
                b.getSide().setY(b.getSide().getY() + scrollAmount);
                b.getBot().setY(b.getBot().getY() + scrollAmount);
                b.getVarLeftButton().setY(b.getVarLeftButton().getY() + scrollAmount);
                b.getVarRightButton().setY(b.getVarRightButton().getY() + scrollAmount);
                b.getOperatorButton().setY(b.getOperatorButton().getY() + scrollAmount);
            } else if (block instanceof GetBlock) {
                GetBlock b = (GetBlock) block;
                b.getVarButton().setY(b.getVarButton().getY() + scrollAmount);
                b.getChangeButton().setY(b.getChangeButton().getY() + scrollAmount);
            } else if (block instanceof SetBlock) {
                SetBlock b = (SetBlock) block;
                b.getSetLeftButton().setY(b.getSetLeftButton().getY() + scrollAmount);
                b.getSetRightButton().setY(b.getSetRightButton().getY() + scrollAmount);
            } else if (block instanceof RotatePlayerBlock) {
                RotatePlayerBlock b = (RotatePlayerBlock) block;
                b.getVariableButton().setY(b.getVariableButton().getY() + scrollAmount);
            }
        }
        //System.out.println(scrollAmount);
    }
}

