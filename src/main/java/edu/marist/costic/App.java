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

        System.out.println("Hello World CO!");

        System.exit(returnCode);
    }
}
