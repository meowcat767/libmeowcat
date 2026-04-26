package site.meowcat.model

import java.net.InetAddress
import java.net.Inet4Address
import java.net.InterfaceAddress
import java.net.NetworkInterface
import java.util.concurrent.CompletableFuture
import site.meowcat.networking.capture.getGateway

/**
 * This code is from PKN @ https://git.meowcat.site/meowcat/PickleNetworkDebugger/src/branch/master/src/main/kotlin/site/meowcat/pkn/model/Edge.kt
 * @author meowcat767
 */

/**
 * Represents a flow between two network nodes.
 * @property src Source IP address or "Internet"
 * @property dst Destination IP address or "Internet"
 * @property weight Number of packets in this flow
 * @property lastRequest Optional information about the last request (e.g., HTTP path)
 * @property lastPacketTime Timestamp of the last packet seen in this flow
 */
data class Edge(val src: String, val dst: String, var weight: Int = 0, var lastRequest: String? = null, var lastPacketTime: Long = 0)

/**
 * Manages the network graph, including nodes and edges.
 */
object NetworkGraph {
    /** Whether to show nodes outside the local network. */
    var showExternalNodes = false
    /** Set of discovered node IP addresses. */
    val nodes = mutableSetOf<String>()
    /** Map of (source, destination) pairs to Edge objects. */
    val edges = mutableMapOf<Pair<String, String>, Edge>()
    private val hostNames = mutableMapOf<String, String>()
    private val localSubnets = mutableListOf<InterfaceAddress>()
    private val localCache = mutableMapOf<String, Boolean>()
    private var cachedGateway: String? = null

    /**
     * Checks if an IP address is in a private range.
     * @param ip the IP address string
     * @return true if private, false otherwise
     */
    fun isPrivateIp(ip: String): Boolean {
        return ip.startsWith("10.") ||
                ip.startsWith("192.168.") ||
                ip.matches(Regex("""172\.(1[6-9]|2\d|3[0-1])\..*""")) ||
                ip == "127.0.0.1"
    }

