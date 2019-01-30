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

      <kpn-poi-config icon="atm-2.png" formControlName="atm" name="atm" i18n-name="@@poi.atm"></kpn-poi-config>
      <kpn-poi-config icon="bank.png" formControlName="bank" name="bank" i18n-name="@@poi.bank"></kpn-poi-config>
      <kpn-poi-config icon="letter_b.png" formControlName="bench" name="bench" i18n-name="@@poi.bench"></kpn-poi-config>
      <kpn-poi-config icon="parking_bicycle-2.png" formControlName="bicycle_parking" name="bicycle_parking" i18n-name="@@poi.bicycle_parking"></kpn-poi-config>
      <kpn-poi-config icon="cycling.png" name="bicycle_rental" formControlName="bicycle_rental" i18n-name="@@poi.bicycle_rental"></kpn-poi-config>
      <kpn-poi-config icon="cinema.png" name="cinema" formControlName="cinema" i18n-name="@@poi.cinema"></kpn-poi-config>
      <kpn-poi-config icon="firstaid.png" name="clinic" formControlName="clinic" i18n-name="@@poi.clinic"></kpn-poi-config>
      <kpn-poi-config icon="embassy.png" name="embassy" formControlName="embassy" i18n-name="@@poi.embassy"></kpn-poi-config>
      <kpn-poi-config icon="firemen.png" name="firestation" formControlName="firestation" i18n-name="@@poi.firestation"></kpn-poi-config>
      <kpn-poi-config icon="fillingstation.png" name="fuel" formControlName="fuel" i18n-name="@@poi.fuel"></kpn-poi-config>
      <kpn-poi-config icon="hospital-building.png" name="hospital" formControlName="hospital" i18n-name="@@poi.hospital"></kpn-poi-config>
      <kpn-poi-config icon="library.png" name="library" formControlName="library" i18n-name="@@poi.library"></kpn-poi-config>
      <kpn-poi-config icon="musicschool.png" name="music_school" formControlName="music_school" i18n-name="@@poi.music_school"></kpn-poi-config>
      <kpn-poi-config icon="parkinggarage.png" name="parking" formControlName="parking" i18n-name="@@poi.parking"></kpn-poi-config>
      <kpn-poi-config icon="medicine.png" name="pharmacy" formControlName="pharmacy" i18n-name="@@poi.pharmacy"></kpn-poi-config>
      <kpn-poi-config icon="police.png" name="police" formControlName="police" i18n-name="@@poi.police"></kpn-poi-config>
      <kpn-poi-config icon="postal2.png" name="post_box" formControlName="post_box" i18n-name="@@poi.post_box"></kpn-poi-config>
      <kpn-poi-config icon="postal.png" name="post_office" formControlName="post_office" i18n-name="@@poi.post_office"></kpn-poi-config>
      <!--    <kpn-poi-config icon="" name="school_college" i18n-name="@@poi.school_college"></kpn-poi-config> -->
      <kpn-poi-config icon="taxi.png" name="taxi" formControlName="taxi" i18n-name="@@poi.taxi"></kpn-poi-config>
      <kpn-poi-config icon="theater.png" name="taxi" formControlName="theatre" i18n-name="@@poi.theatre"></kpn-poi-config>
      <kpn-poi-config icon="toilets.png" name="toilets" formControlName="toilets" i18n-name="@@poi.toilets"></kpn-poi-config>
      <kpn-poi-config icon="university.png" name="university" formControlName="university" i18n-name="@@poi.university"></kpn-poi-config>
      <kpn-poi-config icon="church-2.png" name="place_of_worship" formControlName="place_of_worship" i18n-name="@@poi.place_of_worship"></kpn-poi-config>
      <kpn-poi-config icon="chapel-2.png" name="church" formControlName="church" i18n-name="@@poi.church"></kpn-poi-config>
      <kpn-poi-config icon="mosquee.png" name="mosque" formControlName="mosque" i18n-name="@@poi.mosque"></kpn-poi-config>
      <kpn-poi-config icon="bouddha.png" name="buddhist_temple" formControlName="buddhist_temple" i18n-name="@@poi.buddhist_temple"></kpn-poi-config>
      <kpn-poi-config icon="templehindu.png" name="hindu_temple" formControlName="hindu_temple" i18n-name="@@poi.hindu_temple"></kpn-poi-config>
      <kpn-poi-config icon="synagogue-2.png" name="synagogue" formControlName="synagogue" i18n-name="@@poi.synagogue"></kpn-poi-config>
      <kpn-poi-config icon="cemetary.png" name="cemetery" formControlName="cemetery" i18n-name="@@poi.cemetery"></kpn-poi-config>

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

      <kpn-poi-config icon="letter_a.png" name="arts_centre" formControlName="arts_centre" i18n-name="@@poi.arts_centre"></kpn-poi-config>
      <kpn-poi-config icon="artwork.png" name="artwork" formControlName="artwork" i18n-name="@@poi.artwork"></kpn-poi-config>
      <kpn-poi-config icon="star.png" name="attraction" formControlName="attraction" i18n-name="@@poi.attraction"></kpn-poi-config>
      <kpn-poi-config icon="casino.png" name="casino" formControlName="casino" i18n-name="@@poi.casino"></kpn-poi-config>
      <kpn-poi-config icon="artgallery.png" name="gallery" formControlName="gallery" i18n-name="@@poi.gallery"></kpn-poi-config>
      <kpn-poi-config icon="worldheritagesite.png" name="heritage" formControlName="heritage" i18n-name="@@poi.heritage"></kpn-poi-config>
      <kpn-poi-config icon="star-3.png" name="historic" formControlName="historic" i18n-name="@@poi.historic"></kpn-poi-config>
      <kpn-poi-config icon="castle-2.png" name="castle" formControlName="castle" i18n-name="@@poi.castle"></kpn-poi-config>
      <kpn-poi-config icon="memorial.png" name="monument_memorial" formControlName="monument_memorial" i18n-name="@@poi.monument_memorial"></kpn-poi-config>
      <kpn-poi-config icon="statue-2.png" name="statue" formControlName="statue" i18n-name="@@poi.statue"></kpn-poi-config>
      <kpn-poi-config icon="information.png" name="information" formControlName="information" i18n-name="@@poi.information"></kpn-poi-config>
      <kpn-poi-config icon="tree.png" name="monumental_tree" formControlName="monumental_tree" i18n-name="@@poi.monumental_tree"></kpn-poi-config>
      <kpn-poi-config icon="museum_art.png" name="museum" formControlName="museum" i18n-name="@@poi.museum"></kpn-poi-config>
      <kpn-poi-config icon="picnic-2.png" name="picnic" formControlName="picnic" i18n-name="@@poi.picnic"></kpn-poi-config>
      <kpn-poi-config icon="themepark.png" name="theme_park" formControlName="theme_park" i18n-name="@@poi.theme_park"></kpn-poi-config>
      <kpn-poi-config icon="viewpoint.png" name="viewpoint" formControlName="viewpoint" i18n-name="@@poi.viewpoint"></kpn-poi-config>
      <kpn-poi-config icon="vineyard.png" name="vineyard" formControlName="vineyard" i18n-name="@@poi.vineyard"></kpn-poi-config>
      <kpn-poi-config icon="windmill-2.png" name="windmill" formControlName="windmill" i18n-name="@@poi.windmill"></kpn-poi-config>
      <kpn-poi-config icon="watermill-2.png" name="watermill" formControlName="watermill" i18n-name="@@poi.watermill"></kpn-poi-config>
      <kpn-poi-config icon="zoo.png" name="zoo" formControlName="zoo" i18n-name="@@poi.zoo"></kpn-poi-config>
      <kpn-poi-config icon="sight-2.png" name="tourism" formControlName="tourism" i18n-name="@@poi.tourism"></kpn-poi-config>

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

      <kpn-poi-config icon="alpinehut.png" name="alpine_hut" formControlName="alpine_hut" i18n-name="@@poi.alpine_hut"></kpn-poi-config>
      <kpn-poi-config icon="apartment-3.png" name="apartment" formControlName="apartment" i18n-name="@@poi.apartment"></kpn-poi-config>
      <kpn-poi-config icon="camping-2.png" name="camp_site" formControlName="camp_site" i18n-name="@@poi.camp_site"></kpn-poi-config>
      <kpn-poi-config icon="letter_c.png" name="chalet" formControlName="chalet" i18n-name="@@poi.chalet"></kpn-poi-config>
      <kpn-poi-config icon="bed_breakfast.png" name="guest_house" formControlName="guest_house" i18n-name="@@poi.guest_house"></kpn-poi-config>
      <kpn-poi-config icon="hostel_0star.png" name="hostel" formControlName="hostel" i18n-name="@@poi.hostel"></kpn-poi-config>
      <kpn-poi-config icon="hotel_0star.png" name="hotel" formControlName="hotel" i18n-name="@@poi.hotel"></kpn-poi-config>
      <kpn-poi-config icon="motel-2.png" name="motel" formControlName="motel" i18n-name="@@poi.motel"></kpn-poi-config>
      <!--<kpn-poi-config icon="spa" name="spa" i18n-name="@@poi.spa"></kpn-poi-config>-->
      <kpn-poi-config icon="sauna.png" name="sauna" formControlName="sauna" i18n-name="@@poi.sauna"></kpn-poi-config>

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

      <kpn-poi-config icon="usfootball.png" name="american_football" formControlName="american_football" i18n-name="@@poi.american_football"></kpn-poi-config>
      <kpn-poi-config icon="baseball.png" name="baseball" formControlName="baseball" i18n-name="@@poi.baseball"></kpn-poi-config>
      <kpn-poi-config icon="basketball.png" name="basketball" formControlName="basketball" i18n-name="@@poi.basketball"></kpn-poi-config>
      <kpn-poi-config icon="cycling.png" name="cycling" formControlName="cycling" i18n-name="@@poi.cycling"></kpn-poi-config>
      <kpn-poi-config icon="gymnastics.png" name="gymnastics" formControlName="gymnastics" i18n-name="@@poi.gymnastics"></kpn-poi-config>
      <kpn-poi-config icon="golfing.png" name="golf" formControlName="golf" i18n-name="@@poi.golf"></kpn-poi-config>
      <kpn-poi-config icon="hockey.png" name="hockey" formControlName="hockey" i18n-name="@@poi.hockey"></kpn-poi-config>
      <kpn-poi-config icon="horseriding.png" name="horse_racing" formControlName="horse_racing" i18n-name="@@poi.horse_racing"></kpn-poi-config>
      <kpn-poi-config icon="icehockey.png" name="ice_hockey" formControlName="ice_hockey" i18n-name="@@poi.ice_hockey"></kpn-poi-config>
      <kpn-poi-config icon="icehockey.png" name="ice_hockey" formControlName="ice_hockey" i18n-name="@@poi.ice_hockey"></kpn-poi-config>
      <kpn-poi-config icon="soccer.png" name="soccer" formControlName="soccer" i18n-name="@@poi.soccer"></kpn-poi-config>
      <kpn-poi-config icon="indoor-arena.png" name="sports_centre" formControlName="sports_centre" i18n-name="@@poi.sports_centre"></kpn-poi-config>
      <kpn-poi-config icon="surfing.png" name="surfing" formControlName="surfing" i18n-name="@@poi.surfing"></kpn-poi-config>
      <kpn-poi-config icon="swimming.png" name="swimming" formControlName="swimming" i18n-name="@@poi.swimming"></kpn-poi-config>
      <kpn-poi-config icon="tennis.png" name="tennis" formControlName="tennis" i18n-name="@@poi.tennis"></kpn-poi-config>
      <kpn-poi-config icon="volleyball.png" name="volleyball" formControlName="volleyball" i18n-name="@@poi.volleyball"></kpn-poi-config>

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

      <kpn-poi-config icon="beautysalon.png" name="beauty" formControlName="beauty" i18n-name="@@poi.beauty"></kpn-poi-config>
      <kpn-poi-config icon="bicycle_shop.png" name="bicycle" formControlName="bicycle" i18n-name="@@poi.bicycle"></kpn-poi-config>
      <kpn-poi-config icon="library.png" name="books_stationary" formControlName="books_stationary" i18n-name="@@poi.books_stationary"></kpn-poi-config>
      <kpn-poi-config icon="car.png" name="car" formControlName="car" i18n-name="@@poi.car"></kpn-poi-config>
      <kpn-poi-config icon="drugstore.png" name="chemist" formControlName="chemist" i18n-name="@@poi.chemist"></kpn-poi-config>
      <kpn-poi-config icon="clothers_female.png" name="clothes" formControlName="clothes" i18n-name="@@poi.clothes"></kpn-poi-config>
      <kpn-poi-config icon="letter_c.png" name="copyshop" formControlName="copyshop" i18n-name="@@poi.copyshop"></kpn-poi-config>
      <kpn-poi-config icon="perfumery.png" name="cosmetics" formControlName="cosmetics" i18n-name="@@poi.cosmetics"></kpn-poi-config>
      <kpn-poi-config icon="departmentstore.png" name="department_store" formControlName="department_store" i18n-name="@@poi.department_store"></kpn-poi-config>
      <kpn-poi-config icon="tools.png" name="diy_hardware" formControlName="diy_hardware" i18n-name="@@poi.diy_hardware"></kpn-poi-config>
      <kpn-poi-config icon="flowers-1.png" name="garden_centre" formControlName="garden_centre" i18n-name="@@poi.garden_centre"></kpn-poi-config>
      <kpn-poi-config icon="letter_g.png" name="general" formControlName="general" i18n-name="@@poi.general"></kpn-poi-config>
      <kpn-poi-config icon="gifts.png" name="gift" formControlName="gift" i18n-name="@@poi.gift"></kpn-poi-config>
      <kpn-poi-config icon="barber.png" name="hairdresser" formControlName="hairdresser" i18n-name="@@poi.hairdresser"></kpn-poi-config>
      <kpn-poi-config icon="jewelry.png" name="jewelry" formControlName="jewelry" i18n-name="@@poi.jewelry"></kpn-poi-config>
      <kpn-poi-config icon="kiosk.png" name="kiosk" formControlName="kiosk" i18n-name="@@poi.kiosk"></kpn-poi-config>
      <kpn-poi-config icon="bags.png" name="leather" formControlName="leather" i18n-name="@@poi.leather"></kpn-poi-config>
      <kpn-poi-config icon="market.png" name="marketplace" formControlName="marketplace" i18n-name="@@poi.marketplace"></kpn-poi-config>
      <kpn-poi-config icon="music_rock.png" name="musical_instrument" formControlName="musical_instrument" i18n-name="@@poi.musical_instrument"></kpn-poi-config>
      <kpn-poi-config icon="glasses.png" name="optician" formControlName="optician" i18n-name="@@poi.optician"></kpn-poi-config>
      <kpn-poi-config icon="pets.png" name="pets" formControlName="pets" i18n-name="@@poi.pets"></kpn-poi-config>
      <kpn-poi-config icon="phones.png" name="phone" formControlName="phone" i18n-name="@@poi.phone"></kpn-poi-config>
      <kpn-poi-config icon="photo.png" name="photo" formControlName="photo" i18n-name="@@poi.photo"></kpn-poi-config>
      <kpn-poi-config icon="highhills.png" name="shoes" formControlName="shoes" i18n-name="@@poi.shoes"></kpn-poi-config>
      <kpn-poi-config icon="mall.png" name="shopping_centre" formControlName="shopping_centre" i18n-name="@@poi.shopping_centre"></kpn-poi-config>
      <kpn-poi-config icon="textiles.png" name="textiles" formControlName="textiles" i18n-name="@@poi.textiles"></kpn-poi-config>
      <kpn-poi-config icon="toys.png" name="toys" formControlName="toys" i18n-name="@@poi.toys"></kpn-poi-config>

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

      <kpn-poi-config icon="liquor.png" name="alcohol" formControlName="alcohol" i18n-name="@@poi.alcohol"></kpn-poi-config>
      <kpn-poi-config icon="bread.png" name="bakery" formControlName="bakery" i18n-name="@@poi.bakery"></kpn-poi-config>
      <kpn-poi-config icon="bar_coktail.png" name="beverages" formControlName="beverages" i18n-name="@@poi.beverages"></kpn-poi-config>
      <kpn-poi-config icon="butcher-2.png" name="butcher" formControlName="butcher" i18n-name="@@poi.butcher"></kpn-poi-config>
      <kpn-poi-config icon="cheese.png" name="cheese" formControlName="cheese" i18n-name="@@poi.cheese"></kpn-poi-config>
      <kpn-poi-config icon="candy.png" name="chocolate" formControlName="chocolate" i18n-name="@@poi.chocolate"></kpn-poi-config>
      <kpn-poi-config icon="candy.png" name="confectionery" formControlName="confectionery" i18n-name="@@poi.confectionery"></kpn-poi-config>
      <kpn-poi-config icon="coffee.png" name="coffee" formControlName="coffee" i18n-name="@@poi.coffee"></kpn-poi-config>
      <kpn-poi-config icon="milk_and_cookies.png" name="dairy" formControlName="dairy" i18n-name="@@poi.dairy"></kpn-poi-config>
      <kpn-poi-config icon="patisserie.png" name="deli" formControlName="deli" i18n-name="@@poi.deli"></kpn-poi-config>
      <kpn-poi-config icon="drinkingwater.png" name="drinking_water" formControlName="drinking_water" i18n-name="@@poi.drinking_water"></kpn-poi-config>
      <kpn-poi-config icon="grocery.png" formControlName="grocery" name="grocery" i18n-name="@@poi.grocery"></kpn-poi-config>
      <kpn-poi-config icon="restaurant_vegetarian.png" formControlName="organic" name="organic" i18n-name="@@poi.organic"></kpn-poi-config>
      <kpn-poi-config icon="restaurant_fish.png" name="seafood" formControlName="seafood" i18n-name="@@poi.seafood"></kpn-poi-config>
      <kpn-poi-config icon="supermarket.png" name="supermarket" formControlName="supermarket" i18n-name="@@poi.supermarket"></kpn-poi-config>
      <kpn-poi-config icon="winebar.png" name="wine" formControlName="wine" i18n-name="@@poi.wine"></kpn-poi-config>

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

      <kpn-poi-config icon="bar.png" formControlName="bar" name="bar" i18n-name="@@poi.bar"></kpn-poi-config>
      <kpn-poi-config icon="letter_b.png" formControlName="bbq" name="bbq" i18n-name="@@poi.bbq"></kpn-poi-config>
      <kpn-poi-config icon="number_1.png" formControlName="biergarten" name="biergarten" i18n-name="@@poi.biergarten"></kpn-poi-config>
      <kpn-poi-config icon="cafetaria.png" formControlName="cafe" name="cafe" i18n-name="@@poi.cafe"></kpn-poi-config>
      <kpn-poi-config icon="fastfood.png" formControlName="fast_food" name="fast_food" i18n-name="@@poi.fast_food"></kpn-poi-config>
      <kpn-poi-config icon="letter_f.png" formControlName="food_court" name="food_court" i18n-name="@@poi.food_court"></kpn-poi-config>
      <kpn-poi-config icon="icecream.png" formControlName="ice_cream" name="ice_cream" i18n-name="@@poi.ice_cream"></kpn-poi-config>
      <kpn-poi-config icon="pub.png" formControlName="pub" name="pub" i18n-name="@@poi.pub"></kpn-poi-config>
      <kpn-poi-config icon="restaurant.png" formControlName="restaurant" name="restaurant" i18n-name="@@poi.restaurant"></kpn-poi-config>

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

      <kpn-poi-config icon="busstop.png" formControlName="bus_stop" name="bus_stop" i18n-name="@@poi.bus_stop"></kpn-poi-config>
      <kpn-poi-config icon="e-bike-charging.png" formControlName="ebike_charging" name="ebike_charging" i18n-name="@@poi.ebike_charging"></kpn-poi-config>
      <kpn-poi-config icon="travel_agency.png" formControlName="travel_agency" name="travel_agency" i18n-name="@@poi.travel_agency"></kpn-poi-config>
      <kpn-poi-config icon="aed-2.png" formControlName="defibrillator" name="defibrillator" i18n-name="@@poi.defibrillator"></kpn-poi-config>

    </div>
  `, styles: [`

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
