package kpn.core.poi.configuration

object PoiGroupAmenity {

  val builder: PoiGroupBuilder = new PoiGroupBuilder("amenity", false) {

    poi("atm", "atm-2.png", 14, 15,
      Seq("amenity" -> "atm", "amenity" -> "bank"),
      tagable => tagable.hasTag("amenity", "atm")
        || (tagable.hasTag("amenity", "bank")
        && tagable.hasTag("atm")
        && !tagable.hasTag("atm", "no"))
    )

    poi("bank", "bank.png", 14, 15,
      Seq("amenity" -> "bank"),
      tagable => tagable.hasTag("amenity", "bank")
    )

    poi("cinema", "cinema.png", 14, 15,
      Seq("amenity" -> "cinema"),
      tagable => tagable.hasTag("amenity", "cinema")
    )

    poi("clinic", "firstaid.png", 14, 15,
      Seq("amenity" -> "clinic"),
      tagable => tagable.hasTag("amenity", "clinic")
    )

    poi("embassy", "embassy.png", 14, 15,
      Seq("amenity" -> "embassy"),
      tagable => tagable.hasTag("amenity", "embassy")
    )

    poi("firestation", "firemen.png", 14, 15,
      Seq("amenity" -> "firestation"),
      tagable => tagable.hasTag("amenity", "firestation")
    )

    poi("fuel", "fillingstation.png", 14, 15,
      Seq("amenity" -> "fuel"),
      tagable => tagable.hasTag("amenity", "fuel")
    )

    poi("hospital", "hospital-building.png", 14, 15,
      Seq("amenity" -> "hospital"),
      tagable => tagable.hasTag("amenity", "hospital")
    )

    poi("library", "library.png", 14, 15,
      Seq("amenity" -> "library"),
      tagable => tagable.hasTag("amenity", "library")
    )

    poi("musicschool", "musicschool.png", 14, 15,
      Seq("amenity" -> "music_school"),
      tagable => tagable.hasTag("amenity", "music_school")
    )

    poi("parking", "parkinggarage.png", 14, 15,
      Seq("amenity" -> "parking"),
      tagable => tagable.hasTag("amenity", "parking")
    )

    poi("pharmacy", "medicine.png", 14, 15,
      Seq("amenity" -> "pharmacy"),
      tagable => tagable.hasTag("amenity", "pharmacy")
    )

    poi("police", "police.png", 14, 15,
      Seq("amenity" -> "police"),
      tagable => tagable.hasTag("amenity", "police")
    )

    poi("postbox", "postal2.png", 15, 15,
      Seq("amenity" -> "post_box"),
      tagable => tagable.hasTag("amenity", "post_box")
    )

    poi("postoffice", "postal.png", 14, 15,
      Seq("amenity" -> "post_office"),
      tagable => tagable.hasTag("amenity", "post_office")
    )

    poi("school-college", "", 14, 15,
      Seq("amenity" -> "school", "amenity" -> "college"),
      tagable => tagable.hasTag("amenity", "school", "college")
    )

    poi("taxi", "taxi.png", 14, 15,
      Seq("amenity" -> "taxi"),
      tagable => tagable.hasTag("amenity", "taxi")
    )

    poi("theatre", "theater.png", 14, 15,
      Seq("amenity" -> "theatre"),
      tagable => tagable.hasTag("amenity", "theatre")
    )

    poi("university", "university.png", 14, 15,
      Seq("amenity" -> "university"),
      tagable => tagable.hasTag("amenity", "university")
    )

    poi("cemetery", "cemetary.png", 14, 15,
      Seq("landuse" -> "cemetery"),
      tagable => tagable.hasTag("landuse", "cemetery")
        && !tagable.hasTag("animal")
    )

    poi("busstop", "busstop.png", 15, 15,
      Seq("highway" -> "bus_stop"),
      tagable => tagable.hasTag("highway", "bus_stop")
    )
  }
}
