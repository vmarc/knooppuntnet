package kpn.server.analyzer.engine.analysis.location

import java.io.File

import kpn.api.custom.Country

object LocationConfigurationDefinition {

  val DIR = "/kpn/conf/locations"

  val excludedLocations = Seq(
    "nl/4/Caribbean Netherlands",
    "fr/4/Corsica",
    "es/4/Balearic Islands",
    "es/4/Canary Islands",
    "es/4/Melilla",
    "es/4/Plazas de Soberanía",
    "es/4/Ceuta",
  )

  val skippedLocations = Seq(
    "be/7/Brussels-Capital",
    "be/7/Nivelles",
  )

  def treeFile: File = new File(LocationConfigurationDefinition.DIR + "/tree-new.json")

  val nl: LocationConfigurationDefinition = LocationConfigurationDefinition(Country.nl, "nl/3/Netherlands", Seq(3, 4, 8))
  val be: LocationConfigurationDefinition = LocationConfigurationDefinition(Country.be, "be/2/Belgium", Seq(2, 4, 6, 7, 8))
  val de: LocationConfigurationDefinition = LocationConfigurationDefinition(Country.de, "de/2/Germany", Seq(2, 4, 5, 6))
  val fr: LocationConfigurationDefinition = LocationConfigurationDefinition(Country.fr, "fr/3/Metropolitan France", Seq(3, 4, 7))
  val at: LocationConfigurationDefinition = LocationConfigurationDefinition(Country.at, "at/2/Austria", Seq(2, 4, 6))
  val es: LocationConfigurationDefinition = LocationConfigurationDefinition(Country.es, "es/2/Spain", Seq(2, 4, 6))

  val countries: Seq[LocationConfigurationDefinition] = Seq(nl, be, de, fr, at, es)
}

case class LocationConfigurationDefinition(country: Country, root: String, levels: Seq[Int])
