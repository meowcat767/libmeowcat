package site.meowcat.system.files;

import site.meowcat.system.Platform;

import java.nio.file.Path;

/**
 * Useful methods for working with paths.
 * @author meowcat767
 */
public class PathsEx {
    /**
     * Get the home directory.
     * @return User home directory.
     */
    public static Path home() {
        return Path.of(System.getProperty("user.home"));
    }

    /**
     * Get the current working directory.
     * @return The current working directory.
     */
    public static Path cwd() {
        return Path.of("user.dir");
    }

    /**
     * Get the temp path
     * @return Current platform's tmp path.
     */

    public static Path temp() {
        return Path.of(System.getProperty("java.io.tmpdir"));
    }

    /**
     * Get the system configuration path, platform-dependent.
     * @return The system configuration path.
     */

    public static Path configDir(String appName) {
        return switch (Platform.OS) {
            case "win" -> Path.of(System.getenv("APPDATA"), appName);
            case "mac" -> Path.of(System.getProperty("user.home"), "Library", "Application Support", appName);
            default -> Path.of(System.getProperty("user.home"), ".config", appName);
        };
    }

    /**
     * Get the system data directory
     * @return The system data directory, platform-dependent.
     */

    public static Path dataDir(String appName) {
        return switch (Platform.OS) {
            case "win" -> Path.of(System.getenv("LOCALAPPDATA"), appName);
            case "mac" -> Path.of(System.getProperty("user.home"), "Library", "Application Support", appName);
            default -> Path.of(System.getProperty("user.home"), ".local", "share", appName);
        };
    }

    /**
     * Get the system cache directory, platform-dependent.
     * @return The system cache directory.
     */

    public static Path cacheDir(String appName) {
        return switch (Platform.OS) {
            case "win" -> Path.of(System.getenv("LOCALAPPDATA"), appName, "Cache");
            case "mac" -> Path.of(System.getProperty("user.home"), "Library", "Caches", appName);
            default -> Path.of(System.getProperty("user.home"), ".cache", appName);
        };
    }

    /**
     * Join paths safely
     * @return The combined paths
     */

    public static Path join(String first, String... more) {
        return Path.of(first, more);
    }

    /**
     * Ensure a given directory exists.
     * @return The directory path, if it exists.
     */

    public static Path ensureDir(Path path) {
        try {
            java.nio.file.Files.createDirectories(path);
            return path;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create directory: " + path, e);
        }
    }

    /**
     * Resolve relative paths correctly.
     * @return The resolved path.
     */

    public static Path resolve(Path base, String... parts) {
        Path p = base;
        for (String part : parts) {
            p = p.resolve(part);
        }
        return p;
    }

    /**
     * Normalize paths.
     * @return The absolute path.
     */

    public static Path normalize(Path path) {
        return path.toAbsolutePath().normalize();
    }

    // other nice features that might be useful later

    /**
     * Get the desktop directory.
     * @return The desktop path.
     */
    public static Path desktop() {
        return home().resolve("Desktop");
    }

    /**
     * Get the downloads directory.
     * @return The downloads path.
     */
    public static Path downloads() {
        return home().resolve("Downloads");
    }

    /**
     * Get the documents directory.
     * @return The documents path.
     */
    public static Path documents() {
        return home().resolve("Documents");
    }
}
