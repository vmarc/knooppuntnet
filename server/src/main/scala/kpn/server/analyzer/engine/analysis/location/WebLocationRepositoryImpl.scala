package kpn.server.analyzer.engine.analysis.location

class WebLocationRepositoryImpl extends WebLocationRepository {

  def locationGeojson(locationId: String): Option[String] = {

    val country = locationId.substring(0, 2)

    // lees
    val filename = s"/kpn/locations/$country/geometries/$locationId-minimized.geojson"

    // indien niet gevonden, less:
    val filename2 = s"/kpn/locations/$country/geometries/$locationId.geojson"

    None
  }
}
