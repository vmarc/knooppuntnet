package kpn.core.poi

import kpn.core.poi.tags.HasTag
import kpn.core.poi.tags.NotHasTag
import kpn.core.poi.tags.NotTagContains
import kpn.core.poi.tags.TagContains

object PoiConfiguration {

  def main(args: Array[String]): Unit = {
    poiDefinitionGroups.flatMap(_.definitions).foreach { poiDefinition =>
      println(s"${poiDefinition.layerName}\t${poiDefinition.icon}")
    }
  }

  val poiDefinitionGroups = Seq(
    PoiDefinitionGroup(
      "amenity",
      PoiDefinition("atm", "atm-2.png", HasTag("amenity", "atm")),
      PoiDefinition("atm", "atm-2.png", HasTag("amenity", "bank").and(HasTag("atm")).and(NotHasTag("atm", "no"))),
      PoiDefinition("bank", "bank.png", HasTag("amenity", "bank")),
      PoiDefinition("bench", "letter_b.png", HasTag("amenity", "bench")),
      PoiDefinition("bicycle_parking", "parking_bicycle-2.png", HasTag("amenity", "bicycle_parking")),
      PoiDefinition("bicycle_rental", "cycling.png", HasTag("amenity")),
      PoiDefinition("cinema", "cinema.png", HasTag("amenity", "cinema")),
      PoiDefinition("clinic", "firstaid.png", HasTag("amenity", "clinic")),
      PoiDefinition("embassy", "embassy.png", HasTag("amenity", "embassy")),
      PoiDefinition("firestation", "firemen.png", HasTag("amenity", "firestation")),
      PoiDefinition("fuel", "fillingstation.png", HasTag("amenity", "fuel")),
      PoiDefinition("hospital", "hospital-building.png", HasTag("amenity", "hospital")),
      PoiDefinition("library", "library.png", HasTag("amenity", "library")),
      PoiDefinition("music_school", "musicschool.png", HasTag("amenity", "music_school")),
      PoiDefinition("parking", "parkinggarage.png", HasTag("amenity", "parking")),
      PoiDefinition("pharmacy", "medicine.png", HasTag("amenity", "pharmacy")),
      PoiDefinition("police", "police.png", HasTag("amenity", "police")),
      PoiDefinition("post_box", "postal2.png", HasTag("amenity", "post_box")),
      PoiDefinition("post_office", "postal.png", HasTag("amenity", "post_office")),
      PoiDefinition("school_college", "", HasTag("amenity", "school", "college")),
      PoiDefinition("taxi", "taxi.png", HasTag("amenity", "taxi")),
      PoiDefinition("theatre", "theater.png", HasTag("amenity", "theatre")),
      PoiDefinition("toilets", "toilets.png", HasTag("amenity", "toilets")),
      PoiDefinition("university", "university.png", HasTag("amenity", "university")),

      PoiDefinition("place_of_worship", "church-2.png", HasTag("amenity", "place_of_worship").and(NotTagContains("religion", "christian", "muslim", "buddhist", "hindu", "jewish"))),
      PoiDefinition("church", "chapel-2.png", HasTag("amenity", "place_of_worship").and(TagContains("religion", "christian"))),
      PoiDefinition("mosque", "mosquee.png", HasTag("amenity", "place_of_worship").and(TagContains("religion", "muslim"))),
      PoiDefinition("buddhist_temple", "bouddha.png", HasTag("amenity", "place_of_worship").and(TagContains("religion", "buddhist"))),
      PoiDefinition("hindu_temple", "templehindu.png", HasTag("amenity", "place_of_worship").and(TagContains("religion", "hindu"))),
      PoiDefinition("synagogue", "synagogue-2.png", HasTag("amenity", "place_of_worship").and(TagContains("religion", "jewish"))),

      PoiDefinition("cemetery", "cemetary.png", HasTag("landuse", "cemetery").and(NotHasTag("animal")))
    ),
    PoiDefinitionGroup(
      "tourism",
      PoiDefinition("arts_centre", "letter_a.png", HasTag("amenity", "arts_centre")),
      PoiDefinition("artwork", "artwork.png", HasTag("tourism=artwork").and(NotTagContains("artwork_type", "statue"))),
      PoiDefinition("attraction", "star.png", HasTag("tourism", "attraction")),
      PoiDefinition("casino", "casino.png", HasTag("leisure", "casino")),
      PoiDefinition("casino", "casino.png", HasTag("amenity", "casino")),
      PoiDefinition("gallery", "artgallery.png", HasTag("tourism", "gallery")),
      PoiDefinition("heritage", "worldheritagesite.png", HasTag("heritage")),

      PoiDefinition("historic", "star-3.png", HasTag("historic").and(NotTagContains("historic", "memorial", "monument", "statue", "castle"))),
      PoiDefinition("castle", "castle-2.png", HasTag("historic", "castle")),
      PoiDefinition("monument_memorial", "memorial.png", TagContains("historic", "monument", "memorial")),
      PoiDefinition("statue", "statue-2.png", HasTag("historic", "statue")),

      PoiDefinition("information", "information.png", HasTag("tourism", "information")), // no rel
      PoiDefinition("monumental_tree", "tree.png", HasTag("natural", "tree").and(HasTag("monument", "yes"))),
      PoiDefinition("museum", "museum_art.png", HasTag("tourism", "museum")),
      PoiDefinition("picnic", "picnic-2.png", HasTag("tourism", "picnic_site")),
      PoiDefinition("picnic", "picnic-2.png", HasTag("leisure", "picnic_table")),
      PoiDefinition("theme_park", "themepark.png", HasTag("tourism", "theme_park")),
      PoiDefinition("viewpoint", "viewpoint.png", HasTag("tourism", "viewpoint")),
      PoiDefinition("vineyard", "vineyard.png", HasTag("landuse", "vineyard")),
      PoiDefinition("windmill", "windmill-2.png", HasTag("man_made", "windmill")),
      PoiDefinition("watermill", "watermill-2.png", HasTag("man_made", "watermill")),
      PoiDefinition("zoo", "zoo.png", HasTag("tourism", "zoo")),
      PoiDefinition("tourism", "sight-2.png", HasTag("tourism", "yes")) // TODO review instances of this poi
    ),
    PoiDefinitionGroup(
      "places_to_stay",
      PoiDefinition("alpine_hut", "alpinehut.png", HasTag("tourism", "alpine_hut")),
      PoiDefinition("apartment", "apartment-3.png", HasTag("tourism", "apartment")),
      PoiDefinition("camp_site", "camping-2.png", HasTag("tourism", "camp_site").and(NotHasTag("backcountry", "yes"))),
      PoiDefinition("chalet", "letter_c.png", HasTag("tourism", "chalet")),
      PoiDefinition("guest_house", "bed_breakfast.png", TagContains("tourism", "guest_house", "bed_and_breakfast")),
      PoiDefinition("hostel", "hostel_0star.png", HasTag("tourism", "hostel")),
      PoiDefinition("hotel", "hotel_0star.png", HasTag("tourism", "hotel")),
      PoiDefinition("motel", "motel-2.png", HasTag("tourism", "motel")),
      PoiDefinition("spa", "spa -> ", HasTag("leisure", "spa")),
      PoiDefinition("sauna", "sauna.png", HasTag("leisure", "sauna"))
    ),
    PoiDefinitionGroup(
      "sport",
      PoiDefinition("american_football", "usfootball.png", HasTag("sport", "american_football")),
      PoiDefinition("baseball", "baseball.png", HasTag("sport", "baseball")),
      PoiDefinition("basketball", "basketball.png", HasTag("sport", "basketball")),
      PoiDefinition("cycling", "cycling.png", HasTag("sport", "cycling")),
      PoiDefinition("gymnastics", "gymnastics.png", HasTag("sport", "gymnastics")),
      PoiDefinition("golf", "golfing.png", HasTag("leisure", "golf_course")),
      PoiDefinition("golf", "golfing.png", HasTag("sport", "golf")),
      PoiDefinition("hockey", "hockey.png", HasTag("sport", "hockey")),
      PoiDefinition("hockey", "hockey.png", HasTag("sport", "field_hockey")),
      PoiDefinition("horse_racing", "horseriding.png", HasTag("sport", "horse_racing")),
      PoiDefinition("horse_racing", "horseriding.png", HasTag("sport", "equestrian")),
      PoiDefinition("ice_hockey", "icehockey.png", HasTag("sport", "ice_hockey")),
      PoiDefinition("ice_hockey", "icehockey.png", HasTag("leisure", "ice_rink")),
      PoiDefinition("soccer", "soccer.png", HasTag("sport", "soccer")),
      PoiDefinition("sports_centre", "indoor-arena.png", HasTag("leisure", "sports_centre")),
      PoiDefinition("surfing", "surfing.png", HasTag("sport", "surfing")),
      PoiDefinition("swimming", "swimming.png", HasTag("sport", "swimming")),
      PoiDefinition("tennis", "tennis.png", HasTag("sport", "tennis")),
      PoiDefinition("volleyball", "volleyball.png", HasTag("sport", "volleybal"))
    ),
    PoiDefinitionGroup(
      "shops",
      PoiDefinition("beauty", "beautysalon.png", HasTag("shop", "beauty")),
      PoiDefinition("bicycle", "bicycle_shop.png", HasTag("shop", "bicycle")),
      PoiDefinition("books_stationary", "library.png", TagContains("shop", "books", "stationary")),
      PoiDefinition("car", "car.png", HasTag("shop", "car")),
      PoiDefinition("chemist", "drugstore.png", HasTag("shop", "chemist")),
      PoiDefinition("clothes", "clothers_female.png", HasTag("shop", "clothes")),
      PoiDefinition("copyshop", "letter_c.png", HasTag("shop", "copyshop")),
      PoiDefinition("cosmetics", "perfumery.png", HasTag("shop", "cosmetics")),
      PoiDefinition("department_store", "departmentstore.png", HasTag("shop", "department_store")),
      PoiDefinition("diy_hardware", "tools.png", TagContains("shop", "doityourself", "hardware")),
      PoiDefinition("garden_centre", "flowers-1.png", HasTag("shop", "garden_centre")),
      PoiDefinition("general", "letter_g.png", HasTag("shop", "general")),
      PoiDefinition("gift", "gifts.png", HasTag("shop", "gift")),
      PoiDefinition("hairdresser", "barber.png", HasTag("shop", "hairdresser")),
      PoiDefinition("jewelry", "jewelry.png", TagContains("shop", "jewelry", "jewellery")),
      PoiDefinition("kiosk", "kiosk.png", HasTag("shop", "kiosk")),
      PoiDefinition("leather", "bags.png", HasTag("shop", "leather")),
      PoiDefinition("marketplace", "market.png", HasTag("amenity", "marketplace")),
      PoiDefinition("musical_instrument", "music_rock.png", HasTag("shop", "musical_instrument")),
      PoiDefinition("optician", "glasses.png", HasTag("shop", "optician")),
      PoiDefinition("pets", "pets.png", HasTag("shop", "pets")),
      PoiDefinition("phone", "phones.png", HasTag("shop", "mobile_phone")),
      PoiDefinition("photo", "photo.png", HasTag("shop", "photo")),
      PoiDefinition("shoes", "highhills.png", HasTag("shop", "shoes")),
      PoiDefinition("shopping_centre", "mall.png", HasTag("shop", "mall")),
      PoiDefinition("textiles", "textiles.png", HasTag("shop", "textiles")),
      PoiDefinition("toys", "toys.png", HasTag("shop", "toys"))
    ),
    PoiDefinitionGroup(
      "food_shops",
      PoiDefinition("alcohol", "liquor.png", HasTag("shop", "alcohol")),
      PoiDefinition("bakery", "bread.png", HasTag("shop", "bakery")),
      PoiDefinition("beverages", "bar_coktail.png", HasTag("shop", "beverages")),
      PoiDefinition("butcher", "butcher-2.png", HasTag("shop", "butcher")),
      PoiDefinition("cheese", "cheese.png", HasTag("shop", "cheese")),
      PoiDefinition("chocolate", "candy.png", HasTag("shop", "chocolate")),
      PoiDefinition("confectionery", "candy.png", HasTag("shop", "confectionery")),
      PoiDefinition("coffee", "coffee.png", HasTag("shop", "coffee")),
      PoiDefinition("dairy", "milk_and_cookies.png", HasTag("shop", "dairy")),
      PoiDefinition("deli", "patisserie.png", HasTag("shop", "deli")),
      PoiDefinition("drinking_water", "drinkingwater.png", HasTag("amenity", "drinking_water")),
      PoiDefinition("grocery", "grocery.png", HasTag("shop", "grocery")),
      PoiDefinition("organic", "restaurant_vegetarian.png", HasTag("shop", "organic")),
      PoiDefinition("seafood", "restaurant_fish.png", HasTag("shop", "seafood")),
      PoiDefinition("supermarket", "supermarket.png", HasTag("shop", "supermarket")),
      PoiDefinition("wine", "winebar.png", HasTag("shop", "wine"))
    ),
    PoiDefinitionGroup(
      "restaurants",
      PoiDefinition("bar", "bar.png", HasTag("amenity", "bar")),
      PoiDefinition("bbq", "letter_b.png", HasTag("amenity", "bbq")),
      PoiDefinition("biergarten", "number_1.png", HasTag("amenity", "biergarten")),
      PoiDefinition("cafe", "cafetaria.png", HasTag("amenity", "cafe")),
      PoiDefinition("fast_food", "fastfood.png", HasTag("amenity", "fast_food")),
      PoiDefinition("food_court", "letter_f.png", HasTag("amenity", "food_court")),
      PoiDefinition("ice_cream", "icecream.png", HasTag("amenity", "ice_cream")),
      PoiDefinition("ice_cream", "icecream.png", HasTag("cuisine", "ice_cream")),
      PoiDefinition("pub", "pub.png", HasTag("amenity", "pub")),
      PoiDefinition("restaurant", "restaurant.png", HasTag("amenity", "restaurant"))
    ),
    PoiDefinitionGroup(
      "various",
      PoiDefinition("bus_stop", "busstop.png", HasTag("highway", "bus_stop")),
      PoiDefinition("ebike_charging", "e-bike-charging.png", HasTag("amenity", "charging_station").and(HasTag("bicycle", "yes"))),
      PoiDefinition("travel_agency", "travel_agency.png", HasTag("shop", "travel_agency")),
      PoiDefinition("defibrillator", "aed-2.png", HasTag("emergency", "defibrillator"))
    )
  )
}
