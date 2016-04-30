#! /bin/sh

for d in testinput/*; do
    echo java Solver $d
    java Solver $d
done
