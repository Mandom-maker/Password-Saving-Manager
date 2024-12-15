    package sample.demo;

    /**
     * The main class of the application, responsible for initializing and starting the program.
     * This class contains the {@code main} method, which is the entry point of the application.
     */
    public class Main {

        /**
         * The entry point of the application.
         * Creates an instance of the {@link StartWork} class and calls the {@code StarTtoStart} method
         * to initialize and start the program.
         *
         * @param args The command-line arguments passed to the application.
         */
        public static void main(String[] args) {
            StartWork mainclass = new StartWork();
            mainclass.StartToStart(args);
        }
    }