package kpn.server.analyzer.engine.analysis.node.analyzers

import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.location.LocationConfiguration
import kpn.server.analyzer.engine.analysis.location.LocationLocator
import kpn.server.analyzer.engine.analysis.node.domain.NodeAnalysis
import org.springframework.stereotype.Component

@Component
class NodeLocationsAnalyzerImpl(
  locationConfiguration: LocationConfiguration,
  analyzerEnabled: Boolean
) extends NodeLocationsAnalyzer {
  private val log = Log(classOf[NodeLocationsAnalyzerImpl])

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

  override def analyze(analysis: NodeAnalysis): NodeAnalysis = {
    val locations = findLocations(analysis.node.latitude, analysis.node.longitude)
    analysis.copy(locations = locations)
  }

  private def findLocations(latitude: String, longitude: String): Seq[String] = {
    locators.foreach { locators =>
      val locationNames = doLocate(latitude, longitude, Seq.empty, locators)
      if (locationNames.nonEmpty) {
        return locationNames
      }
    }
    Seq.empty
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
