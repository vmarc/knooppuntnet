package kpn.server.api.analysis.pages.location

@Component
class LocationMapPageBuilderImpl(
  locationRepository: LocationRepository,
  locationConfiguration: LocationConfiguration
) extends LocationMapPageBuilder {

  override def build(locationKey: LocationKey): Option[LocationMapPage] = {
    if (locationKey == LocationKey(NetworkType.cycling, Country.nl, "example")) {
      Some(LocationMapPageExample.page)
    }
    else {
      buildPage(locationKey)
    }
  }

  private def buildPage(locationKey: LocationKey): Option[LocationMapPage] = {
    val summary = locationRepository.summary(locationKey)
    LocationDefinition.find(locationConfiguration.locations, locationKey.name) match {
      case Some(locationDefinition) =>
        val bounds = Bounds(
          minLat = locationDefinition.boundingBox.getMinY,
          minLon = locationDefinition.boundingBox.getMinX,
          maxLat = locationDefinition.boundingBox.getMaxY,
          maxLon = locationDefinition.boundingBox.getMaxX
        )
        val geoWriter = new GeoJsonWriter()
        val geoJson = geoWriter.write(locationDefinition.geometry)
        Some(
          LocationMapPage(
            summary,
            bounds,
            geoJson
          )
        )

      case None =>
        Some(
          LocationMapPage(
            summary,
            Bounds(),
            ""
          )
        )
    }
  }
}
