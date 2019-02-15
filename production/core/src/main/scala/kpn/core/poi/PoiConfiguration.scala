package kpn.core.poi

import kpn.core.poi.tags.HasTag
import kpn.core.poi.tags.NotHasTag
import kpn.core.poi.tags.NotTagContains
import kpn.core.poi.tags.TagContains

object PoiConfiguration {

  def main(args: Array[String]): Unit = {
    poiDefinitionGroups.flatMap(_.definitions).foreach { poiDefinition =>
      println(s"${poiDefinition.name}\t${poiDefinition.icon}")
    }
  }

  def tagKeys(poiName: String): Seq[String] = {
    poiDefinition(poiName).toSeq.flatMap(_.expression.tagKeys)
  }

  private def poiDefinition(poiName: String): Option[PoiDefinition] = {
    poiDefinitionGroups.flatMap(_.definitions).find(_.name == poiName)
  }


  val poiDefinitionGroups: Seq[PoiDefinitionGroup] = Seq(
    PoiDefinitionGroup(
      "amenity",
      PoiDefinition("atm", "atm-2.png", 12, HasTag("amenity", "atm")),
      PoiDefinition("atm", "atm-2.png", 12, HasTag("amenity", "bank").and(HasTag("atm")).and(NotHasTag("atm", "no"))),
      PoiDefinition("bank", "bank.png", 12, HasTag("amenity", "bank")),
      PoiDefinition("bench", "bench.png", 12, HasTag("amenity", "bench")),
      PoiDefinition("bicycle_parking", "parking_bicycle-2.png", 12, HasTag("amenity", "bicycle_parking")),
      PoiDefinition("bicycle_rental", "cycling.png", 12, HasTag("amenity")),
      PoiDefinition("cinema", "cinema.png", 12, HasTag("amenity", "cinema")),
      PoiDefinition("clinic", "firstaid.png", 12, HasTag("amenity", "clinic")),
      PoiDefinition("embassy", "embassy.png", 12, HasTag("amenity", "embassy")),
      PoiDefinition("firestation", "firemen.png", 12, HasTag("amenity", "firestation")),
      PoiDefinition("fuel", "fillingstation.png", 12, HasTag("amenity", "fuel")),
      PoiDefinition("hospital", "hospital-building.png", 12, HasTag("amenity", "hospital")),
      PoiDefinition("library", "library.png", 12, HasTag("amenity", "library")),
      PoiDefinition("music_school", "musicschool.png", 12, HasTag("amenity", "music_school")),
      PoiDefinition("parking", "parkinggarage.png", 12, HasTag("amenity", "parking")),
      PoiDefinition("pharmacy", "medicine.png", 12, HasTag("amenity", "pharmacy")),
      PoiDefinition("police", "police.png", 12, HasTag("amenity", "police")),
      PoiDefinition("post_box", "postal2.png", 12, HasTag("amenity", "post_box")),
      PoiDefinition("post_office", "postal.png", 12, HasTag("amenity", "post_office")),
      PoiDefinition("school_college", "", 12, HasTag("amenity", "school", "college")),
      PoiDefinition("taxi", "taxi.png", 12, HasTag("amenity", "taxi")),
      PoiDefinition("theatre", "theater.png", 12, HasTag("amenity", "theatre")),
      PoiDefinition("toilets", "toilets.png", 12, HasTag("amenity", "toilets")),
      PoiDefinition("university", "university.png", 12, HasTag("amenity", "university")),

      PoiDefinition("place_of_worship", "church-2.png", 12, HasTag("amenity", "place_of_worship").and(NotTagContains("religion", "christian", "muslim", "buddhist", "hindu", "jewish"))),
      PoiDefinition("church", "chapel-2.png", 12, HasTag("amenity", "place_of_worship").and(TagContains("religion", "christian"))),
      PoiDefinition("mosque", "mosquee.png", 12, HasTag("amenity", "place_of_worship").and(TagContains("religion", "muslim"))),
      PoiDefinition("buddhist_temple", "bouddha.png", 12, HasTag("amenity", "place_of_worship").and(TagContains("religion", "buddhist"))),
      PoiDefinition("hindu_temple", "templehindu.png", 12, HasTag("amenity", "place_of_worship").and(TagContains("religion", "hindu"))),
      PoiDefinition("synagogue", "synagogue-2.png", 12, HasTag("amenity", "place_of_worship").and(TagContains("religion", "jewish"))),

      PoiDefinition("cemetery", "cemetary.png", 12, HasTag("landuse", "cemetery").and(NotHasTag("animal")))
    ),
    PoiDefinitionGroup(
      "tourism",
      PoiDefinition("arts_centre", "letter_a.png", 12, HasTag("amenity", "arts_centre")),
      PoiDefinition("artwork", "artwork.png", 12, HasTag("tourism=artwork").and(NotTagContains("artwork_type", "statue"))),
      PoiDefinition("attraction", "star.png", 12, HasTag("tourism", "attraction")),
      PoiDefinition("casino", "casino.png", 12, HasTag("leisure", "casino")),
      PoiDefinition("casino", "casino.png", 12, HasTag("amenity", "casino")),
      PoiDefinition("gallery", "artgallery.png", 12, HasTag("tourism", "gallery")),
      PoiDefinition("heritage", "worldheritagesite.png", 12, HasTag("heritage")),

      PoiDefinition("historic", "star-3.png", 12, HasTag("historic").and(NotTagContains("historic", "memorial", "monument", "statue", "castle"))),
      PoiDefinition("castle", "castle-2.png", 12, HasTag("historic", "castle")),
      PoiDefinition("monument_memorial", "memorial.png", 12, TagContains("historic", "monument", "memorial")),
      PoiDefinition("statue", "statue-2.png", 12, HasTag("historic", "statue")),

      PoiDefinition("information", "information.png", 12, HasTag("tourism", "information")), // no rel
      PoiDefinition("monumental_tree", "tree.png", 12, HasTag("natural", "tree").and(HasTag("monument", "yes"))),
      PoiDefinition("museum", "museum_art.png", 12, HasTag("tourism", "museum")),
      PoiDefinition("picnic", "picnic-2.png", 12, HasTag("tourism", "picnic_site")),
      PoiDefinition("picnic", "picnic-2.png", 12, HasTag("leisure", "picnic_table")),
      PoiDefinition("theme_park", "themepark.png", 12, HasTag("tourism", "theme_park")),
      PoiDefinition("viewpoint", "viewpoint.png", 12, HasTag("tourism", "viewpoint")),
      PoiDefinition("vineyard", "vineyard.png", 12, HasTag("landuse", "vineyard")),
      PoiDefinition("windmill", "windmill-2.png", 12, HasTag("man_made", "windmill")),
      PoiDefinition("watermill", "watermill-2.png", 12, HasTag("man_made", "watermill")),
      PoiDefinition("zoo", "zoo.png", 12, HasTag("tourism", "zoo")),
      PoiDefinition("tourism", "sight-2.png", 12, HasTag("tourism", "yes")) // TODO review instances of this poi
    ),
    PoiDefinitionGroup(
      "places_to_stay",
      PoiDefinition("alpine_hut", "alpinehut.png", 12, HasTag("tourism", "alpine_hut")),
      PoiDefinition("apartment", "apartment-3.png", 12, HasTag("tourism", "apartment")),
      PoiDefinition("camp_site", "camping-2.png", 12, HasTag("tourism", "camp_site").and(NotHasTag("backcountry", "yes"))),
      PoiDefinition("chalet", "letter_c.png", 12, HasTag("tourism", "chalet")),
      PoiDefinition("guest_house", "bed_breakfast.png", 12, TagContains("tourism", "guest_house", "bed_and_breakfast")),
      PoiDefinition("hostel", "hostel_0star.png", 12, HasTag("tourism", "hostel")),
      PoiDefinition("hotel", "hotel_0star.png", 12, HasTag("tourism", "hotel")),
      PoiDefinition("motel", "motel-2.png", 12, HasTag("tourism", "motel")),
      PoiDefinition("spa", "spa -> ", 12, HasTag("leisure", "spa")),
      PoiDefinition("sauna", "sauna.png", 12, HasTag("leisure", "sauna"))
    ),
    PoiDefinitionGroup(
      "sport",
      PoiDefinition("american_football", "usfootball.png", 12, HasTag("sport", "american_football")),
      PoiDefinition("baseball", "baseball.png", 12, HasTag("sport", "baseball")),
      PoiDefinition("basketball", "basketball.png", 12, HasTag("sport", "basketball")),
      PoiDefinition("cycling", "cycling.png", 12, HasTag("sport", "cycling")),
      PoiDefinition("gymnastics", "gymnastics.png", 12, HasTag("sport", "gymnastics")),
      PoiDefinition("golf", "golfing.png", 12, HasTag("leisure", "golf_course")),
      PoiDefinition("golf", "golfing.png", 12, HasTag("sport", "golf")),
      PoiDefinition("hockey", "hockey.png", 12, HasTag("sport", "hockey")),
      PoiDefinition("hockey", "hockey.png", 12, HasTag("sport", "field_hockey")),
      PoiDefinition("horse_racing", "horseriding.png", 12, HasTag("sport", "horse_racing")),
      PoiDefinition("horse_racing", "horseriding.png", 12, HasTag("sport", "equestrian")),
      PoiDefinition("ice_hockey", "icehockey.png", 12, HasTag("sport", "ice_hockey")),
      PoiDefinition("ice_hockey", "icehockey.png", 12, HasTag("leisure", "ice_rink")),
      PoiDefinition("soccer", "soccer.png", 12, HasTag("sport", "soccer")),
      PoiDefinition("sports_centre", "indoor-arena.png", 12, HasTag("leisure", "sports_centre")),
      PoiDefinition("surfing", "surfing.png", 12, HasTag("sport", "surfing")),
      PoiDefinition("swimming", "swimming.png", 12, HasTag("sport", "swimming")),
      PoiDefinition("tennis", "tennis.png", 12, HasTag("sport", "tennis")),
      PoiDefinition("volleyball", "volleyball.png", 12, HasTag("sport", "volleybal"))
    ),
    PoiDefinitionGroup(
      "shops",
      PoiDefinition("beauty", "beautysalon.png", 12, HasTag("shop", "beauty")),
      PoiDefinition("bicycle", "bicycle_shop.png", 12, HasTag("shop", "bicycle")),
      PoiDefinition("books_stationary", "library.png", 12, TagContains("shop", "books", "stationary")),
      PoiDefinition("car", "car.png", 12, HasTag("shop", "car")),
      PoiDefinition("chemist", "drugstore.png", 12, HasTag("shop", "chemist")),
      PoiDefinition("clothes", "clothers_female.png", 12, HasTag("shop", "clothes")),
      PoiDefinition("copyshop", "letter_c.png", 12, HasTag("shop", "copyshop")),
      PoiDefinition("cosmetics", "perfumery.png", 12, HasTag("shop", "cosmetics")),
      PoiDefinition("department_store", "departmentstore.png", 12, HasTag("shop", "department_store")),
      PoiDefinition("diy_hardware", "tools.png", 12, TagContains("shop", "doityourself", "hardware")),
      PoiDefinition("garden_centre", "flowers-1.png", 12, HasTag("shop", "garden_centre")),
      PoiDefinition("general", "letter_g.png", 12, HasTag("shop", "general")),
      PoiDefinition("gift", "gifts.png", 12, HasTag("shop", "gift")),
      PoiDefinition("hairdresser", "barber.png", 12, HasTag("shop", "hairdresser")),
      PoiDefinition("jewelry", "jewelry.png", 12, TagContains("shop", "jewelry", "jewellery")),
      PoiDefinition("kiosk", "kiosk.png", 12, HasTag("shop", "kiosk")),
      PoiDefinition("leather", "bags.png", 12, HasTag("shop", "leather")),
      PoiDefinition("marketplace", "market.png", 12, HasTag("amenity", "marketplace")),
      PoiDefinition("musical_instrument", "music_rock.png", 12, HasTag("shop", "musical_instrument")),
      PoiDefinition("optician", "glasses.png", 12, HasTag("shop", "optician")),
      PoiDefinition("pets", "pets.png", 12, HasTag("shop", "pets")),
      PoiDefinition("phone", "phones.png", 12, HasTag("shop", "mobile_phone")),
      PoiDefinition("photo", "photo.png", 12, HasTag("shop", "photo")),
      PoiDefinition("shoes", "highhills.png", 12, HasTag("shop", "shoes")),
      PoiDefinition("shopping_centre", "mall.png", 12, HasTag("shop", "mall")),
      PoiDefinition("textiles", "textiles.png", 12, HasTag("shop", "textiles")),
      PoiDefinition("toys", "toys.png", 12, HasTag("shop", "toys"))
    ),
    PoiDefinitionGroup(
      "food_shops",
      PoiDefinition("alcohol", "liquor.png", 12, HasTag("shop", "alcohol")),
      PoiDefinition("bakery", "bread.png", 12, HasTag("shop", "bakery")),
      PoiDefinition("beverages", "bar_coktail.png", 12, HasTag("shop", "beverages")),
      PoiDefinition("butcher", "butcher-2.png", 12, HasTag("shop", "butcher")),
      PoiDefinition("cheese", "cheese.png", 12, HasTag("shop", "cheese")),
      PoiDefinition("chocolate", "candy.png", 12, HasTag("shop", "chocolate")),
      PoiDefinition("confectionery", "candy.png", 12, HasTag("shop", "confectionery")),
      PoiDefinition("coffee", "coffee.png", 12, HasTag("shop", "coffee")),
      PoiDefinition("dairy", "milk_and_cookies.png", 12, HasTag("shop", "dairy")),
      PoiDefinition("deli", "patisserie.png", 12, HasTag("shop", "deli")),
      PoiDefinition("drinking_water", "drinkingwater.png", 12, HasTag("amenity", "drinking_water")),
      PoiDefinition("grocery", "grocery.png", 12, HasTag("shop", "grocery")),
      PoiDefinition("organic", "restaurant_vegetarian.png", 12, HasTag("shop", "organic")),
      PoiDefinition("seafood", "restaurant_fish.png", 12, HasTag("shop", "seafood")),
      PoiDefinition("supermarket", "supermarket.png", 12, HasTag("shop", "supermarket")),
      PoiDefinition("wine", "winebar.png", 12, HasTag("shop", "wine"))
    ),
    PoiDefinitionGroup(
      "restaurants",
      PoiDefinition("bar", "bar.png", 12, HasTag("amenity", "bar")),
      PoiDefinition("bbq", "letter_b.png", 12, HasTag("amenity", "bbq")),
      PoiDefinition("biergarten", "beergarden.png", 12, HasTag("amenity", "biergarten")),
      PoiDefinition("cafe", "cafetaria.png", 12, HasTag("amenity", "cafe")),
      PoiDefinition("fast_food", "fastfood.png", 12, HasTag("amenity", "fast_food")),
      PoiDefinition("food_court", "letter_f.png", 12, HasTag("amenity", "food_court")),
      PoiDefinition("ice_cream", "icecream.png", 12, HasTag("amenity", "ice_cream")),
      PoiDefinition("ice_cream", "icecream.png", 12, HasTag("cuisine", "ice_cream")),
      PoiDefinition("pub", "pub.png", 12, HasTag("amenity", "pub")),
      PoiDefinition("restaurant", "restaurant.png", 12, HasTag("amenity", "restaurant"))
    ),
    PoiDefinitionGroup(
      "various",
      PoiDefinition("bus_stop", "busstop.png", 12, HasTag("highway", "bus_stop")),
      PoiDefinition("ebike_charging", "e-bike-charging.png", 12, HasTag("amenity", "charging_station").and(HasTag("bicycle", "yes"))),
      PoiDefinition("travel_agency", "travel_agency.png", 12, HasTag("shop", "travel_agency")),
      PoiDefinition("defibrillator", "aed-2.png", 12, HasTag("emergency", "defibrillator"))
    )
  )
}
