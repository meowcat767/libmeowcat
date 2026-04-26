package site.meowcat.system;

/**
 * Platform identification and system property utilities.
 * @author meowcat767
 */
public final class Platform {

    /**
     * The operating system name, in lowercase.
     */
    public static final String OS =
            System.getProperty("os.name").toLowerCase();

    /**
     * The operating system architecture, in lowercase.
     */
    public static final String ARCH =
            System.getProperty("os.arch").toLowerCase();

    private Platform() {}

    /**
     * Checks if the current OS is Windows.
     * @return true if Windows, false otherwise
     */
    public static boolean isWindows() {
        return OS.contains("win");
    }

    /**
     * Checks if the current OS is Linux.
     * @return true if Linux, false otherwise
     */
    public static boolean isLinux() {
        return OS.contains("linux");
    }

    /**
     * Checks if the current OS is macOS.
     * @return true if macOS, false otherwise
     */
    public static boolean isMac() {
        return OS.contains("mac");
    }

    /**
     * Checks if the current OS is a Unix-like system.
     * @return true if Unix-like, false otherwise
     */
    public static boolean isUnix() {
        return isLinux() || OS.contains("nix") || OS.contains("nux") || OS.contains("aix");
    }

    /**
     * Checks if the architecture is ARM-based.
     * @return true if ARM, false otherwise
     */
    public static boolean isArm() {
        return ARCH.contains("arm");
    }

    /**
     * Checks if the architecture is 64-bit.
     * @return true if 64-bit, false otherwise
     */
    public static boolean is64Bit() {
        return ARCH.contains("64");
    }

    /**
     * Gets the operating system version.
     * @return the OS version string
     */
    public static String getVersion() {
        return System.getProperty("os.version");
    }

    /**
     * Gets the operating system architecture.
     * @return the OS architecture string
     */
    public static String getArch() {
        return System.getProperty("os.arch");
    }
}
