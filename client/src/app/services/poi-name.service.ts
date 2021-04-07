import { Map } from 'immutable';

export class PoiNameService {
  buildPoiNames(): Map<string, string> {
    const keysAndValues: Array<[string, string]> = [];
    keysAndValues.push(['alcohol', $localize`:@@poi.alcohol:Alcohol`]);
    keysAndValues.push(['alpine-hut', $localize`:@@poi.alpine-hut:Alpine hut`]);
    keysAndValues.push([
      'american-football',
      $localize`:@@poi.american-football:American football`,
    ]);
    keysAndValues.push(['apartment', $localize`:@@poi.apartment:Apartment`]);
    keysAndValues.push([
      'arts-centre',
      $localize`:@@poi.arts-centre:Arts centre`,
    ]);
    keysAndValues.push(['artwork', $localize`:@@poi.artwork:Artwork`]);
    keysAndValues.push(['atm', $localize`:@@poi.atm:Atm`]);
    keysAndValues.push(['attraction', $localize`:@@poi.attraction:Attraction`]);
    keysAndValues.push(['bakery', $localize`:@@poi.bakery:Bakery`]);
    keysAndValues.push(['bank', $localize`:@@poi.bank:Bank`]);
    keysAndValues.push(['bar', $localize`:@@poi.bar:Bar`]);
    keysAndValues.push(['baseball', $localize`:@@poi.baseball:Baseball`]);
    keysAndValues.push(['basketball', $localize`:@@poi.basketball:Basketball`]);
    keysAndValues.push(['bbq', $localize`:@@poi.bbq:BBQ`]);
    keysAndValues.push(['beauty', $localize`:@@poi.beauty:Beauty`]);
    keysAndValues.push(['bench', $localize`:@@poi.bench:Bench`]);
    keysAndValues.push(['beverages', $localize`:@@poi.beverages:Beverages`]);
    keysAndValues.push(['bicycle', $localize`:@@poi.bicycle:Bicycle`]);
    keysAndValues.push([
      'bicycle-parking',
      $localize`:@@poi.bicycle-parking:Bicycle parking`,
    ]);
    keysAndValues.push([
      'bicycle-rental',
      $localize`:@@poi.bicycle-rental:Bicycle rental`,
    ]);
    keysAndValues.push([
      'bicycle-rental-2',
      $localize`:@@poi.bicycle-rental:Bicycle rental`,
    ]);
    keysAndValues.push(['biergarten', $localize`:@@poi.biergarten:Biergarten`]);
    keysAndValues.push([
      'books-stationary',
      $localize`:@@poi.books-stationary:Books/stationary`,
    ]);
    keysAndValues.push([
      'boundary-stone',
      $localize`:@@poi.boundary-stone:Boundary stone`,
    ]);
    keysAndValues.push([
      'buddhist-temple',
      $localize`:@@poi.buddhist-temple:Buddhist temple`,
    ]);
    keysAndValues.push(['busstop', $localize`:@@poi.busstop:Bus stop`]);
    keysAndValues.push(['butcher', $localize`:@@poi.butcher:Butcher`]);
    keysAndValues.push(['cafe', $localize`:@@poi.cafe:Cafe`]);
    keysAndValues.push(['campsite', $localize`:@@poi.campsite:Camp site`]);
    keysAndValues.push(['car', $localize`:@@poi.car:Car`]);
    keysAndValues.push(['casino', $localize`:@@poi.casino:Casino`]);
    keysAndValues.push(['castle', $localize`:@@poi.castle:Castle`]);
    keysAndValues.push(['cemetery', $localize`:@@poi.cemetery:Cemetery`]);
    keysAndValues.push(['chalet', $localize`:@@poi.chalet:Chalet`]);
    keysAndValues.push(['cheese', $localize`:@@poi.cheese:Cheese`]);
    keysAndValues.push(['chemist', $localize`:@@poi.chemist:Chemist`]);
    keysAndValues.push(['chocolate', $localize`:@@poi.chocolate:Chocolate`]);
    keysAndValues.push(['church', $localize`:@@poi.church:Church`]);
    keysAndValues.push(['cinema', $localize`:@@poi.cinema:Cinema`]);
    keysAndValues.push(['clinic', $localize`:@@poi.clinic:Clinic`]);
    keysAndValues.push(['clothes', $localize`:@@poi.clothes:Clothes`]);
    keysAndValues.push(['coffee', $localize`:@@poi.coffee:Coffee`]);
    keysAndValues.push([
      'confectionery',
      $localize`:@@poi.confectionery:Confectionery`,
    ]);
    keysAndValues.push(['copyshop', $localize`:@@poi.copyshop:Copyshop`]);
    keysAndValues.push(['cosmetics', $localize`:@@poi.cosmetics:Cosmetics`]);
    keysAndValues.push(['cycling', $localize`:@@poi.cycling:Cycling`]);
    keysAndValues.push(['dairy', $localize`:@@poi.dairy:Dairy`]);
    keysAndValues.push([
      'defibrillator',
      $localize`:@@poi.defibrillator:Defibrillator`,
    ]);
    keysAndValues.push(['deli', $localize`:@@poi.deli:Deli`]);
    keysAndValues.push([
      'departmentstore',
      $localize`:@@poi.departmentstore:Department store`,
    ]);
    keysAndValues.push([
      'diy-hardware',
      $localize`:@@poi.diy-hardware:DIY hardware`,
    ]);
    keysAndValues.push([
      'drinking-water',
      $localize`:@@poi.drinking-water:Drinking water`,
    ]);
    keysAndValues.push([
      'ebike-charging',
      $localize`:@@poi.ebike-charging:Ebike charging`,
    ]);
    keysAndValues.push(['embassy', $localize`:@@poi.embassy:Embassy`]);
    keysAndValues.push(['fastfood', $localize`:@@poi.fastfood:Fast food`]);
    keysAndValues.push([
      'firestation',
      $localize`:@@poi.firestation:Firestation`,
    ]);
    keysAndValues.push(['foodcourt', $localize`:@@poi.foodcourt:Food court`]);
    keysAndValues.push(['fuel', $localize`:@@poi.fuel:Fuel`]);
    keysAndValues.push(['gallery', $localize`:@@poi.gallery:Gallery`]);
    keysAndValues.push([
      'garden-centre',
      $localize`:@@poi.garden-centre:Garden centre`,
    ]);
    keysAndValues.push(['general', $localize`:@@poi.general:General`]);
    keysAndValues.push(['gift', $localize`:@@poi.gift:Gift`]);
    keysAndValues.push(['golf', $localize`:@@poi.golf:Golf`]);
    keysAndValues.push(['grocery', $localize`:@@poi.grocery:Grocery`]);
    keysAndValues.push([
      'guesthouse',
      $localize`:@@poi.guesthouse:Guest house`,
    ]);
    keysAndValues.push(['gymnastics', $localize`:@@poi.gymnastics:Gymnastics`]);
    keysAndValues.push([
      'hairdresser',
      $localize`:@@poi.hairdresser:Hairdresser`,
    ]);
    keysAndValues.push(['heritage', $localize`:@@poi.heritage:Heritage`]);
    keysAndValues.push([
      'hindu-temple',
      $localize`:@@poi.hindu-temple:Hindu temple`,
    ]);
    keysAndValues.push(['historic', $localize`:@@poi.historic:Historic`]);
    keysAndValues.push(['hockey', $localize`:@@poi.hockey:Hockey`]);
    keysAndValues.push([
      'horseracing',
      $localize`:@@poi.horseracing:Horse racing`,
    ]);
    keysAndValues.push(['hospital', $localize`:@@poi.hospital:Hospital`]);
    keysAndValues.push(['hostel', $localize`:@@poi.hostel:Hostel`]);
    keysAndValues.push(['hotel', $localize`:@@poi.hotel:Hotel`]);
    keysAndValues.push(['icecream', $localize`:@@poi.ice-cream:Ice cream`]);
    keysAndValues.push(['icehockey', $localize`:@@poi.icehockey:Ice hockey`]);
    keysAndValues.push([
      'information',
      $localize`:@@poi.information:Information`,
    ]);
    keysAndValues.push(['jewelry', $localize`:@@poi.jewelry:Jewelry`]);
    keysAndValues.push(['kiosk', $localize`:@@poi.kiosk:Kiosk`]);
    keysAndValues.push(['leather', $localize`:@@poi.leather:Leather`]);
    keysAndValues.push(['library', $localize`:@@poi.library:Library`]);
    keysAndValues.push([
      'marketplace',
      $localize`:@@poi.marketplace:Marketplace`,
    ]);
    keysAndValues.push([
      'monumental-tree',
      $localize`:@@poi.monumental-tree:Monumental tree`,
    ]);
    keysAndValues.push([
      'monument-memorial',
      $localize`:@@poi.monument-memorial:Monument`,
    ]);
    keysAndValues.push(['mosque', $localize`:@@poi.mosque:Mosque`]);
    keysAndValues.push(['motel', $localize`:@@poi.motel:Motel`]);
    keysAndValues.push(['museum', $localize`:@@poi.museum:Museum`]);
    keysAndValues.push([
      'musical-instrument',
      $localize`:@@poi.musical-instrument:Musical instrument`,
    ]);
    keysAndValues.push([
      'musicschool',
      $localize`:@@poi.musicschool:Music school`,
    ]);
    keysAndValues.push(['optician', $localize`:@@poi.optician:Optician`]);
    keysAndValues.push(['organic', $localize`:@@poi.organic:Organic`]);
    keysAndValues.push(['parking', $localize`:@@poi.parking:Parking`]);
    keysAndValues.push(['pets', $localize`:@@poi.pets:Pets`]);
    keysAndValues.push(['pharmacy', $localize`:@@poi.pharmacy:Pharmacy`]);
    keysAndValues.push(['phone', $localize`:@@poi.phone:Phone`]);
    keysAndValues.push(['photo', $localize`:@@poi.photo:Photo`]);
    keysAndValues.push(['picnic', $localize`:@@poi.picnic:Picnic`]);
    keysAndValues.push([
      'place-of-worship',
      $localize`:@@poi.place-of-worship:Place of worship`,
    ]);
    keysAndValues.push(['police', $localize`:@@poi.police:Police`]);
    keysAndValues.push(['postbox', $localize`:@@poi.postbox:Post box`]);
    keysAndValues.push([
      'postoffice',
      $localize`:@@poi.postoffice:Post office`,
    ]);
    keysAndValues.push(['pub', $localize`:@@poi.pub:Pub`]);
    keysAndValues.push(['restaurant', $localize`:@@poi.restaurant:Restaurant`]);
    keysAndValues.push(['sauna', $localize`:@@poi.sauna:Sauna`]);
    keysAndValues.push(['seafood', $localize`:@@poi.seafood:Seafood`]);
    keysAndValues.push(['shoes', $localize`:@@poi.shoes:Shoes`]);
    keysAndValues.push([
      'shoppingcentre',
      $localize`:@@poi.shoppingcentre:Shopping centre`,
    ]);
    keysAndValues.push(['soccer', $localize`:@@poi.soccer:Soccer`]);
    keysAndValues.push([
      'sportscentre',
      $localize`:@@poi.sportscentre:Sports centre`,
    ]);
    keysAndValues.push(['statue', $localize`:@@poi.statue:Statue`]);
    keysAndValues.push([
      'supermarket',
      $localize`:@@poi.supermarket:Supermarket`,
    ]);
    keysAndValues.push(['surfing', $localize`:@@poi.surfing:Surfing`]);
    keysAndValues.push(['swimming', $localize`:@@poi.swimming:Swimming`]);
    keysAndValues.push(['synagogue', $localize`:@@poi.synagogue:Synagogue`]);
    keysAndValues.push(['taxi', $localize`:@@poi.taxi:Taxi`]);
    keysAndValues.push(['tennis', $localize`:@@poi.tennis:Tennis`]);
    keysAndValues.push(['textiles', $localize`:@@poi.textiles:Textiles`]);
    keysAndValues.push(['theatre', $localize`:@@poi.theatre:Theatre`]);
    keysAndValues.push(['themepark', $localize`:@@poi.themepark:Themepark`]);
    keysAndValues.push(['toilets', $localize`:@@poi.toilets:Toilets`]);
    keysAndValues.push(['tourism', $localize`:@@poi.tourism:Tourism`]);
    keysAndValues.push(['toys', $localize`:@@poi.toys:Toys`]);
    keysAndValues.push([
      'travelagency',
      $localize`:@@poi.travelagency:Travelagency`,
    ]);
    keysAndValues.push(['university', $localize`:@@poi.university:University`]);
    keysAndValues.push(['viewpoint', $localize`:@@poi.viewpoint:Viewpoint`]);
    keysAndValues.push(['vineyard', $localize`:@@poi.vineyard:Vineyard`]);
    keysAndValues.push(['volleyball', $localize`:@@poi.volleyball:Volleyball`]);
    keysAndValues.push(['watermill', $localize`:@@poi.watermill:Watermill`]);
    keysAndValues.push([
      'wayside-shrine',
      $localize`:@@poi.wayside-shrine:Wayside shrine`,
    ]);
    keysAndValues.push(['windmill', $localize`:@@poi.windmill:Windmill`]);
    keysAndValues.push(['wine', $localize`:@@poi.wine:Wine`]);
    keysAndValues.push(['zoo', $localize`:@@poi.zoo:Zoo`]);

    return Map<string, string>(keysAndValues);
  }
}
