package kpn.core.poi.configuration

object PoiGroupHikingBiking {

  val builder: PoiGroupBuilder = new PoiGroupBuilder("hiking-biking", true) {

    poi("ebike-charging", "e-bike-charging.png", 11, 11,
      Seq("amenity" -> "charging_station"),
      tags => tags.has("amenity", "charging_station")
        && tags.has("bicycle", "yes")
    )

    poi("bicycle", "bicycle_shop.png", 13, 13,
      Seq("shop" -> "bicycle"),
      tags => tags.has("shop", "bicyle")
    )

    poi("bicycle-rental", "cycling.png", 11, 11,
      Seq("amenity" -> "bicycle_rental"),
      tags => tags.has("amenity", "bicycle_rental")
    )

    poi("bicycle-parking", "parking_bicycle-2.png", 13, 14,
      Seq("amenity" -> "bicycle_parking"),
      tags => tags.has("amenity", "bicycle_parking")
    )

    poi("picnic", "picnic-2.png", 13, 14,
      Seq("tourism" -> "picnic_site", "leisure" -> "picnic_table"),
      tags => tags.has("tourism", "picnic_site")
        || tags.has("leisure", "picnic_table")
    )

    poi("bench", "bench.png", 13, 14,
      Seq("amenity" -> "bench"),
      tags => tags.has("amenity", "bench")
    )

    poi("toilets", "toilets.png", 13, 14,
      Seq("amenity" -> "toilets"),
      tags => tags.has("amenity", "toilets")
    )

    poi("drinking-water", "drinkingwater.png", 13, 14,
      Seq("amenity" -> "drinking_water"),
      tags => tags.has("amenity", "drinking_water")
    )

    poi("information", "information.png", 13, 13,
      Seq("tourism" -> "information"),
      tags => tags.has("tourism", "information")
    )

    poi("themepark", "themepark.png", 11, 11,
      Seq("tourism" -> "theme_park"),
      tags => tags.has("tourism", "theme_park")
    )

    poi("viewpoint", "viewpoint.png", 11, 11,
      Seq("tourism" -> "viewpoint"),
      tags => tags.has("tourism", "viewpoint")
    )

    poi("attraction", "star.png", 11, 11,
      Seq("tourism" -> "attraction"),
      tags => tags.has("tourism", "attraction")
    )

    poi("defibrillator", "aed-2.png", 13, 14,
      Seq("emergency" -> "defibrillator"),
      tags => tags.has("emergency", "defibrillator")
    )
  }
}
