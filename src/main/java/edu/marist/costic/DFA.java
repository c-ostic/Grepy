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
        Set<Integer> startingSet = nfa.epsilonClosure(nfa.getStartState());
        unprocessedSubsets.add(startingSet);
        subsetToDFAState.put(startingSet, 0);


        while (!unprocessedSubsets.isEmpty()) {
            Set<Integer> currentSet = unprocessedSubsets.remove();

            // if the set is in the processedSets then skip it
            if (processedSubsets.contains(currentSet)) {
                continue;
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

                // get the next available state to give to the next subset
                int nextState = states;
                states++;
                subsetToDFAState.put(nextSubset, nextState);

                deltaFunction.put(new StateSymbolPair(currentState, c), nextState);

                unprocessedSubsets.add(nextSubset);
            }
        }
    }
}
