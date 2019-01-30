import {Component} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";

@Component({
  selector: 'kpn-map-poi-config',
  template: `

    <div [formGroup]="form">
      <table>

        <tr>
          <td colspan="8" class="subset-title">amenity</td>
        </tr>
        <tr>
          <td class="col-icon"></td>
          <td class="col-name"></td>
          <td class="col-level-0">NO</td>
          <td class="col-level-13">13</td>
          <td class="col-level-14">14</td>
          <td class="col-level-15">15</td>
          <td class="col-level-16">16</td>
        </tr>
      </table>

      <kpn-poi-config formControlName="atm" icon="atm-2.png" name="atm" i18n-name="@@poi.atm"></kpn-poi-config>
      <kpn-poi-config formControlName="bank" icon="bank.png" name="bank" i18n-name="@@poi.bank"></kpn-poi-config>
      <kpn-poi-config formControlName="bench" icon="letter_b.png" name="bench" i18n-name="@@poi.bench"></kpn-poi-config>
      <kpn-poi-config formControlName="bicycle_parking" icon="parking_bicycle-2.png" name="bicycle_parking" i18n-name="@@poi.bicycle_parking"></kpn-poi-config>
      <kpn-poi-config formControlName="bicycle_rental" icon="cycling.png" name="bicycle_rental" i18n-name="@@poi.bicycle_rental"></kpn-poi-config>
      <kpn-poi-config formControlName="cinema" icon="cinema.png" name="cinema" i18n-name="@@poi.cinema"></kpn-poi-config>
      <kpn-poi-config formControlName="clinic" icon="firstaid.png" name="clinic" i18n-name="@@poi.clinic"></kpn-poi-config>
      <kpn-poi-config formControlName="embassy" icon="embassy.png" name="embassy" i18n-name="@@poi.embassy"></kpn-poi-config>
      <kpn-poi-config formControlName="firestation" icon="firemen.png" name="firestation" i18n-name="@@poi.firestation"></kpn-poi-config>
      <kpn-poi-config formControlName="fuel" icon="fillingstation.png" name="fuel" i18n-name="@@poi.fuel"></kpn-poi-config>
      <kpn-poi-config formControlName="hospital" icon="hospital-building.png" name="hospital" i18n-name="@@poi.hospital"></kpn-poi-config>
      <kpn-poi-config formControlName="library" icon="library.png" name="library" i18n-name="@@poi.library"></kpn-poi-config>
      <kpn-poi-config formControlName="music_school" icon="musicschool.png" name="music_school" i18n-name="@@poi.music_school"></kpn-poi-config>
      <kpn-poi-config formControlName="parking" icon="parkinggarage.png" name="parking" i18n-name="@@poi.parking"></kpn-poi-config>
      <kpn-poi-config formControlName="pharmacy" icon="medicine.png" name="pharmacy" i18n-name="@@poi.pharmacy"></kpn-poi-config>
      <kpn-poi-config formControlName="police" icon="police.png" name="police" i18n-name="@@poi.police"></kpn-poi-config>
      <kpn-poi-config formControlName="post_box" icon="postal2.png" name="post_box" i18n-name="@@poi.post_box"></kpn-poi-config>
      <kpn-poi-config formControlName="post_office" icon="postal.png" name="post_office" i18n-name="@@poi.post_office"></kpn-poi-config>
      <!--    <kpn-poi-config icon="" name="school_college" i18n-name="@@poi.school_college"></kpn-poi-config> -->
      <kpn-poi-config formControlName="taxi" icon="taxi.png" name="taxi" i18n-name="@@poi.taxi"></kpn-poi-config>
      <kpn-poi-config formControlName="theatre" icon="theater.png" name="taxi" i18n-name="@@poi.theatre"></kpn-poi-config>
      <kpn-poi-config formControlName="toilets" icon="toilets.png" name="toilets" i18n-name="@@poi.toilets"></kpn-poi-config>
      <kpn-poi-config formControlName="university" icon="university.png" name="university" i18n-name="@@poi.university"></kpn-poi-config>
      <kpn-poi-config formControlName="place_of_worship" icon="church-2.png" name="place_of_worship" i18n-name="@@poi.place_of_worship"></kpn-poi-config>
      <kpn-poi-config formControlName="church" icon="chapel-2.png" name="church" i18n-name="@@poi.church"></kpn-poi-config>
      <kpn-poi-config formControlName="mosque" icon="mosquee.png" name="mosque" i18n-name="@@poi.mosque"></kpn-poi-config>
      <kpn-poi-config formControlName="buddhist_temple" icon="bouddha.png" name="buddhist_temple" i18n-name="@@poi.buddhist_temple"></kpn-poi-config>
      <kpn-poi-config formControlName="hindu_temple" icon="templehindu.png" name="hindu_temple" i18n-name="@@poi.hindu_temple"></kpn-poi-config>
      <kpn-poi-config formControlName="synagogue" icon="synagogue-2.png" name="synagogue" i18n-name="@@poi.synagogue"></kpn-poi-config>
      <kpn-poi-config formControlName="cemetery" icon="cemetary.png" name="cemetery" i18n-name="@@poi.cemetery"></kpn-poi-config>

      <table>
        <tr>
          <td colspan="8" class="subset-title">tourism</td>
        </tr>
        <tr>
          <td class="col-icon"></td>
          <td class="col-name"></td>
          <td class="col-level-0">NO</td>
          <td class="col-level-13">13</td>
          <td class="col-level-14">14</td>
          <td class="col-level-15">15</td>
          <td class="col-level-16">16</td>
        </tr>
      </table>

      <kpn-poi-config formControlName="arts_centre" icon="letter_a.png" name="arts_centre" i18n-name="@@poi.arts_centre"></kpn-poi-config>
      <kpn-poi-config formControlName="artwork" icon="artwork.png" name="artwork" i18n-name="@@poi.artwork"></kpn-poi-config>
      <kpn-poi-config formControlName="attraction" icon="star.png" name="attraction" i18n-name="@@poi.attraction"></kpn-poi-config>
      <kpn-poi-config formControlName="casino" icon="casino.png" name="casino" i18n-name="@@poi.casino"></kpn-poi-config>
      <kpn-poi-config formControlName="gallery" icon="artgallery.png" name="gallery" i18n-name="@@poi.gallery"></kpn-poi-config>
      <kpn-poi-config formControlName="heritage" icon="worldheritagesite.png" name="heritage" i18n-name="@@poi.heritage"></kpn-poi-config>
      <kpn-poi-config formControlName="historic" icon="star-3.png" name="historic" i18n-name="@@poi.historic"></kpn-poi-config>
      <kpn-poi-config formControlName="castle" icon="castle-2.png" name="castle" i18n-name="@@poi.castle"></kpn-poi-config>
      <kpn-poi-config formControlName="monument_memorial" icon="memorial.png" name="monument_memorial" i18n-name="@@poi.monument_memorial"></kpn-poi-config>
      <kpn-poi-config formControlName="statue" icon="statue-2.png" name="statue" i18n-name="@@poi.statue"></kpn-poi-config>
      <kpn-poi-config formControlName="information" icon="information.png" name="information" i18n-name="@@poi.information"></kpn-poi-config>
      <kpn-poi-config formControlName="monumental_tree" icon="tree.png" name="monumental_tree" i18n-name="@@poi.monumental_tree"></kpn-poi-config>
      <kpn-poi-config formControlName="museum" icon="museum_art.png" name="museum" i18n-name="@@poi.museum"></kpn-poi-config>
      <kpn-poi-config formControlName="picnic" icon="picnic-2.png" name="picnic" i18n-name="@@poi.picnic"></kpn-poi-config>
      <kpn-poi-config formControlName="theme_park" icon="themepark.png" name="theme_park" i18n-name="@@poi.theme_park"></kpn-poi-config>
      <kpn-poi-config formControlName="viewpoint" icon="viewpoint.png" name="viewpoint" i18n-name="@@poi.viewpoint"></kpn-poi-config>
      <kpn-poi-config formControlName="vineyard" icon="vineyard.png" name="vineyard" i18n-name="@@poi.vineyard"></kpn-poi-config>
      <kpn-poi-config formControlName="windmill" icon="windmill-2.png" name="windmill" i18n-name="@@poi.windmill"></kpn-poi-config>
      <kpn-poi-config formControlName="watermill" icon="watermill-2.png" name="watermill" i18n-name="@@poi.watermill"></kpn-poi-config>
      <kpn-poi-config formControlName="zoo" icon="zoo.png" name="zoo" i18n-name="@@poi.zoo"></kpn-poi-config>
      <kpn-poi-config formControlName="tourism" icon="sight-2.png" name="tourism" i18n-name="@@poi.tourism"></kpn-poi-config>

      <table>
        <tr>
          <td colspan="8" class="subset-title">places_to_stay</td>
        </tr>
        <tr>
          <td class="col-icon"></td>
          <td class="col-name"></td>
          <td class="col-level-0">NO</td>
          <td class="col-level-13">13</td>
          <td class="col-level-14">14</td>
          <td class="col-level-15">15</td>
          <td class="col-level-16">16</td>
        </tr>
      </table>

      <kpn-poi-config formControlName="alpine_hut" icon="alpinehut.png" name="alpine_hut" i18n-name="@@poi.alpine_hut"></kpn-poi-config>
      <kpn-poi-config formControlName="apartment" icon="apartment-3.png" name="apartment" i18n-name="@@poi.apartment"></kpn-poi-config>
      <kpn-poi-config formControlName="camp_site" icon="camping-2.png" name="camp_site" i18n-name="@@poi.camp_site"></kpn-poi-config>
      <kpn-poi-config formControlName="chalet" icon="letter_c.png" name="chalet" i18n-name="@@poi.chalet"></kpn-poi-config>
      <kpn-poi-config formControlName="guest_house" icon="bed_breakfast.png" name="guest_house" i18n-name="@@poi.guest_house"></kpn-poi-config>
      <kpn-poi-config formControlName="hostel" icon="hostel_0star.png" name="hostel" i18n-name="@@poi.hostel"></kpn-poi-config>
      <kpn-poi-config formControlName="hotel" icon="hotel_0star.png" name="hotel" i18n-name="@@poi.hotel"></kpn-poi-config>
      <kpn-poi-config formControlName="motel" icon="motel-2.png" name="motel" i18n-name="@@poi.motel"></kpn-poi-config>
      <!--<kpn-poi-config icon="spa" name="spa" i18n-name="@@poi.spa"></kpn-poi-config>-->
      <kpn-poi-config formControlName="sauna" icon="sauna.png" name="sauna" i18n-name="@@poi.sauna"></kpn-poi-config>

      <table>
        <tr>
          <td colspan="8" class="subset-title">sport</td>
        </tr>
        <tr>
          <td class="col-icon"></td>
          <td class="col-name"></td>
          <td class="col-level-0">NO</td>
          <td class="col-level-13">13</td>
          <td class="col-level-14">14</td>
          <td class="col-level-15">15</td>
          <td class="col-level-16">16</td>
        </tr>
      </table>

      <kpn-poi-config formControlName="american_football" icon="usfootball.png" name="american_football" i18n-name="@@poi.american_football"></kpn-poi-config>
      <kpn-poi-config formControlName="baseball" icon="baseball.png" name="baseball" i18n-name="@@poi.baseball"></kpn-poi-config>
      <kpn-poi-config formControlName="basketball" icon="basketball.png" name="basketball" i18n-name="@@poi.basketball"></kpn-poi-config>
      <kpn-poi-config formControlName="cycling" icon="cycling.png" name="cycling" i18n-name="@@poi.cycling"></kpn-poi-config>
      <kpn-poi-config formControlName="gymnastics" icon="gymnastics.png" name="gymnastics" i18n-name="@@poi.gymnastics"></kpn-poi-config>
      <kpn-poi-config formControlName="golf" icon="golfing.png" name="golf" i18n-name="@@poi.golf"></kpn-poi-config>
      <kpn-poi-config formControlName="hockey" icon="hockey.png" name="hockey" i18n-name="@@poi.hockey"></kpn-poi-config>
      <kpn-poi-config formControlName="horse_racing" icon="horseriding.png" name="horse_racing" i18n-name="@@poi.horse_racing"></kpn-poi-config>
      <kpn-poi-config formControlName="ice_hockey" icon="icehockey.png" name="ice_hockey" i18n-name="@@poi.ice_hockey"></kpn-poi-config>
      <kpn-poi-config formControlName="ice_hockey" icon="icehockey.png" name="ice_hockey" i18n-name="@@poi.ice_hockey"></kpn-poi-config>
      <kpn-poi-config formControlName="soccer" icon="soccer.png" name="soccer" i18n-name="@@poi.soccer"></kpn-poi-config>
      <kpn-poi-config formControlName="sports_centre" icon="indoor-arena.png" name="sports_centre" i18n-name="@@poi.sports_centre"></kpn-poi-config>
      <kpn-poi-config formControlName="surfing" icon="surfing.png" name="surfing" i18n-name="@@poi.surfing"></kpn-poi-config>
      <kpn-poi-config formControlName="swimming" icon="swimming.png" name="swimming" i18n-name="@@poi.swimming"></kpn-poi-config>
      <kpn-poi-config formControlName="tennis" icon="tennis.png" name="tennis" i18n-name="@@poi.tennis"></kpn-poi-config>
      <kpn-poi-config formControlName="volleyball" icon="volleyball.png" name="volleyball" i18n-name="@@poi.volleyball"></kpn-poi-config>

      <table>
        <tr>
          <td colspan="8" class="subset-title">shops</td>
        </tr>
        <tr>
          <td class="col-icon"></td>
          <td class="col-name"></td>
          <td class="col-level-0">NO</td>
          <td class="col-level-13">13</td>
          <td class="col-level-14">14</td>
          <td class="col-level-15">15</td>
          <td class="col-level-16">16</td>
        </tr>
      </table>

      <kpn-poi-config formControlName="beauty" icon="beautysalon.png" name="beauty" i18n-name="@@poi.beauty"></kpn-poi-config>
      <kpn-poi-config formControlName="bicycle" icon="bicycle_shop.png" name="bicycle" i18n-name="@@poi.bicycle"></kpn-poi-config>
      <kpn-poi-config formControlName="books_stationary" icon="library.png" name="books_stationary" i18n-name="@@poi.books_stationary"></kpn-poi-config>
      <kpn-poi-config formControlName="car" icon="car.png" name="car" i18n-name="@@poi.car"></kpn-poi-config>
      <kpn-poi-config formControlName="chemist" icon="drugstore.png" name="chemist" i18n-name="@@poi.chemist"></kpn-poi-config>
      <kpn-poi-config formControlName="clothes" icon="clothers_female.png" name="clothes" i18n-name="@@poi.clothes"></kpn-poi-config>
      <kpn-poi-config formControlName="copyshop" icon="letter_c.png" name="copyshop" i18n-name="@@poi.copyshop"></kpn-poi-config>
      <kpn-poi-config formControlName="cosmetics" icon="perfumery.png" name="cosmetics" i18n-name="@@poi.cosmetics"></kpn-poi-config>
      <kpn-poi-config formControlName="department_store" icon="departmentstore.png" name="department_store" i18n-name="@@poi.department_store"></kpn-poi-config>
      <kpn-poi-config formControlName="diy_hardware" icon="tools.png" name="diy_hardware" i18n-name="@@poi.diy_hardware"></kpn-poi-config>
      <kpn-poi-config formControlName="garden_centre" icon="flowers-1.png" name="garden_centre" i18n-name="@@poi.garden_centre"></kpn-poi-config>
      <kpn-poi-config formControlName="general" icon="letter_g.png" name="general" i18n-name="@@poi.general"></kpn-poi-config>
      <kpn-poi-config formControlName="gift" icon="gifts.png" name="gift" i18n-name="@@poi.gift"></kpn-poi-config>
      <kpn-poi-config formControlName="hairdresser" icon="barber.png" name="hairdresser" i18n-name="@@poi.hairdresser"></kpn-poi-config>
      <kpn-poi-config formControlName="jewelry" icon="jewelry.png" name="jewelry" i18n-name="@@poi.jewelry"></kpn-poi-config>
      <kpn-poi-config formControlName="kiosk" icon="kiosk.png" name="kiosk" i18n-name="@@poi.kiosk"></kpn-poi-config>
      <kpn-poi-config formControlName="leather" icon="bags.png" name="leather" i18n-name="@@poi.leather"></kpn-poi-config>
      <kpn-poi-config formControlName="marketplace" icon="market.png" name="marketplace" i18n-name="@@poi.marketplace"></kpn-poi-config>
      <kpn-poi-config formControlName="musical_instrument" icon="music_rock.png" name="musical_instrument" i18n-name="@@poi.musical_instrument"></kpn-poi-config>
      <kpn-poi-config formControlName="optician" icon="glasses.png" name="optician" i18n-name="@@poi.optician"></kpn-poi-config>
      <kpn-poi-config formControlName="pets" icon="pets.png" name="pets" i18n-name="@@poi.pets"></kpn-poi-config>
      <kpn-poi-config formControlName="phone" icon="phones.png" name="phone" i18n-name="@@poi.phone"></kpn-poi-config>
      <kpn-poi-config formControlName="photo" icon="photo.png" name="photo" i18n-name="@@poi.photo"></kpn-poi-config>
      <kpn-poi-config formControlName="shoes" icon="highhills.png" name="shoes" i18n-name="@@poi.shoes"></kpn-poi-config>
      <kpn-poi-config formControlName="shopping_centre" icon="mall.png" name="shopping_centre" i18n-name="@@poi.shopping_centre"></kpn-poi-config>
      <kpn-poi-config formControlName="textiles" icon="textiles.png" name="textiles" i18n-name="@@poi.textiles"></kpn-poi-config>
      <kpn-poi-config formControlName="toys" icon="toys.png" name="toys" i18n-name="@@poi.toys"></kpn-poi-config>

      <table>
        <tr>
          <td colspan="8" class="subset-title">food_shops</td>
        </tr>
        <tr>
          <td class="col-icon"></td>
          <td class="col-name"></td>
          <td class="col-level-0">NO</td>
          <td class="col-level-13">13</td>
          <td class="col-level-14">14</td>
          <td class="col-level-15">15</td>
          <td class="col-level-16">16</td>
        </tr>
      </table>

      <kpn-poi-config formControlName="alcohol" icon="liquor.png" name="alcohol" i18n-name="@@poi.alcohol"></kpn-poi-config>
      <kpn-poi-config formControlName="bakery" icon="bread.png" name="bakery" i18n-name="@@poi.bakery"></kpn-poi-config>
      <kpn-poi-config formControlName="beverages" icon="bar_coktail.png" name="beverages" i18n-name="@@poi.beverages"></kpn-poi-config>
      <kpn-poi-config formControlName="butcher" icon="butcher-2.png" name="butcher" i18n-name="@@poi.butcher"></kpn-poi-config>
      <kpn-poi-config formControlName="cheese" icon="cheese.png" name="cheese" i18n-name="@@poi.cheese"></kpn-poi-config>
      <kpn-poi-config formControlName="chocolate" icon="candy.png" name="chocolate" i18n-name="@@poi.chocolate"></kpn-poi-config>
      <kpn-poi-config formControlName="confectionery" icon="candy.png" name="confectionery" i18n-name="@@poi.confectionery"></kpn-poi-config>
      <kpn-poi-config formControlName="coffee" icon="coffee.png" name="coffee" i18n-name="@@poi.coffee"></kpn-poi-config>
      <kpn-poi-config formControlName="dairy" icon="milk_and_cookies.png" name="dairy" i18n-name="@@poi.dairy"></kpn-poi-config>
      <kpn-poi-config formControlName="deli" icon="patisserie.png" name="deli" i18n-name="@@poi.deli"></kpn-poi-config>
      <kpn-poi-config formControlName="drinking_water" icon="drinkingwater.png" name="drinking_water" i18n-name="@@poi.drinking_water"></kpn-poi-config>
      <kpn-poi-config formControlName="grocery" icon="grocery.png" name="grocery" i18n-name="@@poi.grocery"></kpn-poi-config>
      <kpn-poi-config formControlName="organic" icon="restaurant_vegetarian.png" name="organic" i18n-name="@@poi.organic"></kpn-poi-config>
      <kpn-poi-config formControlName="seafood" icon="restaurant_fish.png" name="seafood" i18n-name="@@poi.seafood"></kpn-poi-config>
      <kpn-poi-config formControlName="supermarket" icon="supermarket.png" name="supermarket" i18n-name="@@poi.supermarket"></kpn-poi-config>
      <kpn-poi-config formControlName="wine" icon="winebar.png" name="wine" i18n-name="@@poi.wine"></kpn-poi-config>

      <table>
        <tr>
          <td colspan="8" class="subset-title">restaurants</td>
        </tr>
        <tr>
          <td class="col-icon"></td>
          <td class="col-name"></td>
          <td class="col-level-0">NO</td>
          <td class="col-level-13">13</td>
          <td class="col-level-14">14</td>
          <td class="col-level-15">15</td>
          <td class="col-level-16">16</td>
        </tr>
      </table>

      <kpn-poi-config formControlName="bar" icon="bar.png" name="bar" i18n-name="@@poi.bar"></kpn-poi-config>
      <kpn-poi-config formControlName="bbq" icon="letter_b.png" name="bbq" i18n-name="@@poi.bbq"></kpn-poi-config>
      <kpn-poi-config formControlName="biergarten" icon="number_1.png" name="biergarten" i18n-name="@@poi.biergarten"></kpn-poi-config>
      <kpn-poi-config formControlName="cafe" icon="cafetaria.png" name="cafe" i18n-name="@@poi.cafe"></kpn-poi-config>
      <kpn-poi-config formControlName="fast_food" icon="fastfood.png" name="fast_food" i18n-name="@@poi.fast_food"></kpn-poi-config>
      <kpn-poi-config formControlName="food_court" icon="letter_f.png" name="food_court" i18n-name="@@poi.food_court"></kpn-poi-config>
      <kpn-poi-config formControlName="ice_cream" icon="icecream.png" name="ice_cream" i18n-name="@@poi.ice_cream"></kpn-poi-config>
      <kpn-poi-config formControlName="pub" icon="pub.png" name="pub" i18n-name="@@poi.pub"></kpn-poi-config>
      <kpn-poi-config formControlName="restaurant" icon="restaurant.png" name="restaurant" i18n-name="@@poi.restaurant"></kpn-poi-config>

      <table>
        <tr>
          <td colspan="8" class="subset-title">various</td>
        </tr>
        <tr>
          <td class="col-icon"></td>
          <td class="col-name"></td>
          <td class="col-level-0">NO</td>
          <td class="col-level-13">13</td>
          <td class="col-level-14">14</td>
          <td class="col-level-15">15</td>
          <td class="col-level-16">16</td>
        </tr>
      </table>

      <kpn-poi-config formControlName="bus_stop" icon="busstop.png" name="bus_stop" i18n-name="@@poi.bus_stop"></kpn-poi-config>
      <kpn-poi-config formControlName="ebike_charging" icon="e-bike-charging.png" name="ebike_charging" i18n-name="@@poi.ebike_charging"></kpn-poi-config>
      <kpn-poi-config formControlName="travel_agency" icon="travel_agency.png" name="travel_agency" i18n-name="@@poi.travel_agency"></kpn-poi-config>
      <kpn-poi-config formControlName="defibrillator" icon="aed-2.png" name="defibrillator" i18n-name="@@poi.defibrillator"></kpn-poi-config>

    </div>
  `,
  styles: [`

    .subset-title {
      margin-top: 40px;
      margin-bottom: 20px;
      font-weight: 600;
    }

    /deep/ .col-icon {
      height: 40px;
      vertical-align: center;
      width: 40px;
    }

    /deep/ .col-name {
      height: 40px;
      vertical-align: center;
      width: 150px;
    }

    /deep/ .col-level-0 {
      height: 40px;
      vertical-align: center;
      width: 40px;
    }

    /deep/ .col-level-13 {
      height: 40px;
      vertical-align: center;
      width: 30px;
    }

    /deep/ .col-level-14 {
      height: 40px;
      vertical-align: center;
      width: 30px;
    }

    /deep/ .col-level-15 {
      height: 40px;
      vertical-align: center;
      width: 30px;
    }

    /deep/ .col-level-16 {
      height: 40px;
      vertical-align: center;
      width: 30px;
    }
  `]
})
export class MapPoiConfigComponent {

