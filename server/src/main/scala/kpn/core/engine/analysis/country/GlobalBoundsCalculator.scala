package kpn.core.engine.analysis.country

import kpn.shared.Bounds
import kpn.shared.Country

object GlobalBoundsCalculator {

  def main(args: Array[String]): Unit = {

    val countryBoundaries = Country.all.map { country =>
      println(country)
      CountryBoundaryReader.read(country)
    }

    val boundss = countryBoundaries.map(_.bounds)

    boundss.foreach(println)

    val latMin = boundss.map(_.minLat).min
    val lonMin = boundss.map(_.minLon).min
    val latMax = boundss.map(_.maxLat).max
    val lonMax = boundss.map(_.maxLon).max

    val latDelta = (latMax - latMin) / 20 // 5%
    val lonDelta = (lonMax - lonMin) / 20 // 5%

    val bounds = Bounds(
      minLat = latMin - latDelta,
      minLon = lonMin - lonDelta,
      maxLat = latMax + latDelta,
      maxLon = lonMax + lonDelta
    )

    println(bounds)
  }
}
