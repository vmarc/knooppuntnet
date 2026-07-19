package kpn.core.poi.configuration

object PoiGroupHikingBiking {

  val builder: PoiGroupBuilder = new PoiGroupBuilder("hiking-biking", true) {

    poi("ebike-charging", "e-bike-charging.png", 11, 11,
      Seq("amenity" -> "charging_station"),
      tagable => tagable.hasTag("amenity", "charging_station")
        && tagable.hasTag("bicycle", "yes")
    )

    poi("bicycle", "bicycle_shop.png", 13, 13,
      Seq("shop" -> "bicycle"),
      tagable => tagable.hasTag("shop", "bicyle")
    )

    poi("bicycle-rental", "cycling.png", 11, 11,
      Seq("amenity" -> "bicycle_rental"),
      tagable => tagable.hasTag("amenity", "bicycle_rental")
    )

    poi("bicycle-rental-2", "bicycle_parking.png", 15, 15,
      Seq("amenity" -> "bicycle_rental"),
      tagable => tagable.hasTag("amenity", "bicycle_rental")
        && tagable.hasTag("network", "Velo", "Villo!")
    )

    poi("bicycle-parking", "parking_bicycle-2.png", 13, 14,
      Seq("amenity" -> "bicycle_parking"),
      tagable => tagable.hasTag("amenity", "bicycle_parking")
    )

    poi("picnic", "picnic-2.png", 13, 14,
      Seq("tourism" -> "picnic_site", "leisure" -> "picnic_table"),
      tagable => tagable.hasTag("tourism", "picnic_site")
        || tagable.hasTag("leisure", "picnic_table")
    )

    poi("bench", "bench.png", 13, 14,
      Seq("amenity" -> "bench"),
      tagable => tagable.hasTag("amenity", "bench")
    )

    poi("toilets", "toilets.png", 13, 14,
      Seq("amenity" -> "toilets"),
      tagable => tagable.hasTag("amenity", "toilets")
    )

    poi("drinking-water", "drinkingwater.png", 13, 14,
      Seq("amenity" -> "drinking_water"),
      tagable => tagable.hasTag("amenity", "drinking_water")
    )

    poi("information", "information.png", 13, 13,
      Seq("tourism" -> "information"),
      tagable => tagable.hasTag("tourism", "information")
    )

    poi("themepark", "themepark.png", 11, 11,
      Seq("tourism" -> "theme_park"),
      tagable => tagable.hasTag("tourism", "theme_park")
    )

    poi("viewpoint", "viewpoint.png", 11, 11,
      Seq("tourism" -> "viewpoint"),
      tagable => tagable.hasTag("tourism", "viewpoint")
    )

    poi("attraction", "star.png", 11, 11,
      Seq("tourism" -> "attraction"),
      tagable => tagable.hasTag("tourism", "attraction")
        && !tagable.hasTag("building", "windmill")
    )

    poi("defibrillator", "aed-2.png", 13, 14,
      Seq("emergency" -> "defibrillator"),
      tagable => tagable.hasTag("emergency", "defibrillator")
    )
  }
}
