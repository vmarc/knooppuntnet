package kpn.core.poi

import kpn.api.custom.Tags

class PoiGroupBuilder(name: String, defaultEnabled: Boolean) {

  def poi(name: String, icon: String, minLevel: Long, defaultLevel: Long, xxx: Seq[Tuple2[String, String]], tags: Tags => Boolean): Unit = {
  }

}

object PoiConfigurationBuilder {

  val groupBuilders: Seq[PoiGroupBuilder] = Seq(

    new PoiGroupBuilder("hiking-biking", true) {

      poi("ebike-charging", "e-bike-charging.png", 11, 11,
        Seq("amenity" -> "charging_station"),
        tags => tags.has("amenity", "charging_station") && tags.has("bicycle", "yes")
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
        tags => tags.has("tourism", "picnic_site") || tags.has("leisure", "picnic_table")
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
    },

    new PoiGroupBuilder("landmarks", true) {

      poi("place-of-worship", "church-2.png", 12, 13,
        Seq("amenity" -> "place_of_worship"),
        { tags =>
          tags.has("amenity", "place_of_worship") &&
            !tags.has("religion", "christian", "muslim", "buddhist", "hindu", "jewish")
        }
      )

      poi("church", "chapel-2.png", 12, 13,
        Seq("amenity" -> "place_of_worship"),
        tags => tags.has("amenity", "place_of_worship") && tags.has("religion", "christian")
      )

      poi("mosque", "mosquee.png", 12, 13,
        Seq("amenity" -> "place_of_worship"),
        tags => tags.has("amenity", "place_of_worship") && tags.has("religion", "muslim")
      )

      poi("buddhist-temple", "bouddha.png", 12, 13,
        Seq("amenity" -> "place_of_worship"),
        tags => tags.has("amenity", "place_of_worship") && tags.has("religion", "buddhist")
      )

      poi("hindu-temple", "templehindu.png", 12, 13,
        Seq("amenity" -> "place_of_worship"),
        tags => tags.has("amenity", "place_of_worship") && tags.has("religion", "hindu")
      )

      poi("synagogue", "synagogue-2.png", 12, 13,
        Seq("amenity" -> "place_of_worship"),
        { tags =>
          tags.has("amenity", "place_of_worship") && tags.has("religion", "jewish")
        }
      )

      poi("heritage", "worldheritagesite.png", 12, 14,
        Seq("heritage" -> ""),
        tags => tags.has("heritage")
      )

      poi("historic", "star-3.png", 11, 11,
        Seq("historic" -> ""),
        tags => tags.has("historic") && !tags.has("historic", "memorial", "monument", "statue", "castle")
      )

      poi("castle", "castle-2.png", 11, 11,
        Seq("historic" -> "castle"),
        tags => tags.has("historic", "castle")
      )

      poi("monument-memorial", "memorial.png", 11, 11,
        Seq("historic" -> ""),
        tags => tags.has("historic", "monument", "memorial")
      )

      poi("statue", "statue-2.png", 11, 11,
        Seq("historic" -> "statue"),
        tags => tags.has("historic", "statue")
      )

      poi("windmill", "windmill-2.png", 11, 11,
        Seq("man_made" -> "windmill"),
        tags => tags.has("man_made", "windmill")
      )

      poi("watermill", "watermill-2.png", 11, 11,
        Seq("man_made" -> "watermill"),
        tags => tags.has("man_made", "watermill")
      )

      poi("zoo", "zoo.png", 11, 11,
        Seq("tourism" -> "zoo"),
        tags => tags.has("tourism", "zoo")
      )
    },

    new PoiGroupBuilder("restaurants", true) {

      //      PoiDefinition("bar", "bar.png", 12, 13, HasTag("amenity", "bar")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("bbq", "bbq.png", 12, 13, HasTag("amenity", "bbq")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("biergarten", "beergarden.png", 12, 13, HasTag("amenity", "biergarten")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("cafe", "cafetaria.png", 12, 13, HasTag("amenity", "cafe")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("fastfood", "fastfood.png", 12, 13, HasTag("amenity", "fast_food")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("foodcourt", "letter_f.png", 12, 13, HasTag("amenity", "food_court")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("icecream", "icecream.png", 12, 13, HasTag("amenity", "ice_cream").or(HasTag("cuisine", "ice_cream"))),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("pub", "pub.png", 12, 13, HasTag("amenity", "pub")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("restaurant", "restaurant.png", 12, 13, HasTag("amenity", "restaurant"))
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

    },

    new PoiGroupBuilder("places-to-stay", true) {

      //      PoiDefinition("alpine-hut", "alpinehut.png", 11, 13, HasTag("tourism", "alpine_hut")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("apartment", "apartment-3.png", 13, 13, HasTag("tourism", "apartment")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("campsite", "camping-2.png", 13, 13, HasTag("tourism", "camp_site").and(NotHasTag("backcountry", "yes"))),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("chalet", "letter_c.png", 13, 13, HasTag("tourism", "chalet")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("guesthouse", "bed_breakfast.png", 11, 13, TagContains("tourism", "guest_house", "bed_and_breakfast")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("hostel", "hostel_0star.png", 11, 13, HasTag("tourism", "hostel")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("hotel", "hotel_0star.png", 11, 13, HasTag("tourism", "hotel")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("motel", "motel-2.png", 11, 13, HasTag("tourism", "motel")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("spa", "spa -> ", 13, 13, HasTag("leisure", "spa")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("sauna", "sauna.png", 13, 13, HasTag("leisure", "sauna"))
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

    },

    new PoiGroupBuilder("tourism", true) {

      //      PoiDefinition("arts-centre", "letter_a.png", 13, 13, HasTag("amenity", "arts_centre")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("artwork", "artwork.png", 13, 13, HasTag("tourism=artwork").and(NotTagContains("artwork_type", "statue"))),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("casino", "casino.png", 13, 13, HasTag("leisure", "casino").or(HasTag("amenity", "casino"))),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("gallery", "artgallery.png", 13, 13, HasTag("tourism", "gallery")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("monumental-tree", "tree.png", 13, 13, HasTag("natural", "tree").and(HasTag("monument", "yes"))),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("museum", "museum_art.png", 13, 13, HasTag("tourism", "museum")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("vineyard", "vineyard.png", 13, 13, HasTag("landuse", "vineyard")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("tourism", "sight-2.png", 13, 13, HasTag("tourism", "yes")) // TODO review instances of this poi
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

    },

    new PoiGroupBuilder("amenity", false) {

      //      PoiDefinition("atm", "atm-2.png", 14, 15, HasTag("amenity", "atm").or(HasTag("amenity", "bank").and(HasTag("atm")).and(NotHasTag("atm", "no")))),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("bank", "bank.png", 14, 15, HasTag("amenity", "bank")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("cinema", "cinema.png", 14, 15, HasTag("amenity", "cinema")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("clinic", "firstaid.png", 14, 15, HasTag("amenity", "clinic")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("embassy", "embassy.png", 14, 15, HasTag("amenity", "embassy")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("firestation", "firemen.png", 14, 15, HasTag("amenity", "firestation")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("fuel", "fillingstation.png", 14, 15, HasTag("amenity", "fuel")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("hospital", "hospital-building.png", 14, 15, HasTag("amenity", "hospital")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("library", "library.png", 14, 15, HasTag("amenity", "library")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("musicschool", "musicschool.png", 14, 15, HasTag("amenity", "music_school")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("parking", "parkinggarage.png", 14, 15, HasTag("amenity", "parking")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("pharmacy", "medicine.png", 14, 15, HasTag("amenity", "pharmacy")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("police", "police.png", 14, 15, HasTag("amenity", "police")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("postbox", "postal2.png", 15, 15, HasTag("amenity", "post_box")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("postoffice", "postal.png", 14, 15, HasTag("amenity", "post_office")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("school-college", "", 14, 15, HasTag("amenity", "school", "college")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("taxi", "taxi.png", 14, 15, HasTag("amenity", "taxi")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("theatre", "theater.png", 14, 15, HasTag("amenity", "theatre")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("university", "university.png", 14, 15, HasTag("amenity", "university")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("cemetery", "cemetary.png", 14, 15, HasTag("landuse", "cemetery").and(NotHasTag("animal"))),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("busstop", "busstop.png", 15, 15, HasTag("highway", "bus_stop"))
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )
    },

    new PoiGroupBuilder("shops", false) {

      //      PoiDefinition("beauty", "beautysalon.png", 14, 15, HasTag("shop", "beauty")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("books-stationary", "library.png", 14, 15, TagContains("shop", "books", "stationary")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("car", "car.png", 14, 15, HasTag("shop", "car")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("chemist", "drugstore.png", 14, 15, HasTag("shop", "chemist")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("clothes", "clothers_female.png", 14, 15, HasTag("shop", "clothes")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("copyshop", "letter_c.png", 14, 15, HasTag("shop", "copyshop")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("cosmetics", "perfumery.png", 14, 15, HasTag("shop", "cosmetics")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("departmentstore", "departmentstore.png", 14, 15, HasTag("shop", "department_store")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("diy-hardware", "tools.png", 14, 15, TagContains("shop", "doityourself", "hardware")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("garden-centre", "flowers-1.png", 14, 15, HasTag("shop", "garden_centre")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("general", "letter_g.png", 14, 15, HasTag("shop", "general")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("gift", "gifts.png", 14, 15, HasTag("shop", "gift")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("hairdresser", "barber.png", 14, 15, HasTag("shop", "hairdresser")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("jewelry", "jewelry.png", 14, 15, TagContains("shop", "jewelry", "jewellery")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("kiosk", "kiosk.png", 14, 15, HasTag("shop", "kiosk")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("leather", "bags.png", 14, 15, HasTag("shop", "leather")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("marketplace", "market.png", 14, 15, HasTag("amenity", "marketplace")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("musical-instrument", "music_rock.png", 14, 15, HasTag("shop", "musical_instrument")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("optician", "glasses.png", 14, 15, HasTag("shop", "optician")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("pets", "pets.png", 14, 15, HasTag("shop", "pet")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("phone", "phones.png", 14, 15, HasTag("shop", "mobile_phone")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("photo", "photo.png", 14, 15, HasTag("shop", "photo")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("shoes", "highhills.png", 14, 15, HasTag("shop", "shoes")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("shoppingcentre", "mall.png", 14, 15, HasTag("shop", "mall")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("textiles", "textiles.png", 14, 15, HasTag("shop", "textiles")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("toys", "toys.png", 14, 15, HasTag("shop", "toys")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("travelagency", "travel_agency.png", 14, 15, HasTag("shop", "travel_agency"))
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )
    },

