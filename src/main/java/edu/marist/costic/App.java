package edu.marist.costic;

/**
 * Main app class.
 */
public final class App {
    /** App class constructor. */
    protected App() {
    }

    /**
     * Says hello to the world!
     * @param args The arguments of the program.
     */
    public static void main(final String[] args) {
        int returnCode = 0;
        Utils utils = new Utils();

        utils.processArgs(args);

        System.out.println("Creating NFA...");
        NFA nfa = new NFA(utils.getRegex(), utils.getAlphabet());
        System.out.println("Converting NFA to DOT format...");
        utils.writeNFA(nfa.convertToDot());

        System.out.println("Creating DFA...");
        DFA dfa = new DFA(nfa, utils.getAlphabet());
        System.out.println("Converting DFA to DOT format...");
        utils.writeDFA(dfa.convertToDot());

        System.out.println("DONE");

        System.exit(returnCode);
    }
}
