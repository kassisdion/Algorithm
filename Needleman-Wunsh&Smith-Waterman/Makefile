
JAVA_SRCS = SmithWatermanAlg.java NeedlemanWunschAlg.java

CLASSES = $(JAVA_SRCS:.java=.class)

JAVAC = javac

.PHONY: clean

all: $(CLASSES)

$(CLASSES): $(JAVA_SRCS)
	$(JAVAC) $(JFLAGS) $^

clean:
	rm -f $(CLASSES)
	rm -f *.class

