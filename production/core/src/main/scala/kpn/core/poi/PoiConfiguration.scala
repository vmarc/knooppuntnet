package kpn.core.poi

import kpn.core.poi.tags.HasTag
import kpn.core.poi.tags.NotHasTag
import kpn.core.poi.tags.NotTagContains
import kpn.core.poi.tags.TagContains
import kpn.shared.tiles.ClientPoiConfiguration

object PoiConfiguration {

  val instance = PoiConfiguration(
    PoiGroupDefinition(
      "amenity",
      true,
      PoiDefinition("atm", "atm-2.png", 11, 11, HasTag("amenity", "atm")),
      PoiDefinition("atm", "atm-2.png", 11, 11, HasTag("amenity", "bank").and(HasTag("atm")).and(NotHasTag("atm", "no"))),
      PoiDefinition("bank", "bank.png", 11, 11, HasTag("amenity", "bank")),
      PoiDefinition("bench", "bench.png", 11, 11, HasTag("amenity", "bench")),
      PoiDefinition("bicycle_parking", "parking_bicycle-2.png", 11, 11, HasTag("amenity", "bicycle_parking")),
      PoiDefinition("bicycle_rental", "cycling.png", 11, 11, HasTag("amenity")),
      PoiDefinition("cinema", "cinema.png", 11, 11, HasTag("amenity", "cinema")),
      PoiDefinition("clinic", "firstaid.png", 11, 11, HasTag("amenity", "clinic")),
      PoiDefinition("embassy", "embassy.png", 11, 11, HasTag("amenity", "embassy")),
      PoiDefinition("firestation", "firemen.png", 11, 11, HasTag("amenity", "firestation")),
      PoiDefinition("fuel", "fillingstation.png", 11, 11, HasTag("amenity", "fuel")),
      PoiDefinition("hospital", "hospital-building.png", 11, 11, HasTag("amenity", "hospital")),
      PoiDefinition("library", "library.png", 11, 11, HasTag("amenity", "library")),
      PoiDefinition("music_school", "musicschool.png", 11, 11, HasTag("amenity", "music_school")),
      PoiDefinition("parking", "parkinggarage.png", 11, 11, HasTag("amenity", "parking")),
      PoiDefinition("pharmacy", "medicine.png", 11, 11, HasTag("amenity", "pharmacy")),
      PoiDefinition("police", "police.png", 11, 11, HasTag("amenity", "police")),
      PoiDefinition("post_box", "postal2.png", 11, 11, HasTag("amenity", "post_box")),
      PoiDefinition("post_office", "postal.png", 11, 11, HasTag("amenity", "post_office")),
      PoiDefinition("school_college", "", 11, 11, HasTag("amenity", "school", "college")),
      PoiDefinition("taxi", "taxi.png", 11, 11, HasTag("amenity", "taxi")),
      PoiDefinition("theatre", "theater.png", 11, 11, HasTag("amenity", "theatre")),
      PoiDefinition("toilets", "toilets.png", 11, 11, HasTag("amenity", "toilets")),
      PoiDefinition("university", "university.png", 11, 11, HasTag("amenity", "university")),

      PoiDefinition("place_of_worship", "church-2.png", 11, 11, HasTag("amenity", "place_of_worship").and(NotTagContains("religion", "christian", "muslim", "buddhist", "hindu", "jewish"))),
      PoiDefinition("church", "chapel-2.png", 11, 11, HasTag("amenity", "place_of_worship").and(TagContains("religion", "christian"))),
      PoiDefinition("mosque", "mosquee.png", 11, 11, HasTag("amenity", "place_of_worship").and(TagContains("religion", "muslim"))),
      PoiDefinition("buddhist_temple", "bouddha.png", 11, 11, HasTag("amenity", "place_of_worship").and(TagContains("religion", "buddhist"))),
      PoiDefinition("hindu_temple", "templehindu.png", 11, 11, HasTag("amenity", "place_of_worship").and(TagContains("religion", "hindu"))),
      PoiDefinition("synagogue", "synagogue-2.png", 11, 11, HasTag("amenity", "place_of_worship").and(TagContains("religion", "jewish"))),

      PoiDefinition("cemetery", "cemetary.png", 11, 11, HasTag("landuse", "cemetery").and(NotHasTag("animal")))
    ),
    PoiGroupDefinition(
      "tourism",
      true,
      PoiDefinition("arts_centre", "letter_a.png", 11, 11, HasTag("amenity", "arts_centre")),
      PoiDefinition("artwork", "artwork.png", 11, 11, HasTag("tourism=artwork").and(NotTagContains("artwork_type", "statue"))),
      PoiDefinition("attraction", "star.png", 11, 11, HasTag("tourism", "attraction")),
      PoiDefinition("casino", "casino.png", 11, 11, HasTag("leisure", "casino")),
      PoiDefinition("casino", "casino.png", 11, 11, HasTag("amenity", "casino")),
      PoiDefinition("gallery", "artgallery.png", 11, 11, HasTag("tourism", "gallery")),
      PoiDefinition("heritage", "worldheritagesite.png", 11, 11, HasTag("heritage")),

      PoiDefinition("historic", "star-3.png", 11, 11, HasTag("historic").and(NotTagContains("historic", "memorial", "monument", "statue", "castle"))),
      PoiDefinition("castle", "castle-2.png", 11, 11, HasTag("historic", "castle")),
      PoiDefinition("monument_memorial", "memorial.png", 11, 11, TagContains("historic", "monument", "memorial")),
      PoiDefinition("statue", "statue-2.png", 11, 11, HasTag("historic", "statue")),

      PoiDefinition("information", "information.png", 11, 11, HasTag("tourism", "information")), // no rel
      PoiDefinition("monumental_tree", "tree.png", 11, 11, HasTag("natural", "tree").and(HasTag("monument", "yes"))),
      PoiDefinition("museum", "museum_art.png", 11, 11, HasTag("tourism", "museum")),
      PoiDefinition("picnic", "picnic-2.png", 11, 11, HasTag("tourism", "picnic_site")),
      PoiDefinition("picnic", "picnic-2.png", 11, 11, HasTag("leisure", "picnic_table")),
      PoiDefinition("theme_park", "themepark.png", 11, 11, HasTag("tourism", "theme_park")),
      PoiDefinition("viewpoint", "viewpoint.png", 11, 11, HasTag("tourism", "viewpoint")),
      PoiDefinition("vineyard", "vineyard.png", 11, 11, HasTag("landuse", "vineyard")),
      PoiDefinition("windmill", "windmill-2.png", 11, 11, HasTag("man_made", "windmill")),
      PoiDefinition("watermill", "watermill-2.png", 11, 11, HasTag("man_made", "watermill")),
      PoiDefinition("zoo", "zoo.png", 11, 11, HasTag("tourism", "zoo")),
      PoiDefinition("tourism", "sight-2.png", 11, 11, HasTag("tourism", "yes")) // TODO review instances of this poi
    ),
    PoiGroupDefinition(
      "places_to_stay",
      true,
      PoiDefinition("alpine_hut", "alpinehut.png", 11, 11, HasTag("tourism", "alpine_hut")),
      PoiDefinition("apartment", "apartment-3.png", 11, 11, HasTag("tourism", "apartment")),
      PoiDefinition("camp_site", "camping-2.png", 11, 11, HasTag("tourism", "camp_site").and(NotHasTag("backcountry", "yes"))),
      PoiDefinition("chalet", "letter_c.png", 11, 11, HasTag("tourism", "chalet")),
      PoiDefinition("guest_house", "bed_breakfast.png", 11, 11, TagContains("tourism", "guest_house", "bed_and_breakfast")),
      PoiDefinition("hostel", "hostel_0star.png", 11, 11, HasTag("tourism", "hostel")),
      PoiDefinition("hotel", "hotel_0star.png", 11, 11, HasTag("tourism", "hotel")),
      PoiDefinition("motel", "motel-2.png", 11, 11, HasTag("tourism", "motel")),
      PoiDefinition("spa", "spa -> ", 11, 11, HasTag("leisure", "spa")),
      PoiDefinition("sauna", "sauna.png", 11, 11, HasTag("leisure", "sauna"))
    ),
    PoiGroupDefinition(
      "sport",
      false,
      PoiDefinition("american_football", "usfootball.png", 11, 11, HasTag("sport", "american_football")),
      PoiDefinition("baseball", "baseball.png", 11, 11, HasTag("sport", "baseball")),
      PoiDefinition("basketball", "basketball.png", 11, 11, HasTag("sport", "basketball")),
      PoiDefinition("cycling", "cycling.png", 11, 11, HasTag("sport", "cycling")),
      PoiDefinition("gymnastics", "gymnastics.png", 11, 11, HasTag("sport", "gymnastics")),
      PoiDefinition("golf", "golfing.png", 11, 11, HasTag("leisure", "golf_course")),
      PoiDefinition("golf", "golfing.png", 11, 11, HasTag("sport", "golf")),
      PoiDefinition("hockey", "hockey.png", 11, 11, HasTag("sport", "hockey")),
      PoiDefinition("hockey", "hockey.png", 11, 11, HasTag("sport", "field_hockey")),
      PoiDefinition("horse_racing", "horseriding.png", 11, 11, HasTag("sport", "horse_racing")),
      PoiDefinition("horse_racing", "horseriding.png", 11, 11, HasTag("sport", "equestrian")),
      PoiDefinition("ice_hockey", "icehockey.png", 11, 11, HasTag("sport", "ice_hockey")),
      PoiDefinition("ice_hockey", "icehockey.png", 11, 11, HasTag("leisure", "ice_rink")),
      PoiDefinition("soccer", "soccer.png", 11, 11, HasTag("sport", "soccer")),
      PoiDefinition("sports_centre", "indoor-arena.png", 11, 11, HasTag("leisure", "sports_centre")),
      PoiDefinition("surfing", "surfing.png", 11, 11, HasTag("sport", "surfing")),
      PoiDefinition("swimming", "swimming.png", 11, 11, HasTag("sport", "swimming")),
      PoiDefinition("tennis", "tennis.png", 11, 11, HasTag("sport", "tennis")),
      PoiDefinition("volleyball", "volleyball.png", 11, 11, HasTag("sport", "volleybal"))
    ),
    PoiGroupDefinition(
      "shops",
      false,
      PoiDefinition("beauty", "beautysalon.png", 11, 11, HasTag("shop", "beauty")),
      PoiDefinition("bicycle", "bicycle_shop.png", 11, 11, HasTag("shop", "bicycle")),
      PoiDefinition("books_stationary", "library.png", 11, 11, TagContains("shop", "books", "stationary")),
      PoiDefinition("car", "car.png", 11, 11, HasTag("shop", "car")),
      PoiDefinition("chemist", "drugstore.png", 11, 11, HasTag("shop", "chemist")),
      PoiDefinition("clothes", "clothers_female.png", 11, 11, HasTag("shop", "clothes")),
      PoiDefinition("copyshop", "letter_c.png", 11, 11, HasTag("shop", "copyshop")),
      PoiDefinition("cosmetics", "perfumery.png", 11, 11, HasTag("shop", "cosmetics")),
      PoiDefinition("department_store", "departmentstore.png", 11, 11, HasTag("shop", "department_store")),
      PoiDefinition("diy_hardware", "tools.png", 11, 11, TagContains("shop", "doityourself", "hardware")),
      PoiDefinition("garden_centre", "flowers-1.png", 11, 11, HasTag("shop", "garden_centre")),
      PoiDefinition("general", "letter_g.png", 11, 11, HasTag("shop", "general")),
      PoiDefinition("gift", "gifts.png", 11, 11, HasTag("shop", "gift")),
      PoiDefinition("hairdresser", "barber.png", 11, 11, HasTag("shop", "hairdresser")),
      PoiDefinition("jewelry", "jewelry.png", 11, 11, TagContains("shop", "jewelry", "jewellery")),
      PoiDefinition("kiosk", "kiosk.png", 11, 11, HasTag("shop", "kiosk")),
      PoiDefinition("leather", "bags.png", 11, 11, HasTag("shop", "leather")),
      PoiDefinition("marketplace", "market.png", 11, 11, HasTag("amenity", "marketplace")),
      PoiDefinition("musical_instrument", "music_rock.png", 11, 11, HasTag("shop", "musical_instrument")),
      PoiDefinition("optician", "glasses.png", 11, 11, HasTag("shop", "optician")),
      PoiDefinition("pets", "pets.png", 11, 11, HasTag("shop", "pets")),
      PoiDefinition("phone", "phones.png", 11, 11, HasTag("shop", "mobile_phone")),
      PoiDefinition("photo", "photo.png", 11, 11, HasTag("shop", "photo")),
      PoiDefinition("shoes", "highhills.png", 11, 11, HasTag("shop", "shoes")),
      PoiDefinition("shopping_centre", "mall.png", 11, 11, HasTag("shop", "mall")),
      PoiDefinition("textiles", "textiles.png", 11, 11, HasTag("shop", "textiles")),
      PoiDefinition("toys", "toys.png", 11, 11, HasTag("shop", "toys"))
    ),
    PoiGroupDefinition(
      "food_shops",
      false,
      PoiDefinition("alcohol", "liquor.png", 11, 11, HasTag("shop", "alcohol")),
      PoiDefinition("bakery", "bread.png", 11, 11, HasTag("shop", "bakery")),
      PoiDefinition("beverages", "bar_coktail.png", 11, 11, HasTag("shop", "beverages")),
      PoiDefinition("butcher", "butcher-2.png", 11, 11, HasTag("shop", "butcher")),
      PoiDefinition("cheese", "cheese.png", 11, 11, HasTag("shop", "cheese")),
      PoiDefinition("chocolate", "candy.png", 11, 11, HasTag("shop", "chocolate")),
      PoiDefinition("confectionery", "candy.png", 11, 11, HasTag("shop", "confectionery")),
      PoiDefinition("coffee", "coffee.png", 11, 11, HasTag("shop", "coffee")),
      PoiDefinition("dairy", "milk_and_cookies.png", 11, 11, HasTag("shop", "dairy")),
      PoiDefinition("deli", "patisserie.png", 11, 11, HasTag("shop", "deli")),
      PoiDefinition("drinking_water", "drinkingwater.png", 11, 11, HasTag("amenity", "drinking_water")),
      PoiDefinition("grocery", "grocery.png", 11, 11, HasTag("shop", "grocery")),
      PoiDefinition("organic", "restaurant_vegetarian.png", 11, 11, HasTag("shop", "organic")),
      PoiDefinition("seafood", "restaurant_fish.png", 11, 11, HasTag("shop", "seafood")),
      PoiDefinition("supermarket", "supermarket.png", 11, 11, HasTag("shop", "supermarket")),
      PoiDefinition("wine", "winebar.png", 11, 11, HasTag("shop", "wine"))
    ),
    PoiGroupDefinition(
      "restaurants",
      true,
      PoiDefinition("bar", "bar.png", 11, 11, HasTag("amenity", "bar")),
      PoiDefinition("bbq", "letter_b.png", 11, 11, HasTag("amenity", "bbq")),
      PoiDefinition("biergarten", "beergarden.png", 11, 11, HasTag("amenity", "biergarten")),
      PoiDefinition("cafe", "cafetaria.png", 11, 11, HasTag("amenity", "cafe")),
      PoiDefinition("fast_food", "fastfood.png", 11, 11, HasTag("amenity", "fast_food")),
      PoiDefinition("food_court", "letter_f.png", 11, 11, HasTag("amenity", "food_court")),
      PoiDefinition("ice_cream", "icecream.png", 11, 11, HasTag("amenity", "ice_cream")),
      PoiDefinition("ice_cream", "icecream.png", 11, 11, HasTag("cuisine", "ice_cream")),
      PoiDefinition("pub", "pub.png", 11, 11, HasTag("amenity", "pub")),
      PoiDefinition("restaurant", "restaurant.png", 11, 11, HasTag("amenity", "restaurant"))
    ),
    PoiGroupDefinition(
      "various",
      true,
      PoiDefinition("bus_stop", "busstop.png", 11, 11, HasTag("highway", "bus_stop")),
      PoiDefinition("ebike_charging", "e-bike-charging.png", 11, 11, HasTag("amenity", "charging_station").and(HasTag("bicycle", "yes"))),
      PoiDefinition("travel_agency", "travel_agency.png", 11, 11, HasTag("shop", "travel_agency")),
      PoiDefinition("defibrillator", "aed-2.png", 11, 11, HasTag("emergency", "defibrillator"))
    )
  )
}

case class PoiConfiguration(groupDefinitions: PoiGroupDefinition*) {

  def tagKeys(poiName: String): Seq[String] = {
    poiDefinition(poiName).toSeq.flatMap(_.expression.tagKeys)
  }

  private def poiDefinition(poiName: String): Option[PoiDefinition] = {
    groupDefinitions.flatMap(_.definitions).find(_.name == poiName)
  }

  def toClient: ClientPoiConfiguration = {
    ClientPoiConfiguration(groupDefinitions.map(_.toClient))
  }

}

