package kpn.core.poi.configuration

object PoiGroupSports {

  val builder: PoiGroupBuilder = new PoiGroupBuilder("sports", false) {

    poi("american-football", "usfootball.png", 15, 15,
      Seq("sport" -> "american_football"),
      tags => tags.has("sport", "american_football")
    )

    poi("baseball", "baseball.png", 15, 15,
      Seq("sport" -> "baseball"),
      tags => tags.has("sport", "baseball")
    )

    poi("basketball", "basketball.png", 15, 15,
      Seq("sport" -> "basketball"),
      tags => tags.has("sport", "basketball")
    )

    poi("cycling", "cycling.png", 15, 15,
      Seq("sport" -> "cycling"),
      tags => tags.has("sport", "cycling")
    )

    poi("gymnastics", "gymnastics.png", 15, 15,
      Seq("sport" -> "gymnastics"),
      tags => tags.has("sport", "gymnastics")
    )

    poi("golf", "golfing.png", 15, 15,
      Seq("leisure" -> "golf_course", "sport" -> "golf"),
      tags => tags.has("leisure", "golf_course")
        || tags.has("sport", "golf")
    )

    poi("hockey", "hockey.png", 15, 15,
      Seq("sport" -> "hockey", "sport" -> "field_hockey"),
      tags => tags.has("sport", "hockey")
        || tags.has("sport", "field_hockey")
    )

    poi("horseracing", "horseriding.png", 15, 15,
      Seq("sport" -> "horse_racing", "sport" -> "equestrian"),
      tags => tags.has("sport", "horse_racing")
        || tags.has("sport", "equestrian")
    )

    poi("icehockey", "icehockey.png", 15, 15,
      Seq("sport" -> "ice_hockey", "leisure" -> "ice_rink"),
      tags => tags.has("sport", "ice_hockey")
        || tags.has("leisure", "ice_rink")
    )

    poi("soccer", "soccer.png", 15, 15,
      Seq("sport" -> "soccer"),
      tags => tags.has("sport", "soccer")
    )

    poi("sportscentre", "indoor-arena.png", 15, 15,
      Seq("leisure" -> "sports_centre"),
      tags => tags.has("leisure", "sports_centre")
    )

    poi("surfing", "surfing.png", 15, 15,
      Seq("sport" -> "surfing"),
      tags => tags.has("sport", "surfing")
    )

    poi("swimming", "swimming.png", 15, 15,
      Seq("sport" -> "swimming"),
      tags => tags.has("sport", "swimming")
    )

    poi("tennis", "tennis.png", 15, 15,
      Seq("sport" -> "tennis"),
      tags => tags.has("sport", "tennis")
    )

    poi("volleyball", "volleyball.png", 15, 15,
      Seq("sport" -> "volleybal"),
      tags => tags.has("sport", "volleybal")
    )
  }
}