  readonly form: FormGroup;

  readonly keys = [
    "atm",
    "bank",
    "bench",
    "bicycle_parking",
    "bicycle_rental",
    "cinema",
    "clinic",
    "embassy",
    "firestation",
    "fuel",
    "hospital",
    "library",
    "music_school",
    "parking",
    "pharmacy",
    "police",
    "post_box",
    "post_office",
    "school_college",
    "taxi",
    "theatre",
    "toilets",
    "university",
    "place_of_worship",
    "church",
    "mosque",
    "buddhist_temple",
    "hindu_temple",
    "synagogue",
    "cemetery",
    "arts_centre",
    "artwork",
    "attraction",
    "casino",
    "gallery",
    "heritage",
    "historic",
    "castle",
    "monument_memorial",
    "statue",
    "information",
    "monumental_tree",
    "museum",
    "picnic",
    "theme_park",
    "viewpoint",
    "vineyard",
    "windmill",
    "watermill",
    "zoo",
    "tourism",

    "alpine_hut",
    "apartment",
    "camp_site",
    "chalet",
    "guest_house",
    "hostel",
    "hotel",
    "motel",
    "sauna",

    "american_football",
    "baseball",
    "basketball",
    "cycling",
    "gymnastics",
    "golf",
    "hockey",
    "horse_racing",
    "ice_hockey",
    "soccer",
    "sports_centre",
    "surfing",
    "swimming",
    "tennis",
    "volleyball",

    "beauty",
    "bicycle",
    "books_stationary",
    "car",
    "chemist",
    "clothes",
    "copyshop",
    "cosmetics",
    "department_store",
    "diy_hardware",
    "garden_centre",
    "general",
    "gift",
    "hairdresser",
    "jewelry",
    "kiosk",
    "leather",
    "marketplace",
    "musical_instrument",
    "optician",
    "pets",
    "phone",
    "photo",
    "shoes",
    "shopping_centre",
    "textiles",
    "toys",
    "alcohol",
    "bakery",
    "beverages",
    "butcher",
    "cheese",
    "chocolate",
    "confectionery",
    "coffee",
    "dairy",
    "deli",
    "drinking_water",
    "grocery",
    "organic",
    "seafood",
    "supermarket",
    "wine",

    "bar",
    "bbq",
    "biergarten",
    "cafe",
    "fast_food",
    "food_court",
    "ice_cream",
    "pub",
    "restaurant",

    "bus_stop",
    "ebike_charging",
    "travel_agency",
    "defibrillator"
  ];


