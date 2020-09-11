package kpn.core.poi.configuration

object PoiGroupTourism {

  val builder: PoiGroupBuilder = new PoiGroupBuilder("tourism", true) {

    poi("arts-centre", "letter_a.png", 13, 13,
      Seq("amenity" -> "arts_centre"),
      tags => tags.has("amenity", "arts_centre")
    )

    poi("artwork", "artwork.png", 13, 13,
      Seq("tourism" -> "artwork"),
      tags => tags.has("tourism", "artwork")
        && !tags.has("artwork_type", "statue")
    )

    poi("casino", "casino.png", 13, 13,
      Seq("leisure" -> "casino", "amenity" -> "casino"),
      tags => tags.has("leisure", "casino")
        || tags.has("amenity", "casino")
    )

    poi("gallery", "artgallery.png", 13, 13,
      Seq("tourism" -> "gallery"),
      tags => tags.has("tourism", "gallery")
    )

    poi("monumental-tree", "tree.png", 13, 13,
      Seq("natural" -> "tree"),
      tags => tags.has("natural", "tree")
        && tags.has("monument", "yes")
    )

    poi("museum", "museum_art.png", 13, 13,
      Seq("tourism" -> "museum"),
      tags => tags.has("tourism", "museum")
    )

    poi("vineyard", "vineyard.png", 13, 13,
      Seq("landuse" -> "vineyard"),
      tags => tags.has("landuse", "vineyard")
    )

    // TODO review instances of this poi
    poi("tourism", "sight-2.png", 13, 13,
      Seq("tourism" -> "yes"),
      tags => tags.has("tourism", "yes")
    )
  }
}
