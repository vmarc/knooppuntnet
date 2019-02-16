import {Component, Input} from '@angular/core';

@Component({
  selector: 'kpn-poi-name',
  template: `
    <ng-container [ngSwitch]="name">

      <ng-container *ngSwitchCase="'alcohol'" i18n="@@poi.alcohol">Alcohol</ng-container>
      <ng-container *ngSwitchCase="'alpine_hut'" i18n="@@poi.alpine_hut">Alpine hut</ng-container>
      <ng-container *ngSwitchCase="'american_football'" i18n="@@poi.american_football">American football</ng-container>
      <ng-container *ngSwitchCase="'apartment'" i18n="@@poi.apartment">Apartment</ng-container>
      <ng-container *ngSwitchCase="'arts_centre'" i18n="@@poi.arts_centre">Arts centre</ng-container>
      <ng-container *ngSwitchCase="'artwork'" i18n="@@poi.artwork">Artwork</ng-container>
      <ng-container *ngSwitchCase="'atm'" i18n="@@poi.atm">Atm</ng-container>
      <ng-container *ngSwitchCase="'attraction'" i18n="@@poi.attraction">Attraction</ng-container>
      <ng-container *ngSwitchCase="'bakery'" i18n="@@poi.bakery">Bakery</ng-container>
      <ng-container *ngSwitchCase="'bank'" i18n="@@poi.bank">Bank</ng-container>
      <ng-container *ngSwitchCase="'bar'" i18n="@@poi.bar">Bar</ng-container>
      <ng-container *ngSwitchCase="'baseball'" i18n="@@poi.baseball">Baseball</ng-container>
      <ng-container *ngSwitchCase="'basketball'" i18n="@@poi.basketball">Basketball</ng-container>
      <ng-container *ngSwitchCase="'bbq'" i18n="@@poi.bbq">BBQ</ng-container>
      <ng-container *ngSwitchCase="'beauty'" i18n="@@poi.beauty">Beauty</ng-container>
      <ng-container *ngSwitchCase="'bench'" i18n="@@poi.bench">Bench</ng-container>
      <ng-container *ngSwitchCase="'beverages'" i18n="@@poi.beverages">Beverages</ng-container>
      <ng-container *ngSwitchCase="'bicycle'" i18n="@@poi.bicycle">Bicycle</ng-container>
      <ng-container *ngSwitchCase="'bicycle_parking'" i18n="@@poi.bicycle_parking">Bicycle parking</ng-container>
      <ng-container *ngSwitchCase="'bicycle_rental'" i18n="@@poi.bicycle_rental">Bicycle rental</ng-container>
      <ng-container *ngSwitchCase="'biergarten'" i18n="@@poi.biergarten">Biergarten</ng-container>
      <ng-container *ngSwitchCase="'books_stationary'" i18n="@@poi.books_stationary">Books/stationary</ng-container>
      <ng-container *ngSwitchCase="'buddhist_temple'" i18n="@@poi.buddhist_temple">Buddhist temple</ng-container>
      <ng-container *ngSwitchCase="'bus_stop'" i18n="@@poi.bus_stop">Bus stop</ng-container>
      <ng-container *ngSwitchCase="'butcher'" i18n="@@poi.butcher">Butcher</ng-container>
      <ng-container *ngSwitchCase="'cafe'" i18n="@@poi.cafe">Cafe</ng-container>
      <ng-container *ngSwitchCase="'camp_site'" i18n="@@poi.camp_site">Camp site</ng-container>
      <ng-container *ngSwitchCase="'car'" i18n="@@poi.car">Car</ng-container>
      <ng-container *ngSwitchCase="'casino'" i18n="@@poi.casino">Casino</ng-container>
      <ng-container *ngSwitchCase="'castle'" i18n="@@poi.castle">Castle</ng-container>
      <ng-container *ngSwitchCase="'cemetery'" i18n="@@poi.cemetery">Cemetery</ng-container>
      <ng-container *ngSwitchCase="'chalet'" i18n="@@poi.chalet">Chalet</ng-container>
      <ng-container *ngSwitchCase="'cheese'" i18n="@@poi.cheese">Cheese</ng-container>
      <ng-container *ngSwitchCase="'chemist'" i18n="@@poi.chemist">Chemist</ng-container>
      <ng-container *ngSwitchCase="'chocolate'" i18n="@@poi.chocolate">Chocolate</ng-container>
      <ng-container *ngSwitchCase="'church'" i18n="@@poi.church">Church</ng-container>
      <ng-container *ngSwitchCase="'cinema'" i18n="@@poi.cinema">Cinema</ng-container>
      <ng-container *ngSwitchCase="'clinic'" i18n="@@poi.clinic">Clinic</ng-container>
      <ng-container *ngSwitchCase="'clothes'" i18n="@@poi.clothes">Clothes</ng-container>
      <ng-container *ngSwitchCase="'coffee'" i18n="@@poi.coffee">Coffee</ng-container>
      <ng-container *ngSwitchCase="'confectionery'" i18n="@@poi.confectionery">Confectionery</ng-container>
      <ng-container *ngSwitchCase="'copyshop'" i18n="@@poi.copyshop">Copyshop</ng-container>
      <ng-container *ngSwitchCase="'cosmetics'" i18n="@@poi.cosmetics">Cosmetics</ng-container>
      <ng-container *ngSwitchCase="'cycling'" i18n="@@poi.cycling">Cycling</ng-container>
      <ng-container *ngSwitchCase="'dairy'" i18n="@@poi.dairy">Dairy</ng-container>
      <ng-container *ngSwitchCase="'defibrillator'" i18n="@@poi.defibrillator">Defibrillator</ng-container>
      <ng-container *ngSwitchCase="'deli'" i18n="@@poi.deli">Deli</ng-container>
      <ng-container *ngSwitchCase="'department_store'" i18n="@@poi.department_store">Department store</ng-container>
      <ng-container *ngSwitchCase="'diy_hardware'" i18n="@@poi.diy_hardware">DIY hardware</ng-container>
      <ng-container *ngSwitchCase="'drinking_water'" i18n="@@poi.drinking_water">Drinking water</ng-container>
      <ng-container *ngSwitchCase="'ebike_charging'" i18n="@@poi.ebike_charging">Ebike charging</ng-container>
      <ng-container *ngSwitchCase="'embassy'" i18n="@@poi.embassy">Embassy</ng-container>
      <ng-container *ngSwitchCase="'fast_food'" i18n="@@poi.fast_food">Fast food</ng-container>
      <ng-container *ngSwitchCase="'firestation'" i18n="@@poi.firestation">Firestation</ng-container>
      <ng-container *ngSwitchCase="'food_court'" i18n="@@poi.food_court">Food court</ng-container>
      <ng-container *ngSwitchCase="'fuel'" i18n="@@poi.fuel">Fuel</ng-container>
      <ng-container *ngSwitchCase="'gallery'" i18n="@@poi.gallery">Gallery</ng-container>
      <ng-container *ngSwitchCase="'garden_centre'" i18n="@@poi.garden_centre">Garden centre</ng-container>
      <ng-container *ngSwitchCase="'general'" i18n="@@poi.general">General</ng-container>
      <ng-container *ngSwitchCase="'gift'" i18n="@@poi.gift">Gift</ng-container>
      <ng-container *ngSwitchCase="'golf'" i18n="@@poi.golf">Golf</ng-container>
      <ng-container *ngSwitchCase="'grocery'" i18n="@@poi.grocery">Grocery</ng-container>
      <ng-container *ngSwitchCase="'guest_house'" i18n="@@poi.guest_house">Guest house</ng-container>
      <ng-container *ngSwitchCase="'gymnastics'" i18n="@@poi.gymnastics">Gymnastics</ng-container>
      <ng-container *ngSwitchCase="'hairdresser'" i18n="@@poi.hairdresser">Hairdresser</ng-container>
      <ng-container *ngSwitchCase="'heritage'" i18n="@@poi.heritage">Heritage</ng-container>
      <ng-container *ngSwitchCase="'hindu_temple'" i18n="@@poi.hindu_temple">Hindu temple</ng-container>
      <ng-container *ngSwitchCase="'historic'" i18n="@@poi.historic">Historic</ng-container>
      <ng-container *ngSwitchCase="'hockey'" i18n="@@poi.hockey">Hockey</ng-container>
      <ng-container *ngSwitchCase="'horse_racing'" i18n="@@poi.horse_racing">Horse racing</ng-container>
      <ng-container *ngSwitchCase="'hospital'" i18n="@@poi.hospital">Hospital</ng-container>
      <ng-container *ngSwitchCase="'hostel'" i18n="@@poi.hostel">Hostel</ng-container>
      <ng-container *ngSwitchCase="'hotel'" i18n="@@poi.hotel">Hotel</ng-container>
      <ng-container *ngSwitchCase="'ice_cream'" i18n="@@poi.ice_cream">Ice cream</ng-container>
      <ng-container *ngSwitchCase="'ice_hockey'" i18n="@@poi.ice_hockey">Ice hockey</ng-container>
      <ng-container *ngSwitchCase="'information'" i18n="@@poi.information">Information</ng-container>
      <ng-container *ngSwitchCase="'jewelry'" i18n="@@poi.jewelry">Jewelry</ng-container>
      <ng-container *ngSwitchCase="'kiosk'" i18n="@@poi.kiosk">Kiosk</ng-container>
      <ng-container *ngSwitchCase="'leather'" i18n="@@poi.leather">Leather</ng-container>
      <ng-container *ngSwitchCase="'library'" i18n="@@poi.library">Library</ng-container>
      <ng-container *ngSwitchCase="'marketplace'" i18n="@@poi.marketplace">Marketplace</ng-container>
      <ng-container *ngSwitchCase="'monumental_tree'" i18n="@@poi.monumental_tree">Monumental tree</ng-container>
      <ng-container *ngSwitchCase="'monument_memorial'" i18n="@@poi.monument_memorial">Monument</ng-container>
      <ng-container *ngSwitchCase="'mosque'" i18n="@@poi.mosque">Mosque</ng-container>
      <ng-container *ngSwitchCase="'motel'" i18n="@@poi.motel">Motel</ng-container>
      <ng-container *ngSwitchCase="'museum'" i18n="@@poi.museum">Museum</ng-container>
      <ng-container *ngSwitchCase="'musical_instrument'" i18n="@@poi.musical_instrument">Musical instrument</ng-container>
      <ng-container *ngSwitchCase="'music_school'" i18n="@@poi.music_school">Music school</ng-container>
      <ng-container *ngSwitchCase="'optician'" i18n="@@poi.optician">Optician</ng-container>
      <ng-container *ngSwitchCase="'organic'" i18n="@@poi.organic">Organic</ng-container>
      <ng-container *ngSwitchCase="'parking'" i18n="@@poi.parking">Parking</ng-container>
      <ng-container *ngSwitchCase="'pets'" i18n="@@poi.pets">Pets</ng-container>
      <ng-container *ngSwitchCase="'pharmacy'" i18n="@@poi.pharmacy">Pharmacy</ng-container>
      <ng-container *ngSwitchCase="'phone'" i18n="@@poi.phone">Phone</ng-container>
      <ng-container *ngSwitchCase="'photo'" i18n="@@poi.photo">Photo</ng-container>
      <ng-container *ngSwitchCase="'picnic'" i18n="@@poi.picnic">Picnic</ng-container>
      <ng-container *ngSwitchCase="'place_of_worship'" i18n="@@poi.place_of_worship">Place of worship</ng-container>
      <ng-container *ngSwitchCase="'police'" i18n="@@poi.police">Police</ng-container>
      <ng-container *ngSwitchCase="'post_box'" i18n="@@poi.post_box">Post box</ng-container>
      <ng-container *ngSwitchCase="'post_office'" i18n="@@poi.post_office">Post office</ng-container>
      <ng-container *ngSwitchCase="'pub'" i18n="@@poi.pub">Pub</ng-container>
      <ng-container *ngSwitchCase="'restaurant'" i18n="@@poi.restaurant">Restaurant</ng-container>
      <ng-container *ngSwitchCase="'sauna'" i18n="@@poi.sauna">Sauna</ng-container>
      <ng-container *ngSwitchCase="'seafood'" i18n="@@poi.seafood">Seafood</ng-container>
      <ng-container *ngSwitchCase="'shoes'" i18n="@@poi.shoes">Shoes</ng-container>
      <ng-container *ngSwitchCase="'shopping_centre'" i18n="@@poi.shopping_centre">Shopping centre</ng-container>
      <ng-container *ngSwitchCase="'soccer'" i18n="@@poi.soccer">Soccer</ng-container>
      <ng-container *ngSwitchCase="'sports_centre'" i18n="@@poi.sports_centre">Sports centre</ng-container>
      <ng-container *ngSwitchCase="'statue'" i18n="@@poi.statue">Statue</ng-container>
      <ng-container *ngSwitchCase="'supermarket'" i18n="@@poi.supermarket">Supermarket</ng-container>
      <ng-container *ngSwitchCase="'surfing'" i18n="@@poi.surfing">Surfing</ng-container>
      <ng-container *ngSwitchCase="'swimming'" i18n="@@poi.swimming">Swimming</ng-container>
      <ng-container *ngSwitchCase="'synagogue'" i18n="@@poi.synagogue">Synagogue</ng-container>
      <ng-container *ngSwitchCase="'taxi'" i18n="@@poi.taxi">Taxi</ng-container>
      <ng-container *ngSwitchCase="'tennis'" i18n="@@poi.tennis">Tennis</ng-container>
      <ng-container *ngSwitchCase="'textiles'" i18n="@@poi.textiles">Textiles</ng-container>
      <ng-container *ngSwitchCase="'theatre'" i18n="@@poi.theatre">Theatre</ng-container>
      <ng-container *ngSwitchCase="'theme_park'" i18n="@@poi.theme_park">Themepark</ng-container>
      <ng-container *ngSwitchCase="'toilets'" i18n="@@poi.toilets">Toilets</ng-container>
      <ng-container *ngSwitchCase="'tourism'" i18n="@@poi.tourism">Tourism</ng-container>
      <ng-container *ngSwitchCase="'toys'" i18n="@@poi.toys">Toys</ng-container>
      <ng-container *ngSwitchCase="'travel_agency'" i18n="@@poi.travel_agency">Travelagency</ng-container>
      <ng-container *ngSwitchCase="'university'" i18n="@@poi.university">University</ng-container>
      <ng-container *ngSwitchCase="'viewpoint'" i18n="@@poi.viewpoint">Viewpoint</ng-container>
      <ng-container *ngSwitchCase="'vineyard'" i18n="@@poi.vineyard">Vineyard</ng-container>
      <ng-container *ngSwitchCase="'volleyball'" i18n="@@poi.volleyball">Volleyball</ng-container>
      <ng-container *ngSwitchCase="'watermill'" i18n="@@poi.watermill">Watermill</ng-container>
      <ng-container *ngSwitchCase="'windmill'" i18n="@@poi.windmill">Windmill</ng-container>
      <ng-container *ngSwitchCase="'wine'" i18n="@@poi.wine">Wine</ng-container>
      <ng-container *ngSwitchCase="'zoo'" i18n="@@poi.zoo">Zoo</ng-container>

      <!-- "school_college" i18n="@@poi.school_college" -->
      <!-- "spa" i18n="@@poi.spa" -->

      <ng-container *ngSwitchDefault>{{name}}</ng-container>

    </ng-container>

  `
})
export class PoiNameComponent {
  @Input() name;

}
