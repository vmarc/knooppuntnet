package kpn.server.analyzer.engine.analysis.location

import kpn.api.common.location.Location
import kpn.core.util.Log
import org.springframework.stereotype.Component

@Component
class OldNodeLocationAnalyzerImpl(
  locationConfiguration: LocationConfiguration,
  analyzerEnabled: Boolean
) extends OldNodeLocationAnalyzer {

  private val log = Log(classOf[OldNodeLocationAnalyzerImpl])

  private val locators: Seq[LocationLocator] = if (analyzerEnabled) {
    log.info("Initiating locators")
    try {
      locationConfiguration.locations.map(LocationLocator.from)
    }
    finally {
      log.info("Initiating locators done")
    }
  }
  else {
    Seq.empty
  }

  def locations(latitude: String, longitude: String): Seq[String] = {
    locators.foreach { locators =>
      val locationNames = doLocate(latitude, longitude, Seq.empty, locators)
      if (locationNames.nonEmpty) {
        return locationNames
      }
    }
    Seq.empty
  }

  def oldLocate(latitude: String, longitude: String): Option[Location] = {
    val locationNames = locations(latitude, longitude)
    if (locationNames.nonEmpty) {
      Some(Location(locationNames))
    }
    else {
      None
    }
  }

  private def doLocate(latitude: String, longitude: String, names: Seq[String], locator: LocationLocator): Seq[String] = {

    if (locator.contains(latitude, longitude)) {
      val newNames = names :+ locator.locationDefinition.name

      if (locator.locationDefinition.children.isEmpty) {
        newNames
      }
      else {
        locator.children.foreach { child =>
          val locationNames = doLocate(latitude, longitude, newNames, child)
          if (locationNames.nonEmpty) {
            return locationNames
          }
        }
        Seq.empty
      }
    }
    else {
      Seq.empty
    }
  }
}
