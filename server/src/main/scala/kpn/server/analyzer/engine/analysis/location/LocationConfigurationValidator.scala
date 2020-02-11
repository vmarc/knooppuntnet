package kpn.server.analyzer.engine.analysis.location

class LocationConfigurationValidator {

  case class LocationInfo(level: Int, location: LocationDefinition)

  def validate(configuration: LocationConfiguration): Unit = {

    val duplicates = configuration.locations.flatMap { location =>
      val locationsByName = scala.collection.mutable.Map[String, Seq[LocationInfo]]()
      validateLocation(locationsByName, 0, location)
      locationsByName.filter { case (name, xs) => xs.size > 1 }
    }

    if (duplicates.nonEmpty) {
      val lines = duplicates.flatMap { case (name, xs) =>
        Seq("  " + name) ++ xs.map { locationInfo => "      level=" + locationInfo.level + ", id=" + locationInfo.location.id }
      }
      val message = lines.mkString("\n")
      throw new RuntimeException("Duplicate location names\n" + message)
    }
  }

  private def validateLocation(
    locationsByName: scala.collection.mutable.Map[String, Seq[LocationInfo]],
    level: Int,
    location: LocationDefinition
  ): Unit = {
    val definitions = locationsByName.getOrElse(location.name, Seq.empty)
    val updated = definitions :+ LocationInfo(level, location)
    locationsByName.put(location.name, updated)
    location.children.foreach { child =>
      validateLocation(locationsByName, level + 1, child)
    }
  }

}
