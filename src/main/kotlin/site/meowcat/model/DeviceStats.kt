package site.meowcat.model

/**
 * This code is from PKN @ https://git.meowcat.site/meowcat/PickleNetworkDebugger/src/branch/master/src/main/kotlin/site/meowcat/pkn/model/DeviceStats.kt
 * @author meowcat767
 */
data class DeviceStats(
    val ip: String,
    var sentBytes: Long = 0,
    var recvBytes: Long = 0,
    var sentPackets: Long = 0,
    var recvPackets: Long = 0
)
