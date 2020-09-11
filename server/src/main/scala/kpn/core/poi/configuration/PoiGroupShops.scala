package kpn.core.poi.configuration

object PoiGroupShops {

  val builder: PoiGroupBuilder = new PoiGroupBuilder("shops", false) {

    poi("beauty", "beautysalon.png", 14, 15,
      Seq("shop" -> "beauty"),
      tags => tags.has("shop", "beauty")
    )

    poi("books-stationary", "library.png", 14, 15,
      Seq("shop" -> "books", "shop" -> "stationary"),
      tags => tags.has("shop", "books", "stationary")
    )

    poi("car", "car.png", 14, 15,
      Seq("shop" -> "car"),
      tags => tags.has("shop", "car")
    )

    poi("chemist", "drugstore.png", 14, 15,
      Seq("shop" -> "chemist"),
      tags => tags.has("shop", "chemist")
    )

    poi("clothes", "clothers_female.png", 14, 15,
      Seq("shop" -> "clothes"),
      tags => tags.has("shop", "clothes")
    )

    poi("copyshop", "letter_c.png", 14, 15,
      Seq("shop" -> "copyshop"),
      tags => tags.has("shop", "copyshop")
    )

    poi("cosmetics", "perfumery.png", 14, 15,
      Seq("shop" -> "cosmetics"),
      tags => tags.has("shop", "cosmetics")
    )

    poi("departmentstore", "departmentstore.png", 14, 15,
      Seq("shop" -> "department_store"),
      tags => tags.has("shop", "department_store")
    )

    poi("diy-hardware", "tools.png", 14, 15,
      Seq("shop" -> "doityourself", "shop" -> "hardware"),
      tags => tags.has("shop", "doityourself", "hardware")
    )

    poi("garden-centre", "flowers-1.png", 14, 15,
      Seq("shop" -> "garden_centre"),
      tags => tags.has("shop", "garden_centre")
    )

    poi("general", "letter_g.png", 14, 15,
      Seq("shop" -> "general"),
      tags => tags.has("shop", "general")
    )

    poi("gift", "gifts.png", 14, 15,
      Seq("shop" -> "gift"),
      tags => tags.has("shop", "gift")
    )

    poi("hairdresser", "barber.png", 14, 15,
      Seq("shop" -> "hairdresser"),
      tags => tags.has("shop", "hairdresser")
    )

    poi("jewelry", "jewelry.png", 14, 15,
      Seq("shop" -> "jewelry", "shop" -> "jewellery"),
      tags => tags.has("shop", "jewelry", "jewellery")
    )

    poi("kiosk", "kiosk.png", 14, 15,
      Seq("shop" -> "kiosk"),
      tags => tags.has("shop", "kiosk")
    )

    poi("leather", "bags.png", 14, 15,
      Seq("shop" -> "leather"),
      tags => tags.has("shop", "leather")
    )

    poi("marketplace", "market.png", 14, 15,
      Seq("amenity" -> "marketplace"),
      tags => tags.has("amenity", "marketplace")
    )

    poi("musical-instrument", "music_rock.png", 14, 15,
      Seq("shop" -> "musical_instrument"),
      tags => tags.has("shop", "musical_instrument")
    )

    poi("optician", "glasses.png", 14, 15,
      Seq("shop" -> "optician"),
      tags => tags.has("shop", "optician")
    )

    poi("pets", "pets.png", 14, 15,
      Seq("shop" -> "pet"),
      tags => tags.has("shop", "pet")
    )

    poi("phone", "phones.png", 14, 15,
      Seq("shop" -> "mobile_phone"),
      tags => tags.has("shop", "mobile_phone")
    )

    poi("photo", "photo.png", 14, 15,
      Seq("shop" -> "photo"),
      tags => tags.has("shop", "photo")
    )

    poi("shoes", "highhills.png", 14, 15,
      Seq("shop" -> "shoes"),
      tags => tags.has("shop", "shoes")
    )

    poi("shoppingcentre", "mall.png", 14, 15,
      Seq("shop" -> "mall"),
      tags => tags.has("shop", "mall")
    )

    poi("textiles", "textiles.png", 14, 15,
      Seq("shop" -> "textiles"),
      tags => tags.has("shop", "textiles")
    )

    poi("toys", "toys.png", 14, 15,
      Seq("shop" -> "toys"),
      tags => tags.has("shop", "toys")
    )

    poi("travelagency", "travel_agency.png", 14, 15,
      Seq("shop" -> "travel_agency"),
      tags => tags.has("shop", "travel_agency")
    )
  }
}
