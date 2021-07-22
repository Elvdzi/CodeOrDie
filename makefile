CC = gcc

FLAGS =

CLASSES = exec.c

NAME = CodeOrDie

default: 
	$(CC) $(FLAGS) $(CLASSES) -o $(NAME)
