package site.meowcat.networking.capture

/**
 * This code is from PKN @ https://git.meowcat.site/meowcat/PickleNetworkDebugger/src/branch/master/src/main/kotlin/site/meowcat/pkn/capture/NetworkScanner.kt
 * @author meowcat767
 */
import java.net.InetAddress
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread
import site.meowcat.model.NetworkGraph

/**
 * Fetches the default gateway IP address from the system routing table.
 * @return the gateway IP address, or null if it cannot be determined
 */
fun getGateway(): String? {
    try {
        val process = ProcessBuilder("ip", "route", "show", "default").start()
        val reader = BufferedReader(InputStreamReader(process.inputStream))
        val line = reader.readLine()
        if (line != null && line.startsWith("default via")) {
            return line.split(" ")[2]
        }
    } catch (e: Exception) {
        // Fallback or log
    }
    return null
}

/**
 * Scans the local network to discover active nodes.
 */
object NetworkScanner {
    /** Whether the initial network scan has been completed. */
    var isInitialScanComplete = false
        private set

    /**
     * Starts the background scanning process.
     * Periodically scans all local subnets every minute.
     */
    fun startScanning() {
        thread(isDaemon = true) {
            var firstScan = true
            while (true) {
                val subnets = NetworkGraph.getLocalSubnets()
                for (subnet in subnets) {
                    scanSubnet(subnet)
                }
                if (firstScan) {
                    isInitialScanComplete = true
                    firstScan = false
                }
                Thread.sleep(60000) // Scan every minute
            }
        }
    }

    private fun scanSubnet(subnetCidr: String) {
        val parts = subnetCidr.split("/")
        if (parts.size != 2) return
        val ipStr = parts[0]
        val prefixLen = parts[1].toInt()

        // For simplicity, we only scan /24 or smaller networks actively to avoid huge scans
        // If it's a /16, we might want to be more careful, but let's assume standard LANs
        if (prefixLen < 16) return

        val prefix = ipStr.substringBeforeLast(".")
        val executor = Executors.newFixedThreadPool(20)

        // Scan the /24 range the IP belongs to
        for (i in 1..254) {
            val ip = "$prefix.$i"
            executor.execute {
                try {
                    val address = InetAddress.getByName(ip)
                    if (address.isReachable(500)) { // Shorter timeout
                        NetworkGraph.addNode(ip)
                    }
                } catch (e: Exception) {
                    // Ignore
                }
            }
        }
        executor.shutdown()
        executor.awaitTermination(30, TimeUnit.SECONDS)
    }
}
