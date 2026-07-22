package kpn.api.common

object Bounds {

  def from(latLons: Seq[LatLon], gap: Double = 0): Bounds = {
    if (latLons.isEmpty) {
      Bounds()
    }
    else {
      val lattitudes = latLons.map(_.lat)
      val longitudes = latLons.map(_.lon)
      fromCoordinates(lattitudes, longitudes, gap)
    }
  }

  private def fromCoordinates(lattitudes: Seq[Double], longitudes: Seq[Double], gap: Double = 0): Bounds = {

    val latMin = lattitudes.min
    val lonMin = longitudes.min
    val latMax = lattitudes.max
    val lonMax = longitudes.max

    val latDelta = (latMax - latMin) * gap
    val lonDelta = (lonMax - lonMin) * gap

    Bounds(
      minLat = latMin - latDelta,
      minLon = lonMin - lonDelta,
      maxLat = latMax + latDelta,
      maxLon = lonMax + lonDelta
    )
  }

  def merge(boundsCollection: Seq[Bounds]): Bounds = {
    if (boundsCollection.isEmpty) {
      Bounds()
    }
    else {
      val minLat = boundsCollection.map(_.minLat).min
      val maxLat = boundsCollection.map(_.maxLat).max
      val minLon = boundsCollection.map(_.minLon).min
      val maxLon = boundsCollection.map(_.maxLon).max
      Bounds(
        minLat,
        minLon,
        maxLat,
        maxLon
      )
    }
  }

}

case class Bounds(
  minLat: Double = 0,
  minLon: Double = 0,
  maxLat: Double = 0,
  maxLon: Double = 0
)
