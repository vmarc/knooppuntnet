package kpn.core.poi

import kpn.api.common.tiles.ClientPoiConfiguration
import kpn.core.poi.tags.HasTag
import kpn.core.poi.tags.NotHasTag
import kpn.core.poi.tags.NotTagContains
import kpn.core.poi.tags.TagContains

object PoiConfiguration {

  val instance: PoiConfiguration = PoiConfiguration(
    PoiGroupDefinition(
      "hiking-biking",
      true,
      PoiDefinition("ebike-charging", "e-bike-charging.png", 11, 11, HasTag("amenity", "charging_station").and(HasTag("bicycle", "yes"))),
      PoiDefinition("bicycle", "bicycle_shop.png", 13, 13, HasTag("shop", "bicycle")),
      PoiDefinition("bicycle-rental", "cycling.png", 11, 11, HasTag("amenity", "bicycle_rental")),
      PoiDefinition("bicycle-parking", "parking_bicycle-2.png", 13, 14, HasTag("amenity", "bicycle_parking")),
      PoiDefinition("picnic", "picnic-2.png", 13, 14, HasTag("tourism", "picnic_site")),
      PoiDefinition("picnic", "picnic-2.png", 13, 14, HasTag("leisure", "picnic_table")),
      PoiDefinition("bench", "bench.png", 13, 14, HasTag("amenity", "bench")),
      PoiDefinition("toilets", "toilets.png", 13, 14, HasTag("amenity", "toilets")),
      PoiDefinition("drinking-water", "drinkingwater.png", 13, 14, HasTag("amenity", "drinking_water")),
      PoiDefinition("information", "information.png", 13, 13, HasTag("tourism", "information")), // no rel
      PoiDefinition("themepark", "themepark.png", 11, 11, HasTag("tourism", "theme_park")),
      PoiDefinition("viewpoint", "viewpoint.png", 11, 11, HasTag("tourism", "viewpoint")),
      PoiDefinition("attraction", "star.png", 11, 11, HasTag("tourism", "attraction")),
      PoiDefinition("defibrillator", "aed-2.png", 13, 14, HasTag("emergency", "defibrillator"))
    ),
    PoiGroupDefinition(
      "landmarks",
      true,
      PoiDefinition("place-of-worship", "church-2.png", 12, 13, HasTag("amenity", "place_of_worship").and(NotTagContains("religion", "christian", "muslim", "buddhist",
        "hindu", "jewish"))),
      PoiDefinition("church", "chapel-2.png", 12, 13, HasTag("amenity", "place_of_worship").and(TagContains("religion", "christian"))),
      PoiDefinition("mosque", "mosquee.png", 12, 13, HasTag("amenity", "place_of_worship").and(TagContains("religion", "muslim"))),
      PoiDefinition("buddhist-temple", "bouddha.png", 12, 13, HasTag("amenity", "place_of_worship").and(TagContains("religion", "buddhist"))),
      PoiDefinition("hindu-temple", "templehindu.png", 12, 13, HasTag("amenity", "place_of_worship").and(TagContains("religion", "hindu"))),
      PoiDefinition("synagogue", "synagogue-2.png", 12, 13, HasTag("amenity", "place_of_worship").and(TagContains("religion", "jewish"))),
      PoiDefinition("heritage", "worldheritagesite.png", 12, 14, HasTag("heritage")),
      PoiDefinition("historic", "star-3.png", 11, 11, HasTag("historic").and(NotTagContains("historic", "memorial", "monument", "statue", "castle"))),
      PoiDefinition("castle", "castle-2.png", 11, 11, HasTag("historic", "castle")),
      PoiDefinition("monument-memorial", "memorial.png", 11, 11, TagContains("historic", "monument", "memorial")),
      PoiDefinition("statue", "statue-2.png", 11, 11, HasTag("historic", "statue")),
      PoiDefinition("windmill", "windmill-2.png", 11, 11, HasTag("man_made", "windmill")),
      PoiDefinition("watermill", "watermill-2.png", 11, 11, HasTag("man_made", "watermill")),
      PoiDefinition("zoo", "zoo.png", 11, 11, HasTag("tourism", "zoo"))
    ),
    PoiGroupDefinition(
      "restaurants",
      true,
      PoiDefinition("bar", "bar.png", 12, 13, HasTag("amenity", "bar")),
      PoiDefinition("bbq", "bbq.png", 12, 13, HasTag("amenity", "bbq")),
      PoiDefinition("biergarten", "beergarden.png", 12, 13, HasTag("amenity", "biergarten")),
      PoiDefinition("cafe", "cafetaria.png", 12, 13, HasTag("amenity", "cafe")),
      PoiDefinition("fastfood", "fastfood.png", 12, 13, HasTag("amenity", "fast_food")),
      PoiDefinition("foodcourt", "letter_f.png", 12, 13, HasTag("amenity", "food_court")),
      PoiDefinition("icecream", "icecream.png", 12, 13, HasTag("amenity", "ice_cream")),
      PoiDefinition("icecream", "icecream.png", 12, 13, HasTag("cuisine", "ice_cream")),
      PoiDefinition("pub", "pub.png", 12, 13, HasTag("amenity", "pub")),
      PoiDefinition("restaurant", "restaurant.png", 12, 13, HasTag("amenity", "restaurant"))
    ),
    PoiGroupDefinition(
      "places-to-stay",
      true,
      PoiDefinition("alpine-hut", "alpinehut.png", 11, 13, HasTag("tourism", "alpine_hut")),
      PoiDefinition("apartment", "apartment-3.png", 13, 13, HasTag("tourism", "apartment")),
      PoiDefinition("campsite", "camping-2.png", 13, 13, HasTag("tourism", "camp_site").and(NotHasTag("backcountry", "yes"))),
      PoiDefinition("chalet", "letter_c.png", 13, 13, HasTag("tourism", "chalet")),
      PoiDefinition("guesthouse", "bed_breakfast.png", 11, 13, TagContains("tourism", "guest_house", "bed_and_breakfast")),
      PoiDefinition("hostel", "hostel_0star.png", 11, 13, HasTag("tourism", "hostel")),
      PoiDefinition("hotel", "hotel_0star.png", 11, 13, HasTag("tourism", "hotel")),
      PoiDefinition("motel", "motel-2.png", 11, 13, HasTag("tourism", "motel")),
      PoiDefinition("spa", "spa -> ", 13, 13, HasTag("leisure", "spa")),
      PoiDefinition("sauna", "sauna.png", 13, 13, HasTag("leisure", "sauna"))
    ),
    PoiGroupDefinition(
      "tourism",
      true,
      PoiDefinition("arts-centre", "letter_a.png", 13, 13, HasTag("amenity", "arts_centre")),
      PoiDefinition("artwork", "artwork.png", 13, 13, HasTag("tourism=artwork").and(NotTagContains("artwork_type", "statue"))),
      PoiDefinition("casino", "casino.png", 13, 13, HasTag("leisure", "casino")),
      PoiDefinition("casino", "casino.png", 13, 13, HasTag("amenity", "casino")),
      PoiDefinition("gallery", "artgallery.png", 13, 13, HasTag("tourism", "gallery")),
      PoiDefinition("monumental-tree", "tree.png", 13, 13, HasTag("natural", "tree").and(HasTag("monument", "yes"))),
      PoiDefinition("museum", "museum_art.png", 13, 13, HasTag("tourism", "museum")),
      PoiDefinition("vineyard", "vineyard.png", 13, 13, HasTag("landuse", "vineyard")),
      PoiDefinition("tourism", "sight-2.png", 13, 13, HasTag("tourism", "yes")) // TODO review instances of this poi
    ),
    PoiGroupDefinition(
      "amenity",
      true,
      PoiDefinition("atm", "atm-2.png", 14, 15, HasTag("amenity", "atm")),
      PoiDefinition("atm", "atm-2.png", 14, 15, HasTag("amenity", "bank").and(HasTag("atm")).and(NotHasTag("atm", "no"))),
      PoiDefinition("bank", "bank.png", 14, 15, HasTag("amenity", "bank")),
      PoiDefinition("cinema", "cinema.png", 14, 15, HasTag("amenity", "cinema")),
      PoiDefinition("clinic", "firstaid.png", 14, 15, HasTag("amenity", "clinic")),
      PoiDefinition("embassy", "embassy.png", 14, 15, HasTag("amenity", "embassy")),
      PoiDefinition("firestation", "firemen.png", 14, 15, HasTag("amenity", "firestation")),
      PoiDefinition("fuel", "fillingstation.png", 14, 15, HasTag("amenity", "fuel")),
      PoiDefinition("hospital", "hospital-building.png", 14, 15, HasTag("amenity", "hospital")),
      PoiDefinition("library", "library.png", 14, 15, HasTag("amenity", "library")),
      PoiDefinition("musicschool", "musicschool.png", 14, 15, HasTag("amenity", "music_school")),
      PoiDefinition("parking", "parkinggarage.png", 14, 15, HasTag("amenity", "parking")),
      PoiDefinition("pharmacy", "medicine.png", 14, 15, HasTag("amenity", "pharmacy")),
      PoiDefinition("police", "police.png", 14, 15, HasTag("amenity", "police")),
      PoiDefinition("postbox", "postal2.png", 15, 15, HasTag("amenity", "post_box")),
      PoiDefinition("postoffice", "postal.png", 14, 15, HasTag("amenity", "post_office")),
      PoiDefinition("school-college", "", 14, 15, HasTag("amenity", "school", "college")),
      PoiDefinition("taxi", "taxi.png", 14, 15, HasTag("amenity", "taxi")),
      PoiDefinition("theatre", "theater.png", 14, 15, HasTag("amenity", "theatre")),
      PoiDefinition("university", "university.png", 14, 15, HasTag("amenity", "university")),
      PoiDefinition("cemetery", "cemetary.png", 14, 15, HasTag("landuse", "cemetery").and(NotHasTag("animal"))),
      PoiDefinition("busstop", "busstop.png", 15, 15, HasTag("highway", "bus_stop"))
    ),
    PoiGroupDefinition(
      "shops",
      false,
      PoiDefinition("beauty", "beautysalon.png", 14, 15, HasTag("shop", "beauty")),
      PoiDefinition("books-stationary", "library.png", 14, 15, TagContains("shop", "books", "stationary")),
      PoiDefinition("car", "car.png", 14, 15, HasTag("shop", "car")),
      PoiDefinition("chemist", "drugstore.png", 14, 15, HasTag("shop", "chemist")),
      PoiDefinition("clothes", "clothers_female.png", 14, 15, HasTag("shop", "clothes")),
      PoiDefinition("copyshop", "letter_c.png", 14, 15, HasTag("shop", "copyshop")),
      PoiDefinition("cosmetics", "perfumery.png", 14, 15, HasTag("shop", "cosmetics")),
      PoiDefinition("departmentstore", "departmentstore.png", 14, 15, HasTag("shop", "department_store")),
      PoiDefinition("diy-hardware", "tools.png", 14, 15, TagContains("shop", "doityourself", "hardware")),
      PoiDefinition("garden-centre", "flowers-1.png", 14, 15, HasTag("shop", "garden_centre")),
      PoiDefinition("general", "letter_g.png", 14, 15, HasTag("shop", "general")),
      PoiDefinition("gift", "gifts.png", 14, 15, HasTag("shop", "gift")),
      PoiDefinition("hairdresser", "barber.png", 14, 15, HasTag("shop", "hairdresser")),
      PoiDefinition("jewelry", "jewelry.png", 14, 15, TagContains("shop", "jewelry", "jewellery")),
      PoiDefinition("kiosk", "kiosk.png", 14, 15, HasTag("shop", "kiosk")),
      PoiDefinition("leather", "bags.png", 14, 15, HasTag("shop", "leather")),
      PoiDefinition("marketplace", "market.png", 14, 15, HasTag("amenity", "marketplace")),
      PoiDefinition("musical-instrument", "music_rock.png", 14, 15, HasTag("shop", "musical_instrument")),
      PoiDefinition("optician", "glasses.png", 14, 15, HasTag("shop", "optician")),
      PoiDefinition("pets", "pets.png", 14, 15, HasTag("shop", "pets")),
      PoiDefinition("phone", "phones.png", 14, 15, HasTag("shop", "mobile_phone")),
      PoiDefinition("photo", "photo.png", 14, 15, HasTag("shop", "photo")),
      PoiDefinition("shoes", "highhills.png", 14, 15, HasTag("shop", "shoes")),
      PoiDefinition("shoppingcentre", "mall.png", 14, 15, HasTag("shop", "mall")),
      PoiDefinition("textiles", "textiles.png", 14, 15, HasTag("shop", "textiles")),
      PoiDefinition("toys", "toys.png", 14, 15, HasTag("shop", "toys")),
      PoiDefinition("travelagency", "travel_agency.png", 14, 15, HasTag("shop", "travel_agency"))
    ),
    PoiGroupDefinition(
      "foodshops",
      false,
      PoiDefinition("alcohol", "liquor.png", 14, 15, HasTag("shop", "alcohol")),
      PoiDefinition("bakery", "bread.png", 14, 15, HasTag("shop", "bakery")),
      PoiDefinition("beverages", "bar_coktail.png", 14, 15, HasTag("shop", "beverages")),
      PoiDefinition("butcher", "butcher-2.png", 14, 15, HasTag("shop", "butcher")),
      PoiDefinition("cheese", "cheese.png", 14, 15, HasTag("shop", "cheese")),
      PoiDefinition("chocolate", "candy.png", 14, 15, HasTag("shop", "chocolate")),
      PoiDefinition("confectionery", "candy.png", 14, 15, HasTag("shop", "confectionery")),
      PoiDefinition("coffee", "coffee.png", 11, 11, HasTag("shop", "coffee")),
      PoiDefinition("dairy", "milk_and_cookies.png", 14, 15, HasTag("shop", "dairy")),
      PoiDefinition("deli", "patisserie.png", 14, 15, HasTag("shop", "deli")),
      PoiDefinition("grocery", "grocery.png", 14, 15, HasTag("shop", "grocery")),
      PoiDefinition("organic", "restaurant_vegetarian.png", 14, 15, HasTag("shop", "organic")),
      PoiDefinition("seafood", "restaurant_fish.png", 14, 15, HasTag("shop", "seafood")),
      PoiDefinition("supermarket", "supermarket.png", 14, 15, HasTag("shop", "supermarket")),
      PoiDefinition("wine", "winebar.png", 14, 15, HasTag("shop", "wine"))
    ),
    PoiGroupDefinition(
      "sports",
      false,
      PoiDefinition("american-football", "usfootball.png", 15, 15, HasTag("sport", "american_football")),
      PoiDefinition("baseball", "baseball.png", 15, 15, HasTag("sport", "baseball")),
      PoiDefinition("basketball", "basketball.png", 15, 15, HasTag("sport", "basketball")),
      PoiDefinition("cycling", "cycling.png", 15, 15, HasTag("sport", "cycling")),
      PoiDefinition("gymnastics", "gymnastics.png", 15, 15, HasTag("sport", "gymnastics")),
      PoiDefinition("golf", "golfing.png", 15, 15, HasTag("leisure", "golf_course")),
      PoiDefinition("golf", "golfing.png", 15, 15, HasTag("sport", "golf")),
      PoiDefinition("hockey", "hockey.png", 15, 15, HasTag("sport", "hockey")),
      PoiDefinition("hockey", "hockey.png", 15, 15, HasTag("sport", "field_hockey")),
      PoiDefinition("horseracing", "horseriding.png", 15, 15, HasTag("sport", "horse_racing")),
      PoiDefinition("horseracing", "horseriding.png", 15, 15, HasTag("sport", "equestrian")),
      PoiDefinition("icehockey", "icehockey.png", 15, 15, HasTag("sport", "ice_hockey")),
      PoiDefinition("icehockey", "icehockey.png", 15, 15, HasTag("leisure", "ice_rink")),
      PoiDefinition("soccer", "soccer.png", 15, 15, HasTag("sport", "soccer")),
      PoiDefinition("sportscentre", "indoor-arena.png", 15, 15, HasTag("leisure", "sports_centre")),
      PoiDefinition("surfing", "surfing.png", 15, 15, HasTag("sport", "surfing")),
      PoiDefinition("swimming", "swimming.png", 15, 15, HasTag("sport", "swimming")),
      PoiDefinition("tennis", "tennis.png", 15, 15, HasTag("sport", "tennis")),
      PoiDefinition("volleyball", "volleyball.png", 15, 15, HasTag("sport", "volleybal"))
    )
  )
}

case class PoiConfiguration(groupDefinitions: PoiGroupDefinition*) {

  def tagKeys(poiName: String): Seq[String] = {
    poiDefinition(poiName).toSeq.flatMap(_.expression.tagKeys)
  }

  def poiDefinition(poiName: String): Option[PoiDefinition] = {
    groupDefinitions.flatMap(_.definitions).find(_.name == poiName)
  }

  def toClient: ClientPoiConfiguration = {
    ClientPoiConfiguration(groupDefinitions.map(_.toClient))
  }

}
