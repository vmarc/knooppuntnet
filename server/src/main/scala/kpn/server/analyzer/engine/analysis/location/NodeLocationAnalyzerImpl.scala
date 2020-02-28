package kpn.server.analyzer.engine.analysis.location

import kpn.api.common.location.Location
import kpn.core.util.Log
import org.springframework.stereotype.Component

@Component
class NodeLocationAnalyzerImpl(locationConfiguration: LocationConfiguration) extends NodeLocationAnalyzer {

  private val log = Log(classOf[NodeLocationAnalyzerImpl])

  log.info("Initiating locators")
  private val locators = locationConfiguration.locations.map(LocationLocator.from)
  log.info("Initiating locators done")

  def locate(latitude: String, longitude: String): Option[Location] = {
    locators.foreach { locators =>
      val locationNames = doLocate(latitude, longitude, Seq(), locators)
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
