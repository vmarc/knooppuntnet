package kpn.server.analyzer.engine.analysis.location

import java.io.File

import kpn.api.custom.Country

object LocationConfigurationDefinition {

  val DIR = "/kpn/conf/locations/"
  val EXTENSION = ".GeoJson"

  def file(name: String): File = new File(filename(name))
  def filename(name: String): String = s"$DIR$name$EXTENSION"
  def treeFile: File = new File(LocationConfigurationDefinition.DIR + "tree.json")

  val nl: LocationConfigurationDefinition = LocationConfigurationDefinition(Country.nl, Seq(3, 4, 8))
  val be: LocationConfigurationDefinition = LocationConfigurationDefinition(Country.be, Seq(2, 4, 6, 7, 8))
  val de: LocationConfigurationDefinition = LocationConfigurationDefinition(Country.de, Seq(2, 4, 5, 6))
  val fr: LocationConfigurationDefinition = LocationConfigurationDefinition(Country.fr, Seq(3, 4, 7))
  val at: LocationConfigurationDefinition = LocationConfigurationDefinition(Country.at, Seq(2, 4, 6))

  val countries: Seq[LocationConfigurationDefinition] = Seq(nl, be, de, fr, at)
}

case class LocationConfigurationDefinition(country: Country, levels: Seq[Int])
