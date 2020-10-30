package kpn.core.tools.location

import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.location.LocationConfigurationDefinition
import kpn.server.analyzer.engine.analysis.location.LocationDefinition

import scala.annotation.tailrec

class LocationTreeBuilder(
  configuration: LocationConfigurationDefinition,
  locations: Seq[LocationDefinition]
) {

  private var locationMap = locations.map(l => l.id -> l).toMap
  private val log = Log(classOf[LocationTreeBuilder])

  def countryLocation(): Option[LocationDefinition] = {
    configuration.levels.tail.zipWithIndex.reverse.foreach { case (level, index) =>
      levelLocations(level).foreach { childLocation =>
        if (index >= 0) {
          findParent(index, childLocation) match {
            case None => log.error("Could not find parent location for " + childLocation.id)
            case Some(parentLocation) =>
              log.debug(s""""${childLocation.id}" added to "${parentLocation.id}""")
              val updatedChildren = (parentLocation.children :+ childLocation).sortBy(_.name)
              val updatedParentLocation = parentLocation.copy(children = updatedChildren)
              locationMap = locationMap.updated(updatedParentLocation.id, updatedParentLocation)
          }
        }
      }
    }
    locationMap.get(configuration.root)
  }

  private def levelLocations(level: Int): Iterable[LocationDefinition] = {
    locationMap.values.
      filterNot(location => LocationConfigurationDefinition.excludedLocations.contains(location.id)).
      filter(_.level == level)
  }

  @tailrec
  private def findParent(index: Int, childLocation: LocationDefinition): Option[LocationDefinition] = {
    val parentLevel = configuration.levels(index)
    val candidateParentLocations = locationMap.values.filter(_.level == parentLevel)
    candidateParentLocations.find(location => contains(location, childLocation)) match {
      case Some(parentLocation) => Some(parentLocation)
      case None => findParent(index - 1, childLocation)
    }
  }

  private def contains(parentLocation: LocationDefinition, childLocation: LocationDefinition): Boolean = {
    val intersection = parentLocation.geometry.intersection(childLocation.geometry)
    val intersectionArea = intersection.getArea
    val childLocationArea = childLocation.geometry.getArea
    Math.abs(intersectionArea / childLocationArea) > 0.95
  }
}
