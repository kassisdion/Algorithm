
JAVA_SRCS = Board.java Solver.java

CLASSES = $(JAVA_SRCS:.java=.class)

JAVAC = javac
JFLAGS = -g

.PHONY: clean

all: $(CLASSES)

$(CLASSES): $(JAVA_SRCS)
	$(JAVAC) $(JFLAGS) $^

clean:
	rm -f $(CLASSES)
	rm -f *.class

