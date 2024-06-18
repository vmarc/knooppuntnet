package kpn.core.poi.configuration

object PoiGroupTourism {

  val builder: PoiGroupBuilder = new PoiGroupBuilder("tourism", true) {

    poi("arts-centre", "letter_a.png", 13, 13,
      Seq("amenity" -> "arts_centre"),
      tagable => tagable.hasTag("amenity", "arts_centre")
    )

    poi("artwork", "artwork.png", 13, 13,
      Seq("tourism" -> "artwork"),
      tagable => tagable.hasTag("tourism", "artwork")
        && !tagable.hasTag("artwork_type", "statue")
    )

    poi("casino", "casino.png", 13, 13,
      Seq("leisure" -> "casino", "amenity" -> "casino"),
      tagable => tagable.hasTag("leisure", "casino")
        || tagable.hasTag("amenity", "casino")
    )

    poi("gallery", "artgallery.png", 13, 13,
      Seq("tourism" -> "gallery"),
      tagable => tagable.hasTag("tourism", "gallery")
    )

    poi("monumental-tree", "tree.png", 13, 13,
      Seq("natural" -> "tree"),
      tagable => tagable.hasTag("natural", "tree")
        && tagable.hasTag("monument", "yes")
    )

    poi("museum", "museum_art.png", 13, 13,
      Seq("tourism" -> "museum"),
      tagable => tagable.hasTag("tourism", "museum")
    )

    poi("vineyard", "vineyard.png", 13, 13,
      Seq("landuse" -> "vineyard"),
      tagable => tagable.hasTag("landuse", "vineyard")
    )

    // TODO review instances of this poi
    poi("tourism", "sight-2.png", 13, 13,
      Seq("tourism" -> "yes"),
      tagable => tagable.hasTag("tourism", "yes")
    )
  }
}
