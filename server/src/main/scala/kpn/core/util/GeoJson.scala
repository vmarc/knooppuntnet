package kpn.core.util

trait GeoJsonGeometry

case class GeoJsonFeatureCollection(`type`: String, features: Array[GeoJsonFeature])

case class GeoJsonFeature(`type`: String, geometry: GeoJsonGeometry, properties: GeoJsonProperties)

case class GeoJsonPointGeometry(`type`: String, coordinates: Array[Double]) extends GeoJsonGeometry

case class GeoJsonLineStringGeometry(`type`: String, coordinates: Array[Array[Double]]) extends GeoJsonGeometry

case class GeoJsonProperties(
  id: Option[String] = None,
  name: Option[String] = None,
  begin_geoid: Option[String] = None,
  end_geoid: Option[String] = None,
  distance: Option[String] = None,
  stroke: Option[String] = None,
)

object GeoJson {

  def featureCollection(features: Seq[GeoJsonFeature]): GeoJsonFeatureCollection = {
    GeoJsonFeatureCollection("FeatureCollection", features.toArray)
  }

  def feature(geometry: GeoJsonGeometry, properties: GeoJsonProperties): GeoJsonFeature = {
    GeoJsonFeature("Feature", geometry, properties)
  }

  def pointGeometry(lattitude: String, longitude: String): GeoJsonPointGeometry = {
    GeoJsonPointGeometry(
      "Point",
      Array(
        lattitude.toDouble,
        longitude.toDouble
      )
    )
  }

  def lineStringGeometry(coordinates: Seq[Seq[String]]): GeoJsonLineStringGeometry = {
    val ccc = coordinates.map(c => c.map(_.toDouble).toArray).toArray
    GeoJsonLineStringGeometry(
      "LineString",
      ccc
    )
  }

  def properties(
    id: Option[String] = None,
    name: Option[String] = None,
    begin_geoid: Option[String] = None,
    end_geoid: Option[String] = None,
    distance: Option[String] = None
  ): GeoJsonProperties = {
    GeoJsonProperties(
      id,
      name,
      begin_geoid,
      end_geoid,
      distance
    )
  }

}
