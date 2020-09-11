package kpn.core.poi.configuration

object PoiGroupAmenity {

  val builder: PoiGroupBuilder = new PoiGroupBuilder("amenity", false) {

    poi("atm", "atm-2.png", 14, 15,
      Seq("amenity" -> "atm", "amenity" -> "bank"),
      tags => tags.has("amenity", "atm")
        || (tags.has("amenity", "bank")
        && tags.has("atm")
        && !tags.has("atm", "no"))
    )

    poi("bank", "bank.png", 14, 15,
      Seq("amenity" -> "bank"),
      tags => tags.has("amenity", "bank")
    )

    poi("cinema", "cinema.png", 14, 15,
      Seq("amenity" -> "cinema"),
      tags => tags.has("amenity", "cinema")
    )

    poi("clinic", "firstaid.png", 14, 15,
      Seq("amenity" -> "clinic"),
      tags => tags.has("amenity", "clinic")
    )

    poi("embassy", "embassy.png", 14, 15,
      Seq("amenity" -> "embassy"),
      tags => tags.has("amenity", "embassy")
    )

    poi("firestation", "firemen.png", 14, 15,
      Seq("amenity" -> "firestation"),
      tags => tags.has("amenity", "firestation")
    )

    poi("fuel", "fillingstation.png", 14, 15,
      Seq("amenity" -> "fuel"),
      tags => tags.has("amenity", "fuel")
    )

    poi("hospital", "hospital-building.png", 14, 15,
      Seq("amenity" -> "hospital"),
      tags => tags.has("amenity", "hospital")
    )

    poi("library", "library.png", 14, 15,
      Seq("amenity" -> "library"),
      tags => tags.has("amenity", "library")
    )

    poi("musicschool", "musicschool.png", 14, 15,
      Seq("amenity" -> "music_school"),
      tags => tags.has("amenity", "music_school")
    )

    poi("parking", "parkinggarage.png", 14, 15,
      Seq("amenity" -> "parking"),
      tags => tags.has("amenity", "parking")
    )

    poi("pharmacy", "medicine.png", 14, 15,
      Seq("amenity" -> "pharmacy"),
      tags => tags.has("amenity", "pharmacy")
    )

    poi("police", "police.png", 14, 15,
      Seq("amenity" -> "police"),
      tags => tags.has("amenity", "police")
    )

    poi("postbox", "postal2.png", 15, 15,
      Seq("amenity" -> "post_box"),
      tags => tags.has("amenity", "post_box")
    )

    poi("postoffice", "postal.png", 14, 15,
      Seq("amenity" -> "post_office"),
      tags => tags.has("amenity", "post_office")
    )

    poi("school-college", "", 14, 15,
      Seq("amenity" -> "school", "amenity" -> "college"),
      tags => tags.has("amenity", "school", "college")
    )

    poi("taxi", "taxi.png", 14, 15,
      Seq("amenity" -> "taxi"),
      tags => tags.has("amenity", "taxi")
    )

    poi("theatre", "theater.png", 14, 15,
      Seq("amenity" -> "theatre"),
      tags => tags.has("amenity", "theatre")
    )

    poi("university", "university.png", 14, 15,
      Seq("amenity" -> "university"),
      tags => tags.has("amenity", "university")
    )

    poi("cemetery", "cemetary.png", 14, 15,
      Seq("landuse" -> "cemetery"),
      tags => tags.has("landuse", "cemetery")
        && !tags.has("animal")
    )

    poi("busstop", "busstop.png", 15, 15,
      Seq("highway" -> "bus_stop"),
      tags => tags.has("highway", "bus_stop")
    )
  }
}