    new PoiGroupBuilder("foodshops", false) {

      //      PoiDefinition("alcohol", "liquor.png", 14, 15, HasTag("shop", "alcohol")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("bakery", "bread.png", 14, 15, HasTag("shop", "bakery")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("beverages", "bar_coktail.png", 14, 15, HasTag("shop", "beverages")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("butcher", "butcher-2.png", 14, 15, HasTag("shop", "butcher")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("cheese", "cheese.png", 14, 15, HasTag("shop", "cheese")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("chocolate", "candy.png", 14, 15, HasTag("shop", "chocolate")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("confectionery", "candy.png", 14, 15, HasTag("shop", "confectionery")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("coffee", "coffee.png", 11, 11, HasTag("shop", "coffee")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("dairy", "milk_and_cookies.png", 14, 15, HasTag("shop", "dairy")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("deli", "patisserie.png", 14, 15, HasTag("shop", "deli")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("grocery", "grocery.png", 14, 15, HasTag("shop", "grocery")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("organic", "restaurant_vegetarian.png", 14, 15, HasTag("shop", "organic")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("seafood", "restaurant_fish.png", 14, 15, HasTag("shop", "seafood")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("supermarket", "supermarket.png", 14, 15, HasTag("shop", "supermarket")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("wine", "winebar.png", 14, 15, HasTag("shop", "wine"))
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )
    },

    new PoiGroupBuilder("sports", false) {

      //      PoiDefinition("american-football", "usfootball.png", 15, 15, HasTag("sport", "american_football")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("baseball", "baseball.png", 15, 15, HasTag("sport", "baseball")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("basketball", "basketball.png", 15, 15, HasTag("sport", "basketball")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("cycling", "cycling.png", 15, 15, HasTag("sport", "cycling")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("gymnastics", "gymnastics.png", 15, 15, HasTag("sport", "gymnastics")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("golf", "golfing.png", 15, 15, HasTag("leisure", "golf_course").or(HasTag("sport", "golf"))),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("hockey", "hockey.png", 15, 15, HasTag("sport", "hockey").or(HasTag("sport", "field_hockey"))),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("horseracing", "horseriding.png", 15, 15, HasTag("sport", "horse_racing").or(HasTag("sport", "equestrian"))),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("icehockey", "icehockey.png", 15, 15, HasTag("sport", "ice_hockey").or(HasTag("leisure", "ice_rink"))),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("soccer", "soccer.png", 15, 15, HasTag("sport", "soccer")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("sportscentre", "indoor-arena.png", 15, 15, HasTag("leisure", "sports_centre")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("surfing", "surfing.png", 15, 15, HasTag("sport", "surfing")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("swimming", "swimming.png", 15, 15, HasTag("sport", "swimming")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("tennis", "tennis.png", 15, 15, HasTag("sport", "tennis")),
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )

      //      PoiDefinition("volleyball", "volleyball.png", 15, 15, HasTag("sport", "volleybal"))
      poi("", "", 11, 11,
        Seq("" -> ""),
        { tags =>
          tags.has("", "")
        }
      )
    }
  )
}
