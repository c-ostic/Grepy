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

        utils.log("Creating NFA...");
        NFA nfa = new NFA(utils.getRegex(), utils.getAlphabet());
        utils.log("Converting NFA to DOT format...");
        utils.writeNFA(nfa.convertToDot());

        utils.log("Creating DFA...");
        DFA dfa = new DFA(nfa, utils.getAlphabet());
        utils.log("Converting DFA to DOT format...");
        utils.writeDFA(dfa.convertToDot());

        utils.log("Processing Input...");
        utils.log("");

        utils.log("Accepted Strings:");

        for (String s : utils.getInput()) {
            if (dfa.accepts(s)) {
                System.out.println(s);
            }
        }

        System.exit(returnCode);
    }
}
