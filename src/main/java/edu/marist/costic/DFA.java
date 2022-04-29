package edu.marist.costic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * Represents a DFA.
 */
public class DFA {
    // the delta function maps a state to the state it is connected to on a particular symbol
    private Map<StateSymbolPair, Integer> deltaFunction;
    private int states;
    private Set<Character> alphabet;

    // due to the nature of subset construction, the start state is always 0, but there could be multiple end states
    private Set<Integer> endStates;

    // this will be used for printing to the .dot so each state is linked to the
    // string representation of the NFA subsets they represent
    private Map<Integer, String> subsetLabels;

    /**
     * Constructs a DFA given an NFA and an alphabet.
     * @param nfa
     * @param alphabet
     */
    public DFA(NFA nfa, Set<Character> alphabet) {
        this.alphabet = alphabet;

        states = 0;

        deltaFunction = new HashMap<StateSymbolPair, Integer>();
        endStates = new HashSet<Integer>();
        subsetLabels = new HashMap<Integer, String>();

        subsetConstruction(nfa);
    }

    /**
     * Constructs the DFA with the NFA using subset construction.
     * @param nfa
     */
    private void subsetConstruction(NFA nfa) {
        // A Queue of all subsets that still need to be processed
        Queue<Set<Integer>> unprocessedSubsets = new LinkedList<Set<Integer>>();

        // A Set of the subsets that have already been processed so they don't get processed again
        Set<Set<Integer>> processedSubsets = new HashSet<Set<Integer>>();

        // Maps each subset used in subset construction to the state it represents in the DFA
        Map<Set<Integer>, Integer> subsetToDFAState = new HashMap<Set<Integer>, Integer>();


        // the first set added is everything available from the start of the NFA and is given the state of 0
        Set<Integer> startingSet = new HashSet<Integer>();
        startingSet.add(nfa.getStartState());
        startingSet.addAll(nfa.epsilonClosure(nfa.getStartState()));
        unprocessedSubsets.add(startingSet);
        subsetToDFAState.put(startingSet, 0);
        states++;


        while (!unprocessedSubsets.isEmpty()) {
            Set<Integer> currentSet = unprocessedSubsets.remove();

            // if the set is in the processedSets then skip it
            if (processedSubsets.contains(currentSet)) {
                continue;
            } else {
                processedSubsets.add(currentSet);
            }


            // get the representative state of this subset
            int currentState = subsetToDFAState.get(currentSet);

            // if this subset contains the NFA end state, add this state to the DFA's end states
            if (currentSet.contains(nfa.getEndState())) {
                endStates.add(currentState);
            }

            // set the string representation for this state
            subsetLabels.put(currentState, currentSet.toString());

            // for each symbol in the alphabet, get the next set of possible states with that symbol
            // and add them to the delta function
            for (char c : alphabet) {
                Set<Integer> nextSubset = new HashSet<Integer>();

                // get the next states from all the states in the current set
                for (int state : currentSet) {
                    nextSubset.addAll(nfa.getConnectedStates(state, c));
                }

                // if the nextSubset is not empty, continue processing
                if (!nextSubset.isEmpty()) {
                    int nextState = subsetToDFAState.getOrDefault(nextSubset, -1);
                    if (nextState == -1) {
                        nextState = states;
                        states++;
                        subsetToDFAState.put(nextSubset, nextState);
                    }

                    deltaFunction.put(new StateSymbolPair(currentState, c), nextState);

                    unprocessedSubsets.add(nextSubset);
                } // end if
            } // end for
        } // end while
    } // end method

    /**
     * Converts the DFA to dot format.
     * @return the dot format as a string.
     */
    public String convertToDot() {
        String dotFormat = "digraph dfa {\n";

        // add the double circle to the end states
        for (int endState : endStates) {
            dotFormat += "\t" + endState + " [shape=doublecircle];\n";
        }

        // add the start state with a fake empty state to simulate the first arrow
        dotFormat += "\tstart [label=\"\",shape=none];\n";
        dotFormat += "\tstart -> " + 0 + ";\n\n";

        // add subset labels for each of the nodes
        for (int state : subsetLabels.keySet()) {
            dotFormat += "\t" + state + " [label=\"" + subsetLabels.get(state) + "\"];\n";
        }

        // add each of the node pairs to the string
        for (StateSymbolPair pair : deltaFunction.keySet()) {
            int dest = deltaFunction.get(pair);
            if (pair.getSymbol() == StateSymbolPair.EPSILON) {
                dotFormat += "\t" + pair.getState() + " -> " + dest + " [label=epsilon];\n";
            } else {
                dotFormat += "\t" + pair.getState() + " -> " + dest + " [label=" + pair.getSymbol() + "];\n";
            }
        }

        dotFormat += "}";

        return dotFormat;
    }

    /**
     * Simulates the DFA and determines if a string s is accepted or rejected.
     * @param s the string to test
     * @return true if the string is accepted, false otherwise
     */
    public boolean accepts(String s) {
        int currentState = 0;
        for (int i = 0; i < s.length(); i++) {
            currentState = deltaFunction.getOrDefault(new StateSymbolPair(currentState, s.charAt(i)), -1);
            if (currentState == -1) {
                return false;
            }
        }
        return endStates.contains(currentState);
    }
}