  readonly atm = new FormControl();
  readonly bank = new FormControl();
  readonly bench = new FormControl();
  readonly bicycle_parking = new FormControl();
  readonly bicycle_rental = new FormControl();
  readonly cinema = new FormControl();
  readonly clinic = new FormControl();
  readonly embassy = new FormControl();
  readonly firestation = new FormControl();
  readonly fuel = new FormControl();
  readonly hospital = new FormControl();
  readonly library = new FormControl();
  readonly music_school = new FormControl();
  readonly parking = new FormControl();
  readonly pharmacy = new FormControl();
  readonly police = new FormControl();
  readonly post_box = new FormControl();
  readonly post_office = new FormControl();
  readonly school_college = new FormControl();
  readonly taxi = new FormControl();
  readonly theatre = new FormControl();
  readonly toilets = new FormControl();
  readonly university = new FormControl();
  readonly place_of_worship = new FormControl();
  readonly church = new FormControl();
  readonly mosque = new FormControl();
  readonly buddhist_temple = new FormControl();
  readonly hindu_temple = new FormControl();
  readonly synagogue = new FormControl();
  readonly cemetery = new FormControl();
  readonly arts_centre = new FormControl();
  readonly artwork = new FormControl();
  readonly attraction = new FormControl();
  readonly casino = new FormControl();
  readonly gallery = new FormControl();
  readonly heritage = new FormControl();
  readonly historic = new FormControl();
  readonly castle = new FormControl();
  readonly monument_memorial = new FormControl();
  readonly statue = new FormControl();
  readonly information = new FormControl();
  readonly monumental_tree = new FormControl();
  readonly museum = new FormControl();
  readonly picnic = new FormControl();
  readonly theme_park = new FormControl();
  readonly viewpoint = new FormControl();
  readonly vineyard = new FormControl();
  readonly windmill = new FormControl();
  readonly watermill = new FormControl();
  readonly zoo = new FormControl();
  readonly tourism = new FormControl();

