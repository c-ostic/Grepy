# GrepyCO
Grep-like program to detect strings with regex by converting it to NFAs and DFAs.
Currently, only generates an NFA dot file.

## Usage
```
java -jar target/grepyCO-0.1.0-jar-with-dependencies.jar REGEX INPUTFILE
grepyCO
Version: 0.0.2
usage: grepyCO
 -h   Display this help text
 -v   Verbose mode
 -n   Specify NFA dot file
 -d   Specify DFA dot file
```
