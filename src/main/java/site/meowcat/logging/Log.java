package site.meowcat.logging;

public class Log {
    // console colours
    private static final String RESET = "\u001B[0m"; // flush
    private static final String INFO = "\u001B[32m"; // green
    private static final String WARNING = "\u001B[33m"; // yellow
    private static final String ERROR = "\u001B[31m"; // red

    public void info(String message) {
        System.out.println(INFO + message + RESET);
    }

    public void warn(String message) {
        System.out.println(WARNING + message + RESET);
    }

    public void error(String message) {
        System.out.println(ERROR + message + RESET);
    }
}
