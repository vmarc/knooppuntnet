package kpn.server.analyzer.engine.analysis.location

import kpn.api.common.location.Location

case class LocationSelector(locationDefinitions: Seq[LocationStoreData]) {

  def name: String = locationDefinitions.map(_.name).mkString(" > ")

  def toLocation: Location = Location(locationDefinitions.map(_.id))

  def leaf: LocationStoreData = locationDefinitions.last
}
