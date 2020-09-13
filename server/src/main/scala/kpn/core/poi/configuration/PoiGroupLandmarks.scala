package kpn.core.poi.configuration

object PoiGroupLandmarks {

  val builder: PoiGroupBuilder = new PoiGroupBuilder("landmarks", true) {

    poi("place-of-worship", "church-2.png", 12, 13,
      Seq("amenity" -> "place_of_worship"),
      tags => tags.has("amenity", "place_of_worship")
        && !tags.has("religion", "christian", "muslim", "buddhist", "hindu", "jewish")
    )

    poi("church", "chapel-2.png", 12, 13,
      Seq("amenity" -> "place_of_worship"),
      tags => tags.has("amenity", "place_of_worship")
        && tags.has("religion", "christian")
        && !tags.has("historic", "wayside_shrine")
    )

    poi("mosque", "mosquee.png", 12, 13,
      Seq("amenity" -> "place_of_worship"),
      tags => tags.has("amenity", "place_of_worship")
        && tags.has("religion", "muslim")
    )

    poi("buddhist-temple", "bouddha.png", 12, 13,
      Seq("amenity" -> "place_of_worship"),
      tags => tags.has("amenity", "place_of_worship")
        && tags.has("religion", "buddhist")
    )

    poi("hindu-temple", "templehindu.png", 12, 13,
      Seq("amenity" -> "place_of_worship"),
      tags => tags.has("amenity", "place_of_worship")
        && tags.has("religion", "hindu")
    )

    poi("synagogue", "synagogue-2.png", 12, 13,
      Seq("amenity" -> "place_of_worship"),
      tags => tags.has("amenity", "place_of_worship")
        && tags.has("religion", "jewish")
    )

    poi("wayside-shrine", "cross-2.png", 15, 15,
      Seq("historic" -> "wayside_shrine"),
      tags => tags.has("historic", "wayside_shrine")
    )

    poi("heritage", "worldheritagesite.png", 12, 14,
      Seq("heritage" -> ""),
      tags => tags.has("heritage")
    )

    poi("historic", "star-3.png", 11, 11,
      Seq("historic" -> ""),
      tags => tags.has("historic")
        && !tags.has("historic", "memorial", "monument", "statue", "castle",	"boundary_stone")
    )

    poi("boundary-stone", "modernmonument.png", 15, 15,
      Seq("historic" -> ""),
      tags => tags.has("historic",	"boundary_stone")
    )

    poi("castle", "castle-2.png", 11, 11,
      Seq("historic" -> "castle"),
      tags => tags.has("historic", "castle")
    )

    poi("monument-memorial", "memorial.png", 11, 11,
      Seq("historic" -> ""),
      tags => tags.has("historic", "monument", "memorial")
    )

    poi("statue", "statue-2.png", 11, 11,
      Seq("historic" -> "statue"),
      tags => tags.has("historic", "statue")
    )

    poi("windmill", "windmill-2.png", 11, 11,
      Seq("man_made" -> "windmill", "building" -> "windmill"),
      tags => tags.has("man_made", "windmill")
        || tags.has("building", "windmill")
    )

    poi("watermill", "watermill-2.png", 11, 11,
      Seq("man_made" -> "watermill"),
      tags => tags.has("man_made", "watermill")
    )

    poi("zoo", "zoo.png", 11, 11,
      Seq("tourism" -> "zoo"),
      tags => tags.has("tourism", "zoo")
    )
  }
}
