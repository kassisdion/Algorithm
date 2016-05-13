#! /bin/sh

set -x

java NeedlemanWunschAlg abcdefg qbcdty
java SmithWatermanAlg "a b c d e f" "q b c d t y"
java SmithWatermanAlg abcdefg qbcdty
java NeedlemanWunschAlg hello_world! we_are_the_world!
