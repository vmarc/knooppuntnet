package kpn.core.poi.configuration

object PoiGroupRestaurants {

  val builder: PoiGroupBuilder = new PoiGroupBuilder("restaurants", true) {

    poi("bar", "bar.png", 12, 13,
      Seq("amenity" -> "bar"),
      tags => tags.has("amenity", "bar")
    )

    poi("bbq", "bbq.png", 12, 13,
      Seq("amenity" -> "bbq"),
      tags => tags.has("amenity", "bbq")
    )

    poi("biergarten", "beergarden.png", 12, 13,
      Seq("amenity" -> "biergarten"),
      tags => tags.has("amenity", "biergarten")
    )

    poi("cafe", "cafetaria.png", 12, 13,
      Seq("amenity" -> "cafe"),
      tags => tags.has("amenity", "cafe")
    )

    poi("fastfood", "fastfood.png", 12, 13,
      Seq("amenity" -> "fast_food"),
      tags => tags.has("amenity", "fast_food")
    )

    poi("foodcourt", "letter_f.png", 12, 13,
      Seq("amenity" -> "food_court"),
      tags => tags.has("amenity", "food_court")
    )

    poi("icecream", "icecream.png", 12, 13,
      Seq("amenity" -> "ice_cream", "cuisine" -> "ice_cream"),
      tags => tags.has("amenity", "ice_cream")
        || tags.has("cuisine", "ice_cream")
    )

    poi("pub", "pub.png", 12, 13,
      Seq("amenity" -> "pub"),
      tags => tags.has("amenity", "pub")
    )

    poi("restaurant", "restaurant.png", 12, 13,
      Seq("amenity" -> "restaurant"),
      tags => tags.has("amenity", "restaurant")
    )
  }
}
