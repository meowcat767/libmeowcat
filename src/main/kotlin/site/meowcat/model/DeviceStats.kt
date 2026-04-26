package site.meowcat.model

/**
 * This code is from PKN @ https://git.meowcat.site/meowcat/PickleNetworkDebugger/src/branch/master/src/main/kotlin/site/meowcat/pkn/model/DeviceStats.kt
 * @author meowcat767
 */
/**
 * Statistics for a network device.
 * @property ip the IP address of the device
 * @property sentBytes total bytes sent by this device
 * @property recvBytes total bytes received by this device
 * @property sentPackets total packets sent by this device
 * @property recvPackets total packets received by this device
 */
data class DeviceStats(
    val ip: String,
    var sentBytes: Long = 0,
    var recvBytes: Long = 0,
    var sentPackets: Long = 0,
    var recvPackets: Long = 0
)
