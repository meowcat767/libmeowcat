package site.meowcat.system;

/**
 * Fetch the platform of the current machine.
 * @author meowcat767
 */
public final class Platform {

    public static final String OS =
            System.getProperty("os.name").toLowerCase();

    public static final String ARCH =
            System.getProperty("os.arch").toLowerCase();

    private Platform() {}

    public static boolean isWindows() {
        return OS.contains("win");
    }

    public static boolean isLinux() {
        return OS.contains("linux");
    }

    public static boolean isMac() {
        return OS.contains("mac");
    }

    public static boolean isUnix() {
        return isLinux() || OS.contains("nix") || OS.contains("nux") || OS.contains("aix");
    }

    public static boolean isArm() {
        return ARCH.contains("arm");
    }

    public static boolean is64Bit() {
        return ARCH.contains("64");
    }

    public static String getVersion() {
        return System.getProperty("os.version");
    }

    public static String getArch() {
        return System.getProperty("os.arch");
    }
}
