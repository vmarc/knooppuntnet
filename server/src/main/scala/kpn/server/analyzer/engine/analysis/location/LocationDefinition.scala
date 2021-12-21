package kpn.server.analyzer.engine.analysis.location

import kpn.api.common.Language

object LocationDefinition {

  def find(locationDefinition: LocationDefinition, name: String): Option[LocationDefinition] = {
    if (locationDefinition.name == name) {
      Some(locationDefinition)
    }
    else {
      find(locationDefinition.children, name)
    }
  }

  def find(locationDefinitions: Seq[LocationDefinition], name: String): Option[LocationDefinition] = {
    locationDefinitions.foreach { child =>
      find(child, name) match {
        case Some(ld) => return Some(ld)
        case None =>
      }
    }
    None
  }
}

case class LocationDefinition(
  id: String,
  name: String,
  locationNames: Map[Language, String],
  children: Seq[LocationDefinition] = Seq.empty
) {

  def name(language: Language): String = {
    locationNames.getOrElse(language, name)
  }

  def names: Seq[String] = {
    locationNames.keys.map(key => key.toString + "=" + locationNames(key)).toSeq
  }

  def allChilderen(): Seq[LocationDefinition] = {
    children ++ children.flatMap(_.allChilderen())
  }
}
