package kpn.core.poi.configuration

object PoiGroupFoodshops {

  val builder: PoiGroupBuilder = new PoiGroupBuilder("foodshops", false) {

    poi("alcohol", "liquor.png", 14, 15,
      Seq("shop" -> "alcohol"),
      tagable => tagable.hasTag("shop", "alcohol")
    )

    poi("bakery", "bread.png", 14, 15,
      Seq("shop" -> "bakery"),
      tagable => tagable.hasTag("shop", "bakery")
    )

    poi("beverages", "bar_coktail.png", 14, 15,
      Seq("shop" -> "beverages"),
      tagable => tagable.hasTag("shop", "beverages")
    )

    poi("butcher", "butcher-2.png", 14, 15,
      Seq("shop" -> "butcher"),
      tagable => tagable.hasTag("shop", "butcher")
    )

    poi("cheese", "cheese.png", 14, 15,
      Seq("shop" -> "cheese"),
      tagable => tagable.hasTag("shop", "cheese")
    )

    poi("chocolate", "candy.png", 14, 15,
      Seq("shop" -> "chocolate"),
      tagable => tagable.hasTag("shop", "chocolate")
    )

    poi("confectionery", "candy.png", 14, 15,
      Seq("shop" -> "confectionery"),
      tagable => tagable.hasTag("shop", "confectionery")
    )

    poi("coffee", "coffee.png", 11, 11,
      Seq("shop" -> "coffee"),
      tagable => tagable.hasTag("shop", "coffee")
    )

    poi("dairy", "milk_and_cookies.png", 14, 15,
      Seq("shop" -> "dairy"),
      tagable => tagable.hasTag("shop", "dairy")
    )

    poi("deli", "patisserie.png", 14, 15,
      Seq("shop" -> "deli"),
      tagable => tagable.hasTag("shop", "deli")
    )

    poi("grocery", "grocery.png", 14, 15,
      Seq("shop" -> "grocery"),
      tagable => tagable.hasTag("shop", "grocery")
    )

    poi("organic", "restaurant_vegetarian.png", 14, 15,
      Seq("shop" -> "organic"),
      tagable => tagable.hasTag("shop", "organic")
    )

    poi("seafood", "restaurant_fish.png", 14, 15,
      Seq("shop" -> "seafood"),
      tagable => tagable.hasTag("shop", "seafood")
    )

    poi("supermarket", "supermarket.png", 14, 15,
      Seq("shop" -> "supermarket"),
      tagable => tagable.hasTag("shop", "supermarket")
    )

    poi("wine", "winebar.png", 14, 15,
      Seq("shop" -> "wine"),
      tagable => tagable.hasTag("shop", "wine")
    )
  }
}
