import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import kotlin.math.*

typealias Location = Pair<Double, Double>

private const val R_MERCATOR = 6_378_137.0
private const val EARTH_RADIUS_KM = 6371.0

class TraceTests : StringSpec({
    "calculate the distance between the first and last point" {
        parseTracePoints("trace")
            .let { points -> points.first().haversineDistanceTo(points.last()) }
            .shouldBe(16968.724 plusOrMinus 0.001);
    }
})

data class TracePoint(val order: Int, val x: Double, val y: Double) {
    fun haversineDistanceTo(other: TracePoint): Double =
        haversineDistance(this.toWgs84(), other.toWgs84())

    private fun toWgs84(): Location {
        val latitude = (2.0 * atan(exp(this.y / R_MERCATOR)) - Math.PI / 2.0) * 180.0 / Math.PI
        val longitude = (this.x / R_MERCATOR) * 180.0 / Math.PI

        return latitude to longitude
    }
}

private fun String.toTracePoint(): TracePoint =
    this.split(",")
        .run {
            TracePoint(
                this[0].toInt(),
                this[1].toDouble(),
                this[2].toDouble()
            )
        }

private fun parseTracePoints(fileName: String): List<TracePoint> =
    parseLines(fileName)
        .map { it.toTracePoint() }
        .sortedBy { it.order }

private fun parseLines(name: String): List<String> =
    getResource(name)
        .let {
            Files.readString(
                Path.of(Objects.requireNonNull(it).toURI())
            ).split("\r\n")
        }

private fun getResource(name: String): URL = TraceTests::class.java.classLoader.getResource(name)!!

private fun haversineDistance(from: Location, to: Location): Double {
    fun degreesToRadians(degrees: Double): Double = degrees * Math.PI / 180.0

    val (lat1, lon1) = from.let { degreesToRadians(it.first) to degreesToRadians(it.second) }
    val (lat2, lon2) = to.let { degreesToRadians(it.first) to degreesToRadians(it.second) }

    val deltaLat = lat2 - lat1
    val deltaLon = lon2 - lon1

    val a = sin(deltaLat / 2).pow(2) + cos(lat1) * cos(lat2) * sin(deltaLon / 2).pow(2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))

    return EARTH_RADIUS_KM * c
}