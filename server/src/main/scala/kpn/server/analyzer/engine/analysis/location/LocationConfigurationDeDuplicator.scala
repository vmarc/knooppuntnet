package kpn.server.analyzer.engine.analysis.location

class LocationConfigurationDeDuplicator {

  private val alternatives: Map[String, String] = Map(
    // The Netherlands
    "47796" -> "nl",
    "47667" -> "Utrecht provincie",
    "47826" -> "Groningen provincie",
    // Belgium
    "52411" -> "be",
    "2336710" -> "Charleroi arrondissement",
    "1407190" -> "Waremme arrondissement",
    "2524404" -> "Turnhout arrondissement",
    "2198351" -> "Mouscron arrondissement",
    "1412580" -> "Arlon arrondissement",
    "1407189" -> "Huy arrondissement",
    "2239751" -> "Tielt arrondissement",
    "2239583" -> "Diksmuide arrondissement",
    "2239546" -> "Brugge arrondissement",
    "2524068" -> "Oudenaarde arrondissement",
    "2198998" -> "Tournai arrondissement",
    "1405412" -> "Dinant arrondissement",
    "2523964" -> "Aalst arrondissement",
    "2198768" -> "Thuin arrondissement",
    "2524256" -> "Leuven arrondissement",
    "1311816" -> "Namur province",
    "1405410" -> "Namur arrondissement",
    "2239635" -> "Veurne arrondissement",
    "1405411" -> "Philippeville arrondissement",
    "2372934" -> "Mons arrondissement",
    "2239123" -> "Roeselare arrondissement",
    "2413054" -> "Maaseik arrondissement",
    "2336443" -> "Neufchâteau arrondissement",
    "2322809" -> "Nivelles arrondissement",
    "2524074" -> "Sint-Niklaas arrondissement",
    "1407192" -> "Liège province",
    "1407191" -> "Liège arrondissement",
    "2377237" -> "Tongeren arrondissement",
    "2524403" -> "Mechelen arrondissement",
    "2239750" -> "Kortrijk arrondissement",
    "53114" -> "Antwerp province",
    "1902793" -> "Antwerp arrondissement",
    "2372935" -> "Soignies arrondissement",
    "2372933" -> "Ath arrondissement",
    "2412218" -> "Hasselt arrondissement",
    "1407211" -> "Verviers arrondissement",
    "2523965" -> "Eeklo arrondissement",
    "2523970" -> "Dendermonde arrondissement",
    "2239593" -> "Ostend arrondissement",
    "1412582" -> "Marche-en-Famenne arrondissement",
    "1412583" -> "Bastogne arrondissement",
    "2336444" -> "Virton arrondissement",
    //  France
    "1403916" -> "fr",
    //  Germany
    "51477" -> "de",
    "62718" -> "Bremen Bundesland",
    "62434" -> "Leipzig Bundesland",
    // Austria
    "16239" -> "at",
    "86539" -> "Salzburg Bundesland"
  )

  def deduplicate(configuration: LocationConfiguration): LocationConfiguration = {
    LocationConfiguration(
      configuration.locations.map { locationDefinition =>
        deDuplicateLocationDefinition(locationDefinition)
      }
    )
  }

  private def deDuplicateLocationDefinition(locationDefinition: LocationDefinition): LocationDefinition = {
    val name = alternatives.getOrElse(locationDefinition.id, locationDefinition.name)
    val children = locationDefinition.children.map(deDuplicateLocationDefinition)
    locationDefinition.copy(
      name = name,
      children = children
    )
  }
}
