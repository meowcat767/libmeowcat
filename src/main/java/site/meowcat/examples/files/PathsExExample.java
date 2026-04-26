package site.meowcat.examples.files;

import site.meowcat.system.files.PathsEx;

/**
 * Example using PathsEx to get various system paths. Note that the appName parameters are just examples; in production, use your real app name.
 * @author meowcat767
 */
public class PathsExExample {
    public static void main(String[] args) {
        System.out.println("Your home directory is:" + PathsEx.home());
        System.out.println("Your documents directory is:" + PathsEx.documents());
        System.out.println("Your data directory is:" + PathsEx.dataDir("libmeowcat")); // Note that I'm using example appNames; in prod use your real one.
        System.out.println("Your config directory is:" + PathsEx.configDir("MyCoolApp"));
        System.out.println("Your cache directory is:" + PathsEx.cacheDir("DukesDungeon"));
    }
}
