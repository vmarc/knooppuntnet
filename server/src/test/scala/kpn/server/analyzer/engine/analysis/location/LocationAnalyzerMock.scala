package kpn.server.analyzer.engine.analysis.location

import kpn.api.common.LatLon
import kpn.api.custom.Country
import org.locationtech.jts.geom.Geometry

class LocationAnalyzerMock extends LocationAnalyzerAbstract {

  override def countries(latLon: LatLon): Seq[Country] = {
    if (latLon.latitude == "" || latLon.latitude == "0") {
      Seq(Country.nl)
    }
    else {
      Seq.empty
    }
  }

  override def country(latLons: Iterable[LatLon]): Option[Country] = {
    doCountry(latLons)
  }

  override def findLocations(latitude: String, longitude: String): Seq[String] = {
    Seq.empty
  }

  override def locateGeometries(geometries: Seq[Geometry]): Seq[LocationSelector] = {
    Seq.empty
  }
}
