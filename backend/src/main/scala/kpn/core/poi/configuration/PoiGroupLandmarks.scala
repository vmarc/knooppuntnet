package kpn.core.poi.configuration

object PoiGroupLandmarks {

  val builder: PoiGroupBuilder = new PoiGroupBuilder("landmarks", true) {

    poi("place-of-worship", "church-2.png", 12, 13,
      Seq("amenity" -> "place_of_worship"),
      tagable => tagable.hasTag("amenity", "place_of_worship")
        && !tagable.hasTag("religion", "christian", "muslim", "buddhist", "hindu", "jewish")
    )

    poi("church", "chapel-2.png", 12, 13,
      Seq("amenity" -> "place_of_worship"),
      tagable => tagable.hasTag("amenity", "place_of_worship")
        && tagable.hasTag("religion", "christian")
        && !tagable.hasTag("historic", "wayside_shrine")
    )

    poi("mosque", "mosquee.png", 12, 13,
      Seq("amenity" -> "place_of_worship"),
      tagable => tagable.hasTag("amenity", "place_of_worship")
        && tagable.hasTag("religion", "muslim")
    )

    poi("buddhist-temple", "bouddha.png", 12, 13,
      Seq("amenity" -> "place_of_worship"),
      tagable => tagable.hasTag("amenity", "place_of_worship")
        && tagable.hasTag("religion", "buddhist")
    )

    poi("hindu-temple", "templehindu.png", 12, 13,
      Seq("amenity" -> "place_of_worship"),
      tagable => tagable.hasTag("amenity", "place_of_worship")
        && tagable.hasTag("religion", "hindu")
    )

    poi("synagogue", "synagogue-2.png", 12, 13,
      Seq("amenity" -> "place_of_worship"),
      tagable => tagable.hasTag("amenity", "place_of_worship")
        && tagable.hasTag("religion", "jewish")
    )

    poi("wayside-shrine", "cross-2.png", 15, 15,
      Seq("historic" -> "wayside_shrine"),
      tagable => tagable.hasTag("historic", "wayside_shrine")
    )

    poi("heritage", "worldheritagesite.png", 12, 14,
      Seq("heritage" -> ""),
      tagable => tagable.hasTag("heritage")
    )

    poi("historic", "star-3.png", 11, 11,
      Seq("historic" -> ""),
      tagable => tagable.hasTag("historic")
        && !tagable.hasTag("historic", "memorial", "monument", "statue", "castle", "boundary_stone")
    )

    poi("boundary-stone", "modernmonument.png", 15, 15,
      Seq("historic" -> ""),
      tagable => tagable.hasTag("historic", "boundary_stone")
    )

    poi("castle", "castle-2.png", 11, 11,
      Seq("historic" -> "castle"),
      tagable => tagable.hasTag("historic", "castle")
    )

    poi("monument-memorial", "memorial.png", 11, 11,
      Seq("historic" -> ""),
      tagable => tagable.hasTag("historic", "monument", "memorial")
    )

    poi("statue", "statue-2.png", 11, 11,
      Seq("historic" -> "statue"),
      tagable => tagable.hasTag("historic", "statue")
    )

    poi("windmill", "windmill-2.png", 11, 11,
      Seq("man_made" -> "windmill", "building" -> "windmill"),
      tagable => tagable.hasTag("man_made", "windmill")
        || tagable.hasTag("building", "windmill")
    )

    poi("watermill", "watermill-2.png", 11, 11,
      Seq("man_made" -> "watermill"),
      tagable => tagable.hasTag("man_made", "watermill")
    )

    poi("zoo", "zoo.png", 11, 11,
      Seq("tourism" -> "zoo"),
      tagable => tagable.hasTag("tourism", "zoo")
    )
  }
}