  readonly alpine_hut = new FormControl();
  readonly apartment = new FormControl();
  readonly camp_site = new FormControl();
  readonly chalet = new FormControl();
  readonly guest_house = new FormControl();
  readonly hostel = new FormControl();
  readonly hotel = new FormControl();
  readonly motel = new FormControl();
  readonly sauna = new FormControl();

  readonly american_football = new FormControl();
  readonly baseball = new FormControl();
  readonly basketball = new FormControl();
  readonly cycling = new FormControl();
  readonly gymnastics = new FormControl();
  readonly golf = new FormControl();
  readonly hockey = new FormControl();
  readonly horse_racing = new FormControl();
  readonly ice_hockey = new FormControl();
  readonly soccer = new FormControl();
  readonly sports_centre = new FormControl();
  readonly surfing = new FormControl();
  readonly swimming = new FormControl();
  readonly tennis = new FormControl();
  readonly volleyball = new FormControl();

  readonly beauty = new FormControl();
  readonly bicycle = new FormControl();
  readonly books_stationary = new FormControl();
  readonly car = new FormControl();
  readonly chemist = new FormControl();
  readonly clothes = new FormControl();
  readonly copyshop = new FormControl();
  readonly cosmetics = new FormControl();
  readonly department_store = new FormControl();
  readonly diy_hardware = new FormControl();
  readonly garden_centre = new FormControl();
  readonly general = new FormControl();
  readonly gift = new FormControl();
  readonly hairdresser = new FormControl();
  readonly jewelry = new FormControl();
  readonly kiosk = new FormControl();
  readonly leather = new FormControl();
  readonly marketplace = new FormControl();
  readonly musical_instrument = new FormControl();
  readonly optician = new FormControl();
  readonly pets = new FormControl();
  readonly phone = new FormControl();
  readonly photo = new FormControl();
  readonly shoes = new FormControl();
  readonly shopping_centre = new FormControl();
  readonly textiles = new FormControl();
  readonly toys = new FormControl();
  readonly alcohol = new FormControl();
  readonly bakery = new FormControl();
  readonly beverages = new FormControl();
  readonly butcher = new FormControl();
  readonly cheese = new FormControl();
  readonly chocolate = new FormControl();
  readonly confectionery = new FormControl();
  readonly coffee = new FormControl();
  readonly dairy = new FormControl();
  readonly deli = new FormControl();
  readonly drinking_water = new FormControl();
  readonly grocery = new FormControl();
  readonly organic = new FormControl();
  readonly seafood = new FormControl();
  readonly supermarket = new FormControl();
  readonly wine = new FormControl();

