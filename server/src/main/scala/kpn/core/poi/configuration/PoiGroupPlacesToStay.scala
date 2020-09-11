package kpn.core.poi.configuration

object PoiGroupPlacesToStay {

  val builder: PoiGroupBuilder = new PoiGroupBuilder("places-to-stay", true) {

    poi("alpine-hut", "alpinehut.png", 11, 13,
      Seq("tourism" -> "alpine_hut"),
      tags => tags.has("tourism", "alpine_hut")
    )

    poi("apartment", "apartment-3.png", 13, 13,
      Seq("tourism" -> "apartment"),
      tags => tags.has("tourism", "apartment")
    )

    poi("campsite", "camping-2.png", 13, 13,
      Seq("tourism" -> "camp_site"),
      tags => tags.has("tourism", "camp_site")
        && !tags.has("backcountry", "yes")
    )

    poi("chalet", "letter_c.png", 13, 13,
      Seq("tourism" -> "chalet"),
      tags => tags.has("tourism", "chalet")
    )

    poi("guesthouse", "bed_breakfast.png", 11, 13,
      Seq("tourism" -> "guest_house", "tourism" -> "bed_and_breakfast"),
      tags => tags.has("tourism", "guest_house", "bed_and_breakfast")
    )

    poi("hostel", "hostel_0star.png", 11, 13,
      Seq("tourism" -> "hostel"),
      tags => tags.has("tourism", "hostel")
    )

    poi("hotel", "hotel_0star.png", 11, 13,
      Seq("tourism" -> "hotel"),
      tags => tags.has("tourism", "hotel")
    )

    poi("motel", "motel-2.png", 11, 13,
      Seq("tourism" -> "motel"),
      tags => tags.has("tourism", "motel")
    )

    poi("spa", "spa -> ", 13, 13,
      Seq("leisure" -> "spa"),
      tags => tags.has("leisure", "spa")
    )

    poi("sauna", "sauna.png", 13, 13,
      Seq("leisure" -> "sauna"),
      tags => tags.has("leisure", "sauna")
    )
  }
}
