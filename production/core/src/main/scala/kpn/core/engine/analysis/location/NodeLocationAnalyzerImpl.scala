package kpn.core.engine.analysis.location

class NodeLocationAnalyzerImpl(locationDefinitions: Seq[LocationDefinition]) {

  println("initiating locators")
  private val locators = locationDefinitions.map(LocationLocator.from)
  println("initiating locators done")

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