    init {
        cachedGateway = getGateway()
        try {
            val interfaces = NetworkInterface.getNetworkInterfaces()
            while (interfaces.hasMoreElements()) {
                val nif = interfaces.nextElement()
                // Skip loopback, down, and common virtual/VPN interfaces
                if (nif.isLoopback || !nif.isUp) continue

                val name = nif.name.lowercase()
                if (name.contains("docker") || name.contains("tailscale") ||
                    name.contains("veth") || name.contains("tun")) continue

                for (addr: InterfaceAddress in nif.interfaceAddresses) {
                    val ip: InetAddress = addr.address
                    if (ip is Inet4Address) {
                        localSubnets.add(addr)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun isLocal(ipStr: String): Boolean {
        if (ipStr == "127.0.0.1") return true
        if (isPrivateIp(ipStr)) return true
        val target: ByteArray = try {
            InetAddress.getByName(ipStr).address
        } catch (e: Exception) {
            return false
        }

        for (localAddr: InterfaceAddress in localSubnets) {
            val localIp: ByteArray = localAddr.address.address
            val maskLen: Int = localAddr.networkPrefixLength.toInt()
            if (isInSubnet(target, localIp, maskLen)) return true
        }
        return false
    }

    private fun isInSubnet(target: ByteArray, local: ByteArray, maskLen: Int): Boolean {
        if (target.size != local.size) return false
        val bytes = maskLen / 8
        val bits = maskLen % 8

        for (i in 0 until bytes) {
            if (target[i] != local[i]) return false
        }

        if (bits > 0) {
            val mask = (0xFF shl (8 - bits)).toByte()
            if ((target[bytes].toInt() and mask.toInt()) != (local[bytes].toInt() and mask.toInt())) return false
        }

        return true
    }

    /**
     * Adds a node to the graph if it is local.
     * @param ip the IP address of the node
     */
    @kotlin.jvm.Synchronized
    fun addNode(ip: String) {
        if (!isLocal(ip)) return
        if (nodes.add(ip)) {
            resolveHostName(ip)
        }
    }

    /**
     * Gets the default gateway IP address.
     * @return the gateway IP, or null if not found
     */
    @kotlin.jvm.Synchronized
    fun getGateway(): String? {
        if (cachedGateway == null) {
            cachedGateway = site.meowcat.networking.capture.getGateway()
        }
        return cachedGateway
    }

    /**
     * Adds a flow between two IP addresses.
     * @param src source IP address
     * @param dst destination IP address
     * @param requestInfo optional request metadata
     */
    @kotlin.jvm.Synchronized
    fun addFlow(src: String, dst: String, requestInfo: String? = null) {
        val srcLocal = isLocalNode(src)
        val dstLocal = isLocalNode(dst)

        if (!srcLocal && !dstLocal) return

        if (srcLocal && nodes.add(src)) resolveHostName(src)
        if (dstLocal && nodes.add(dst)) resolveHostName(dst)

        val router = getGateway()

        val effectiveSrc = if (srcLocal) {
            src
        } else if (showExternalNodes) {
            if (nodes.add(src)) resolveHostName(src)
            src
        } else {
            "Internet"
        }

        val effectiveDst = if (dstLocal) {
            dst
        } else if (showExternalNodes) {
            if (nodes.add(dst)) resolveHostName(dst)
            dst
        } else {
            "Internet"
        }

        if (effectiveSrc == "Internet") nodes.add("Internet")
        if (effectiveDst == "Internet") nodes.add("Internet")

        if (effectiveSrc == effectiveDst) return

        val key = effectiveSrc to effectiveDst
        val edge = edges.getOrPut(key) { Edge(effectiveSrc, effectiveDst, 0) }
        edge.weight++
        edge.lastPacketTime = System.currentTimeMillis()
        if (requestInfo != null) {
            edge.lastRequest = requestInfo
        }
    }

    /**
     * Gets a list of local subnets in CIDR notation.
     * @return list of subnet strings (e.g., "192.168.1.0/24")
     */
    fun getLocalSubnets(): List<String> {
        return localSubnets.map { addr: InterfaceAddress ->
            val ip: String = addr.address.hostAddress
            val prefix: Short = addr.networkPrefixLength
            "$ip/$prefix"
        }
    }

    private fun resolveHostName(ip: String) {
        synchronized(this) {
            if (hostNames.containsKey(ip) && hostNames[ip] != ip) return
            // Set a placeholder to avoid multiple lookups for the same IP
            hostNames[ip] = ip
        }

        CompletableFuture.runAsync {
            // 1. Try avahi-resolve-address
            try {
                val process = ProcessBuilder("avahi-resolve-address", ip).start()
                val reader: java.io.BufferedReader = process.inputStream.bufferedReader()
                val line: String? = reader.readLine()
                process.waitFor(500, java.util.concurrent.TimeUnit.MILLISECONDS)
                if (line != null && line.contains(ip)) {
                    val resolved: String = line.substringAfter(ip).trim().removeSuffix(".local")
                    if (resolved.isNotEmpty() && resolved != ip) {
                        synchronized(this) { hostNames[ip] = resolved }
                        return@runAsync
                    }
                }
            } catch (e: Exception) {}

            // 2. Try avahi-browse
            try {
                val process = ProcessBuilder("avahi-browse", "-t", "-r", "-a").start()
                val reader: java.io.BufferedReader = process.inputStream.bufferedReader()
                var currentAddress: String? = null
                var currentHostname: String? = null

                reader.useLines { lines: Sequence<String> ->
                    for (line in lines) {
                        if (line.contains("address = [")) {
                            currentAddress = line.substringAfter("[").substringBefore("]")
                        }
                        if (line.contains("hostname = [")) {
                            currentHostname = line.substringAfter("[").substringBefore("]").removeSuffix(".local")
                        }
                        if (currentAddress == ip && currentHostname != null) {
                            synchronized(this) {
                                if (hostNames[ip] == ip) {
                                    hostNames[ip] = currentHostname!!
                                }
                            }
                            break
                        }
                    }
                }
                process.destroy()
            } catch (e: Exception) {}

            // 3. Fallback to standard resolution
            try {
                val address: InetAddress = InetAddress.getByName(ip)
                val hostName: String = address.hostName
                val canonicalHostName: String = address.canonicalHostName

                val resolvedName: String? = if (hostName != ip) hostName else if (canonicalHostName != ip) canonicalHostName else null

                if (resolvedName != null) {
                    synchronized(this) {
                        if (hostNames[ip] == ip) {
                            hostNames[ip] = resolvedName
                        }
                    }
                }
            } catch (e: Exception) {}
        }
    }

    /**
     * Checks if an IP address belongs to the local network.
     * @param ip the IP address to check
     * @return true if local, false otherwise
     */
    @kotlin.jvm.Synchronized
    fun isLocalNode(ip: String): Boolean {
        return localCache.getOrPut(ip) { isLocal(ip) }
    }

    /**
     * Gets a display name for an IP, including its resolved hostname if available.
     * @param ip the IP address
     * @return the display name
     */
    @kotlin.jvm.Synchronized
    fun getDisplayName(ip: String): String {
        if (ip == "Internet") return "Internet"
        val hostName: String = hostNames[ip] ?: ip
        return if (hostName == ip) ip else "$hostName ($ip)"
    }

    /**
     * Gets the detected hostname for an IP, or the IP itself if not resolved.
     * @param ip the IP address
     * @return the hostname or IP
     */
    @kotlin.jvm.Synchronized
    fun getDetectedName(ip: String): String {
        if (ip == "Internet") return "Internet"
        return hostNames[ip] ?: ip
    }
}