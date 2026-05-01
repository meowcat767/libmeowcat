# libmeowcat
[![](https://jitpack.io/v/meowcat767/libmeowcat.svg)](https://jitpack.io/#meowcat767/libmeowcat)

A versatile Java/Kotlin library providing cross-platform utilities for system integration, file operations, networking, and UI components.

## Overview

**libmeowcat** is a comprehensive utility library designed to simplify common development tasks including:
- Platform detection and system property access
- Cross-platform file system operations
- Network scanning and graph visualization
- Swing UI helpers with fluent API

## Features

### 🖥️ Platform Detection (`site.meowcat.system.Platform`)
Easily detect the operating system and architecture:
```java
if (Platform.isWindows()) {
    // Windows-specific code
}
if (Platform.isLinux()) {
    // Linux-specific code
}
if (Platform.isMac()) {
    // macOS-specific code
}
boolean is64Bit = Platform.is64Bit();
boolean isArm = Platform.isArm();
```

**Supported methods:**
- `isWindows()` - Detect Windows OS
- `isLinux()` - Detect Linux OS
- `isMac()` - Detect macOS
- `isUnix()` - Detect Unix-like systems
- `isArm()` - Detect ARM architecture
- `is64Bit()` - Detect 64-bit architecture
- `getVersion()` - Get OS version string
- `getArch()` - Get architecture string

### 📁 File System Utilities (`site.meowcat.system.files`)

#### PathsEx
Platform-aware path utilities for standard directories:
```java
// Get standard directories
Path home = PathsEx.home();
Path downloads = PathsEx.downloads();
Path documents = PathsEx.documents();
Path desktop = PathsEx.desktop();

// Platform-specific app directories
Path configDir = PathsEx.configDir("myapp");  // ~/.config/myapp on Linux, etc.
Path dataDir = PathsEx.dataDir("myapp");      // ~/.local/share/myapp on Linux, etc.
Path cacheDir = PathsEx.cacheDir("myapp");    // ~/.cache/myapp on Linux, etc.

// Path utilities
Path combined = PathsEx.join("home", "user", "file.txt");
Path resolved = PathsEx.resolve(base, "subdir", "file.txt");
Path normalized = PathsEx.normalize(path);
Path ensured = PathsEx.ensureDir(dirPath);    // Creates directory if it doesn't exist
```

#### Read
Simple file reading utilities:
```java
Read reader = new Read();
String content = reader.read("/path/to/file.txt");
```

#### Write
Simple file writing utilities:
```java
Write writer = new Write();
writer.write("/path/to/file.txt", "file content");
```

### 🌐 Networking (`site.meowcat.networking.capture`)

#### NetworkScanner
Discover devices on the local network:
```kotlin
// Start background scanning
NetworkScanner.startScanning()

// Check if initial scan is complete
if (NetworkScanner.isInitialScanComplete) {
    // Scan complete
}
```

Features:
- Automatic discovery of local network devices
- Periodic scanning every 60 seconds
- Threaded execution with connection pool

#### NetworkGraph
Manage network topology and device statistics:
```kotlin
// Add nodes (devices) to the graph
NetworkGraph.addNode("192.168.1.100")

// Add flows (connections) between nodes
NetworkGraph.addFlow("192.168.1.100", "192.168.1.1", "GET /index.html")

// Query information
val displayName = NetworkGraph.getDisplayName("192.168.1.100")
val isLocal = NetworkGraph.isLocalNode("192.168.1.50")
val gateway = NetworkGraph.getGateway()
val subnets = NetworkGraph.getLocalSubnets()

// Access nodes and edges
val nodes: Set<String> = NetworkGraph.nodes
val edges: Map<Pair<String, String>, Edge> = NetworkGraph.edges
```

**DeviceStats** - Track statistics for network devices:
```kotlin
data class DeviceStats(
    val ip: String,
    var sentBytes: Long = 0,
    var recvBytes: Long = 0,
    var sentPackets: Long = 0,
    var recvPackets: Long = 0
)
```

### 🎨 UI Components (`site.meowcat.ui.swing`)

#### AppWindow
Fluent API wrapper for JFrame with method chaining:
```java
JFrame frame = new JFrame();
AppWindow.wrap(frame)
    .title("My Application")
    .size(800, 600)
    .center()
    .layout(new BorderLayout())
    .add(new JPanel())
    .show();

// Also supports
.hide()
.raw()  // Get the underlying JFrame
```


#### WindowKit
Additional UI utilities and helpers (see examples in the codebase).

## Requirements

- **Java**: 17 or higher
- **Maven**: 3.6+
- **Kotlin**: 2.0.21 (included as dependency)

## Building

### Build the project:
```bash
mvn clean install
```

### Build a fat JAR:
```bash
mvn clean package
```

This creates:
- `target/libmeowcat-1.0.jar` - Regular JAR
- `target/libmeowcat-1.0-jar-with-dependencies.jar` - Shaded JAR with dependencies

## Installation

Add to your Maven `pom.xml`:
```xml
<dependency>
    <groupId>site.meowcat</groupId>
    <artifactId>libmeowcat</artifactId>
    <version>1.0</version>
</dependency>
```

Or download the JAR from the [JitPack](https://jitpack.io/#meowcat767/libmeowcat/-SNAPSHOT).


## Usage Examples

### Detect platform and get app config directory
```java
import site.meowcat.system.Platform;
import site.meowcat.system.files.PathsEx;
import java.nio.file.Path;

if (Platform.isWindows()) {
    System.out.println("Running on Windows " + Platform.getVersion());
} else if (Platform.isLinux()) {
    System.out.println("Running on Linux");
}

Path configPath = PathsEx.configDir("myapp");
PathsEx.ensureDir(configPath);
```

### Read and write files
```java
import site.meowcat.system.files.Read;
import site.meowcat.system.files.Write;

Read reader = new Read();
String content = reader.read("input.txt");

Write writer = new Write();
writer.write("output.txt", "Modified content");
```

### Create a Swing window
```java
import site.meowcat.ui.swing.AppWindow;
import javax.swing.*;

JFrame frame = new JFrame();
AppWindow.wrap(frame)
    .title("My Application")
    .size(1024, 768)
    .center()
    .show();
```

### Network scanning
```kotlin
import site.meowcat.networking.capture.NetworkScanner
import site.meowcat.model.NetworkGraph

// Start scanning in the background
NetworkScanner.startScanning()

// Wait for initial scan
Thread.sleep(5000)

// Get discovered devices
val devices = NetworkGraph.nodes
for (device in devices) {
    println("Found device: ${NetworkGraph.getDisplayName(device)}")
}
```

## Dependencies

- `org.jetbrains.kotlin:kotlin-stdlib:2.0.21` - Kotlin standard library
- Java 17+ (built-in)
- Swing (built-in with Java)

## License

See LICENSE file (if available) in the repository.

## Author

Created by meowcat767

## Contributing

Contributions are welcome! Please feel free to submit issues or pull requests.

## Related Projects

- **PickleNetworkDebugger (PKN)** - Some networking components are adapted from PKN at https://github.com/meowcat767/PickleNetworkDebugger

## Notes

- Network scanning uses `avahi-resolve-address` for hostname resolution (Linux/Unix with avahi-daemon)
- Fallback to standard Java DNS resolution if avahi is not available
- NetworkScanner periodically scans every 60 seconds
- UI components use Swing and are designed for desktop applications

