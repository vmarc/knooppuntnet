package kpn.core.poi.configuration

object PoiGroupFoodshops {

  val builder: PoiGroupBuilder = new PoiGroupBuilder("foodshops", false) {

    poi("alcohol", "liquor.png", 14, 15,
      Seq("shop" -> "alcohol"),
      tags => tags.has("shop", "alcohol")
    )

    poi("bakery", "bread.png", 14, 15,
      Seq("shop" -> "bakery"),
      tags => tags.has("shop", "bakery")
    )

    poi("beverages", "bar_coktail.png", 14, 15,
      Seq("shop" -> "beverages"),
      tags => tags.has("shop", "beverages")
    )

    poi("butcher", "butcher-2.png", 14, 15,
      Seq("shop" -> "butcher"),
      tags => tags.has("shop", "butcher")
    )

    poi("cheese", "cheese.png", 14, 15,
      Seq("shop" -> "cheese"),
      tags => tags.has("shop", "cheese")
    )

    poi("chocolate", "candy.png", 14, 15,
      Seq("shop" -> "chocolate"),
      tags => tags.has("shop", "chocolate")
    )

    poi("confectionery", "candy.png", 14, 15,
      Seq("shop" -> "confectionery"),
      tags => tags.has("shop", "confectionery")
    )

    poi("coffee", "coffee.png", 11, 11,
      Seq("shop" -> "coffee"),
      tags => tags.has("shop", "coffee")
    )

    poi("dairy", "milk_and_cookies.png", 14, 15,
      Seq("shop" -> "dairy"),
      tags => tags.has("shop", "dairy")
    )

    poi("deli", "patisserie.png", 14, 15,
      Seq("shop" -> "deli"),
      tags => tags.has("shop", "deli")
    )

    poi("grocery", "grocery.png", 14, 15,
      Seq("shop" -> "grocery"),
      tags => tags.has("shop", "grocery")
    )

    poi("organic", "restaurant_vegetarian.png", 14, 15,
      Seq("shop" -> "organic"),
      tags => tags.has("shop", "organic")
    )

    poi("seafood", "restaurant_fish.png", 14, 15,
      Seq("shop" -> "seafood"),
      tags => tags.has("shop", "seafood")
    )

    poi("supermarket", "supermarket.png", 14, 15,
      Seq("shop" -> "supermarket"),
      tags => tags.has("shop", "supermarket")
    )

    poi("wine", "winebar.png", 14, 15,
      Seq("shop" -> "wine"),
      tags => tags.has("shop", "wine")
    )
  }
}