  readonly bar = new FormControl();
  readonly bbq = new FormControl();
  readonly biergarten = new FormControl();
  readonly cafe = new FormControl();
  readonly fast_food = new FormControl();
  readonly food_court = new FormControl();
  readonly ice_cream = new FormControl();
  readonly pub = new FormControl();
  readonly restaurant = new FormControl();

  readonly bus_stop = new FormControl();
  readonly ebike_charging = new FormControl();
  readonly travel_agency = new FormControl();
  readonly defibrillator = new FormControl();

  constructor(private fb: FormBuilder) {
    this.form = this.buildForm();
  }


  private buildForm() {
    return this.fb.group(
      {
        atm: this.atm,
        bank: this.bank,
        bench: this.bench,
        bicycle_parking: this.bicycle_parking,
        bicycle_rental: this.bicycle_rental,
        cinema: this.cinema,
        clinic: this.clinic,
        embassy: this.embassy,
        firestation: this.firestation,
        fuel: this.fuel,
        hospital: this.hospital,
        library: this.library,
        music_school: this.music_school,
        parking: this.parking,
        pharmacy: this.pharmacy,
        police: this.police,
        post_box: this.post_box,
        post_office: this.post_office,
        school_college: this.school_college,
        taxi: this.taxi,
        theatre: this.theatre,
        toilets: this.toilets,
        university: this.university,
        place_of_worship: this.place_of_worship,
        church: this.church,
        mosque: this.mosque,
        buddhist_temple: this.buddhist_temple,
        hindu_temple: this.hindu_temple,
        synagogue: this.synagogue,
        cemetery: this.cemetery,
        arts_centre: this.arts_centre,
        artwork: this.artwork,
        attraction: this.attraction,
        casino: this.casino,
        gallery: this.gallery,
        heritage: this.heritage,
        historic: this.historic,
        castle: this.castle,
        monument_memorial: this.monument_memorial,
        statue: this.statue,
        information: this.information,
        monumental_tree: this.monumental_tree,
        museum: this.museum,
        picnic: this.picnic,
        theme_park: this.theme_park,
        viewpoint: this.viewpoint,
        vineyard: this.vineyard,
        windmill: this.windmill,
        watermill: this.watermill,
        zoo: this.zoo,
        tourism: this.tourism,

        alpine_hut: this.alpine_hut,
        apartment: this.apartment,
        camp_site: this.camp_site,
        chalet: this.chalet,
        guest_house: this.guest_house,
        hostel: this.hostel,
        hotel: this.hotel,
        motel: this.motel,
        sauna: this.sauna,

        american_football: this.american_football,
        baseball: this.baseball,
        basketball: this.basketball,
        cycling: this.cycling,
        gymnastics: this.gymnastics,
        golf: this.golf,
        hockey: this.hockey,
        horse_racing: this.horse_racing,
        ice_hockey: this.ice_hockey,
        soccer: this.soccer,
        sports_centre: this.sports_centre,
        surfing: this.surfing,
        swimming: this.swimming,
        tennis: this.tennis,
        volleyball: this.volleyball,

        beauty: this.beauty,
        bicycle: this.bicycle,
        books_stationary: this.books_stationary,
        car: this.car,
        chemist: this.chemist,
        clothes: this.clothes,
        copyshop: this.copyshop,
        cosmetics: this.cosmetics,
        department_store: this.department_store,
        diy_hardware: this.diy_hardware,
        garden_centre: this.garden_centre,
        general: this.general,
        gift: this.gift,
        hairdresser: this.hairdresser,
        jewelry: this.jewelry,
        kiosk: this.kiosk,
        leather: this.leather,
        marketplace: this.marketplace,
        musical_instrument: this.musical_instrument,
        optician: this.optician,
        pets: this.pets,
        phone: this.phone,
        photo: this.photo,
        shoes: this.shoes,
        shopping_centre: this.shopping_centre,
        textiles: this.textiles,
        toys: this.toys,
        alcohol: this.alcohol,
        bakery: this.bakery,
        beverages: this.beverages,
        butcher: this.butcher,
        cheese: this.cheese,
        chocolate: this.chocolate,
        confectionery: this.confectionery,
        coffee: this.coffee,
        dairy: this.dairy,
        deli: this.deli,
        drinking_water: this.drinking_water,
        grocery: this.grocery,
        organic: this.organic,
        seafood: this.seafood,
        supermarket: this.supermarket,
        wine: this.wine,

        bar: this.bar,
        bbq: this.bbq,
        biergarten: this.biergarten,
        cafe: this.cafe,
        fast_food: this.fast_food,
        food_court: this.food_court,
        ice_cream: this.ice_cream,
        pub: this.pub,
        restaurant: this.restaurant,

        bus_stop: this.bus_stop,
        ebike_charging: this.ebike_charging,
        travel_agency: this.travel_agency,
        defibrillator: this.defibrillator
      }
    );
  }

}
