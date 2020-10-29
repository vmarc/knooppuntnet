package kpn.server.analyzer.engine.analysis.location

class LocationConfigurationDeDuplicator {

  private val alternatives: Map[String, String] = Map(
    // The Netherlands
    "nl/3/Netherlands" -> "nl",
    "nl/4/Utrecht" -> "Utrecht provincie",
    "nl/4/Groningen" -> "Groningen provincie",
    // Belgium
    "be/2/Belgium" -> "be",
    "be/7/Charleroi" -> "Charleroi arrondissement",
    "be/7/Waremme" -> "Waremme arrondissement",
    "be/7/Turnhout" -> "Turnhout arrondissement",
    "be/7/Mouscron" -> "Mouscron arrondissement",
    "be/7/Arlon" -> "Arlon arrondissement",
    "be/7/Huy" -> "Huy arrondissement",
    "be/7/Tielt" -> "Tielt arrondissement",
    "be/7/Diksmuide" -> "Diksmuide arrondissement",
    "be/7/Brugge" -> "Brugge arrondissement",
    "be/7/Oudenaarde" -> "Oudenaarde arrondissement",
    "be/7/Tournai" -> "Tournai arrondissement",
    "be/7/Dinant" -> "Dinant arrondissement",
    "be/7/Aalst" -> "Aalst arrondissement",
    "be/7/Thuin" -> "Thuin arrondissement",
    "be/7/Leuven" -> "Leuven arrondissement",
    "be/6/Namur" -> "Namur province",
    "be/7/Namur" -> "Namur arrondissement",
    "be/7/Veurne" -> "Veurne arrondissement",
    "be/7/Philippeville" -> "Philippeville arrondissement",
    "be/7/Mons" -> "Mons arrondissement",
    "be/7/Roeselare" -> "Roeselare arrondissement",
    "be/7/Maaseik" -> "Maaseik arrondissement",
    "be/7/Neufchâteau" -> "Neufchâteau arrondissement",
    // TODO "2322809" -> "Nivelles arrondissement",
    "be/7/Sint-Niklaas" -> "Sint-Niklaas arrondissement",
    "be/6/Liège" -> "Liège province",
    "be/7/Liège" -> "Liège arrondissement",
    "be/7/Tongeren" -> "Tongeren arrondissement",
    "be/7/Mechelen" -> "Mechelen arrondissement",
    "be/7/Kortrijk" -> "Kortrijk arrondissement",
    "be/6/Antwerp" -> "Antwerp province",
    "be/7/Antwerp" -> "Antwerp arrondissement",
    "be/7/Soignies" -> "Soignies arrondissement",
    "be/7/Ath" -> "Ath arrondissement",
    "be/7/Hasselt" -> "Hasselt arrondissement",
    "be/7/Verviers" -> "Verviers arrondissement",
    "be/7/Eeklo" -> "Eeklo arrondissement",
    "be/7/Dendermonde" -> "Dendermonde arrondissement",
    "be/7/Ostend" -> "Ostend arrondissement",
    "be/7/Marche-en-Famenne" -> "Marche-en-Famenne arrondissement",
    "be/7/Bastogne" -> "Bastogne arrondissement",
    "be/7/Virton" -> "Virton arrondissement",
    //  France
    "fr/3/Metropolitan France" -> "fr",
    //  Germany
    "de/2/Germany" -> "de",
    "de/4/Bremen" -> "Bremen Bundesland",
    // TODO "62434" -> "Leipzig Bundesland",
    // Austria
    "at/2/Austria" -> "at",
    "at/4/Salzburg" -> "Salzburg Bundesland",
    // duplicates in different countries
    "be/8/Lens" -> "Lens BE",
    "fr/7/Lens" -> "Lens FR",
    "be/8/Rochefort" -> "Rochefort BE",
    "fr/7/Rochefort" -> "Rochefort FR",
    "be/8/Neufchâteau" -> "Neufchâteau BE",
    "fr/7/Neufchâteau" -> "Neufchâteau FR",
    "nl/8/Zwijndrecht" -> "Zwijndrecht NL",
    "be/8/Zwijndrecht" -> "Zwijndrecht BE",
    "nl/4/Limburg" -> "Limburg NL",
    "be/6/Limburg" -> "Limburg BE",
    "be/8/Lille" -> "Lille BE",
    "fr/7/Lille" -> "Lille FR",
    "be/8/Herne" -> "Herne BE",
    "de/6/Herne" -> "Herne DE",
    "be/8/Essen" -> "Essen BE",
    "de/6/Essen" -> "Essen DE",
    "es/2/Spain" -> "es",
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
