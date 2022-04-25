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

        for (char c : utils.getAlphabet()) {
            System.out.println(c);
        }

        for (String s : utils.getInput()) {
            System.out.println(s);
        }

        System.out.println("Creating NFA...");
        NFA nfa = new NFA(utils.getRegex(), utils.getAlphabet());
        System.out.println("Converting to DOT format...");
        utils.writeNFA(nfa.convertToDot());
        System.out.println("DONE");

        System.exit(returnCode);
    }
}
