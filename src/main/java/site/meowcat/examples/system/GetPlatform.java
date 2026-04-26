package site.meowcat.examples.system;

import site.meowcat.system.Platform;

public class GetPlatform {
    public static void main(String[] args) {
        if (Platform.isLinux()) {
            System.out.println("Greetings, penguin.");
        } else if (Platform.isWindows()) {
            System.out.println("You are using Windows.");
        } else if (Platform.isMac()) {
            System.out.println("You are using Mac.");

        }
    }
}
