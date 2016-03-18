CC		=	gcc
RM		=	rm -f
ECHO		=	@echo

NAME		=	insertion
NAME_MERGE	= 	merge_sort
NAME_INSERTION	=	insertion_sort

SRC_MERGE	=	merge_sort.c
SRC_INSERTION	=	insertion_sort.c

OBJ_MERGE	=	$(SRC_MERGE:.c=.o)
OBJ_INSERTION	=	$(SRC_INSERTION:.c=.o)

CFLAGS		=	-W	\
			-Wall	\
			-Wextra	\

all:			$(NAME)

$(NAME):		$(NAME_MERGE) $(NAME_INSERTION)


$(NAME_MERGE):		$(OBJ_MERGE)
			$(CC) -o $(NAME_MERGE) $(CFLAGS) $(OBJ_MERGE)

$(NAME_INSERTION):	$(OBJ_INSERTION)
			$(CC) -o $(NAME_INSERTION) $(CFLAGS) $(OBJ_INSERTION)


clean:
			$(RM) $(OBJ_MERGE)
			$(RM) $(OBJ_INSERTION)

fclean:			clean
			$(RM) $(NAME_MERGE)
			$(RM) $(NAME_INSERTION)

re:			fclean all
