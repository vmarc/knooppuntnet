package kpn.core.poi.configuration

object PoiGroupSports {

  val builder: PoiGroupBuilder = new PoiGroupBuilder("sports", false) {

    poi("american-football", "usfootball.png", 15, 15,
      Seq("sport" -> "american_football"),
      tagable => tagable.hasTag("sport", "american_football")
    )

    poi("baseball", "baseball.png", 15, 15,
      Seq("sport" -> "baseball"),
      tagable => tagable.hasTag("sport", "baseball")
    )

    poi("basketball", "basketball.png", 15, 15,
      Seq("sport" -> "basketball"),
      tagable => tagable.hasTag("sport", "basketball")
    )

    poi("cycling", "cycling.png", 15, 15,
      Seq("sport" -> "cycling"),
      tagable => tagable.hasTag("sport", "cycling")
    )

    poi("gymnastics", "gymnastics.png", 15, 15,
      Seq("sport" -> "gymnastics"),
      tagable => tagable.hasTag("sport", "gymnastics")
    )

    poi("golf", "golfing.png", 15, 15,
      Seq("leisure" -> "golf_course", "sport" -> "golf"),
      tagable => tagable.hasTag("leisure", "golf_course")
        || tagable.hasTag("sport", "golf")
    )

    poi("hockey", "hockey.png", 15, 15,
      Seq("sport" -> "hockey", "sport" -> "field_hockey"),
      tagable => tagable.hasTag("sport", "hockey")
        || tagable.hasTag("sport", "field_hockey")
    )

    poi("horseracing", "horseriding.png", 15, 15,
      Seq("sport" -> "horse_racing", "sport" -> "equestrian"),
      tagable => tagable.hasTag("sport", "horse_racing")
        || tagable.hasTag("sport", "equestrian")
    )

    poi("icehockey", "icehockey.png", 15, 15,
      Seq("sport" -> "ice_hockey", "leisure" -> "ice_rink"),
      tagable => tagable.hasTag("sport", "ice_hockey")
        || tagable.hasTag("leisure", "ice_rink")
    )

    poi("soccer", "soccer.png", 15, 15,
      Seq("sport" -> "soccer"),
      tagable => tagable.hasTag("sport", "soccer")
    )

    poi("sportscentre", "indoor-arena.png", 15, 15,
      Seq("leisure" -> "sports_centre"),
      tagable => tagable.hasTag("leisure", "sports_centre")
    )

    poi("surfing", "surfing.png", 15, 15,
      Seq("sport" -> "surfing"),
      tagable => tagable.hasTag("sport", "surfing")
    )

    poi("swimming", "swimming.png", 15, 15,
      Seq("sport" -> "swimming"),
      tagable => tagable.hasTag("sport", "swimming")
    )

    poi("tennis", "tennis.png", 15, 15,
      Seq("sport" -> "tennis"),
      tagable => tagable.hasTag("sport", "tennis")
    )

    poi("volleyball", "volleyball.png", 15, 15,
      Seq("sport" -> "volleybal"),
      tagable => tagable.hasTag("sport", "volleybal")
    )
  }
}
