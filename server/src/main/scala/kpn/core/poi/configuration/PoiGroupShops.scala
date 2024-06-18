package kpn.core.poi.configuration

object PoiGroupShops {

  val builder: PoiGroupBuilder = new PoiGroupBuilder("shops", false) {

    poi("beauty", "beautysalon.png", 14, 15,
      Seq("shop" -> "beauty"),
      tagable => tagable.hasTag("shop", "beauty")
    )

    poi("books-stationary", "library.png", 14, 15,
      Seq("shop" -> "books", "shop" -> "stationary"),
      tagable => tagable.hasTag("shop", "books", "stationary")
    )

    poi("car", "car.png", 14, 15,
      Seq("shop" -> "car"),
      tagable => tagable.hasTag("shop", "car")
    )

    poi("chemist", "drugstore.png", 14, 15,
      Seq("shop" -> "chemist"),
      tagable => tagable.hasTag("shop", "chemist")
    )

    poi("clothes", "clothers_female.png", 14, 15,
      Seq("shop" -> "clothes"),
      tagable => tagable.hasTag("shop", "clothes")
    )

    poi("copyshop", "letter_c.png", 14, 15,
      Seq("shop" -> "copyshop"),
      tagable => tagable.hasTag("shop", "copyshop")
    )

    poi("cosmetics", "perfumery.png", 14, 15,
      Seq("shop" -> "cosmetics"),
      tagable => tagable.hasTag("shop", "cosmetics")
    )

    poi("departmentstore", "departmentstore.png", 14, 15,
      Seq("shop" -> "department_store"),
      tagable => tagable.hasTag("shop", "department_store")
    )

    poi("diy-hardware", "tools.png", 14, 15,
      Seq("shop" -> "doityourself", "shop" -> "hardware"),
      tagable => tagable.hasTag("shop", "doityourself", "hardware")
    )

    poi("garden-centre", "flowers-1.png", 14, 15,
      Seq("shop" -> "garden_centre"),
      tagable => tagable.hasTag("shop", "garden_centre")
    )

    poi("general", "letter_g.png", 14, 15,
      Seq("shop" -> "general"),
      tagable => tagable.hasTag("shop", "general")
    )

    poi("gift", "gifts.png", 14, 15,
      Seq("shop" -> "gift"),
      tagable => tagable.hasTag("shop", "gift")
    )

    poi("hairdresser", "barber.png", 14, 15,
      Seq("shop" -> "hairdresser"),
      tagable => tagable.hasTag("shop", "hairdresser")
    )

    poi("jewelry", "jewelry.png", 14, 15,
      Seq("shop" -> "jewelry", "shop" -> "jewellery"),
      tagable => tagable.hasTag("shop", "jewelry", "jewellery")
    )

    poi("kiosk", "kiosk.png", 14, 15,
      Seq("shop" -> "kiosk"),
      tagable => tagable.hasTag("shop", "kiosk")
    )

    poi("leather", "bags.png", 14, 15,
      Seq("shop" -> "leather"),
      tagable => tagable.hasTag("shop", "leather")
    )

    poi("marketplace", "market.png", 14, 15,
      Seq("amenity" -> "marketplace"),
      tagable => tagable.hasTag("amenity", "marketplace")
    )

    poi("musical-instrument", "music_rock.png", 14, 15,
      Seq("shop" -> "musical_instrument"),
      tagable => tagable.hasTag("shop", "musical_instrument")
    )

    poi("optician", "glasses.png", 14, 15,
      Seq("shop" -> "optician"),
      tagable => tagable.hasTag("shop", "optician")
    )

    poi("pets", "pets.png", 14, 15,
      Seq("shop" -> "pet"),
      tagable => tagable.hasTag("shop", "pet")
    )

    poi("phone", "phones.png", 14, 15,
      Seq("shop" -> "mobile_phone"),
      tagable => tagable.hasTag("shop", "mobile_phone")
    )

    poi("photo", "photo.png", 14, 15,
      Seq("shop" -> "photo"),
      tagable => tagable.hasTag("shop", "photo")
    )

    poi("shoes", "highhills.png", 14, 15,
      Seq("shop" -> "shoes"),
      tagable => tagable.hasTag("shop", "shoes")
    )

    poi("shoppingcentre", "mall.png", 14, 15,
      Seq("shop" -> "mall"),
      tagable => tagable.hasTag("shop", "mall")
    )

    poi("textiles", "textiles.png", 14, 15,
      Seq("shop" -> "textiles"),
      tagable => tagable.hasTag("shop", "textiles")
    )

    poi("toys", "toys.png", 14, 15,
      Seq("shop" -> "toys"),
      tagable => tagable.hasTag("shop", "toys")
    )

    poi("travelagency", "travel_agency.png", 14, 15,
      Seq("shop" -> "travel_agency"),
      tagable => tagable.hasTag("shop", "travel_agency")
    )
  }
}
