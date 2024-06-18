package kpn.core.poi.configuration

object PoiGroupRestaurants {

  val builder: PoiGroupBuilder = new PoiGroupBuilder("restaurants", true) {

    poi("bar", "bar.png", 12, 13,
      Seq("amenity" -> "bar"),
      tagable => tagable.hasTag("amenity", "bar")
    )

    poi("bbq", "bbq.png", 12, 13,
      Seq("amenity" -> "bbq"),
      tagable => tagable.hasTag("amenity", "bbq")
    )

    poi("biergarten", "beergarden.png", 12, 13,
      Seq("amenity" -> "biergarten"),
      tagable => tagable.hasTag("amenity", "biergarten")
    )

    poi("cafe", "cafetaria.png", 12, 13,
      Seq("amenity" -> "cafe"),
      tagable => tagable.hasTag("amenity", "cafe")
    )

    poi("fastfood", "fastfood.png", 12, 13,
      Seq("amenity" -> "fast_food"),
      tagable => tagable.hasTag("amenity", "fast_food")
    )

    poi("foodcourt", "letter_f.png", 12, 13,
      Seq("amenity" -> "food_court"),
      tagable => tagable.hasTag("amenity", "food_court")
    )

    poi("icecream", "icecream.png", 12, 13,
      Seq("amenity" -> "ice_cream", "cuisine" -> "ice_cream"),
      tagable => tagable.hasTag("amenity", "ice_cream")
        || tagable.hasTag("cuisine", "ice_cream")
    )

    poi("pub", "pub.png", 12, 13,
      Seq("amenity" -> "pub"),
      tagable => tagable.hasTag("amenity", "pub")
    )

    poi("restaurant", "restaurant.png", 12, 13,
      Seq("amenity" -> "restaurant"),
      tagable => tagable.hasTag("amenity", "restaurant")
    )
  }
}
