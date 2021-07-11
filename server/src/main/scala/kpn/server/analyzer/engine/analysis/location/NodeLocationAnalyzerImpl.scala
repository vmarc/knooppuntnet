package kpn.server.analyzer.engine.analysis.location

import kpn.api.common.location.Location
import kpn.core.util.Log
import org.springframework.stereotype.Component

@Component
class NodeLocationAnalyzerImpl(
  locationConfiguration: LocationConfiguration,
  analyzerEnabled: Boolean
) extends NodeLocationAnalyzer {

  private val log = Log(classOf[NodeLocationAnalyzerImpl])

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

  def locate(latitude: String, longitude: String): Option[Location] = {
    locators.foreach { locators =>
      val locationNames = doLocate(latitude, longitude, Seq.empty, locators)
      if (locationNames.isDefined) {
        return Some(Location(locationNames.get))
      }
    }
    None
  }

  private def doLocate(latitude: String, longitude: String, names: Seq[String], locator: LocationLocator): Option[Seq[String]] = {

    if (locator.contains(latitude, longitude)) {
      val newNames = names :+ locator.locationDefinition.name

      if (locator.locationDefinition.children.isEmpty) {
        Some(newNames)
      }
      else {
        locator.children.foreach { child =>
          val locationNames = doLocate(latitude, longitude, newNames, child)
          if (locationNames.isDefined) {
            return locationNames
          }
        }
        None
      }
    }
    else {
      None
    }
  }
}
