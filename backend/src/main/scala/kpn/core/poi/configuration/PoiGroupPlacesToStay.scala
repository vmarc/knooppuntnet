package kpn.core.poi.configuration

object PoiGroupPlacesToStay {

  val builder: PoiGroupBuilder = new PoiGroupBuilder("places-to-stay", true) {

    poi("alpine-hut", "alpinehut.png", 11, 13,
      Seq("tourism" -> "alpine_hut"),
      tagable => tagable.hasTag("tourism", "alpine_hut")
    )

    poi("apartment", "apartment-3.png", 13, 13,
      Seq("tourism" -> "apartment"),
      tagable => tagable.hasTag("tourism", "apartment")
    )

    poi("campsite", "camping-2.png", 13, 13,
      Seq("tourism" -> "camp_site"),
      tagable => tagable.hasTag("tourism", "camp_site")
        && !tagable.hasTag("backcountry", "yes")
    )

    poi("chalet", "letter_c.png", 13, 13,
      Seq("tourism" -> "chalet"),
      tagable => tagable.hasTag("tourism", "chalet")
    )

    poi("guesthouse", "bed_breakfast.png", 11, 13,
      Seq("tourism" -> "guest_house", "tourism" -> "bed_and_breakfast"),
      tagable => tagable.hasTag("tourism", "guest_house", "bed_and_breakfast")
    )

    poi("hostel", "hostel_0star.png", 11, 13,
      Seq("tourism" -> "hostel"),
      tagable => tagable.hasTag("tourism", "hostel")
    )

    poi("hotel", "hotel_0star.png", 11, 13,
      Seq("tourism" -> "hotel"),
      tagable => tagable.hasTag("tourism", "hotel")
    )

    poi("motel", "motel-2.png", 11, 13,
      Seq("tourism" -> "motel"),
      tagable => tagable.hasTag("tourism", "motel")
    )

    poi("spa", "spa -> ", 13, 13,
      Seq("leisure" -> "spa"),
      tagable => tagable.hasTag("leisure", "spa")
    )

    poi("sauna", "sauna.png", 13, 13,
      Seq("leisure" -> "sauna"),
      tagable => tagable.hasTag("leisure", "sauna")
    )
  }
}
