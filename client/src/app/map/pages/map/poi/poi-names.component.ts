import {ChangeDetectionStrategy} from "@angular/core";
import {AfterViewInit, ChangeDetectorRef, Component, ElementRef} from "@angular/core";
import {PoiService} from "../../../../services/poi.service";

@Component({
  selector: "kpn-poi-names",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="!isRegistryUpdated()">
      <span id="alcohol" i18n="@@poi.alcohol">Alcohol</span>
      <span id="alpine-hut" i18n="@@poi.alpine-hut">Alpine hut</span>
      <span id="american-football" i18n="@@poi.american-football">American football</span>
      <span id="apartment" i18n="@@poi.apartment">Apartment</span>
      <span id="arts-centre" i18n="@@poi.arts-centre">Arts centre</span>
      <span id="artwork" i18n="@@poi.artwork">Artwork</span>
      <span id="atm" i18n="@@poi.atm">Atm</span>
      <span id="attraction" i18n="@@poi.attraction">Attraction</span>
      <span id="bakery" i18n="@@poi.bakery">Bakery</span>
      <span id="bank" i18n="@@poi.bank">Bank</span>
      <span id="bar" i18n="@@poi.bar">Bar</span>
      <span id="baseball" i18n="@@poi.baseball">Baseball</span>
      <span id="basketball" i18n="@@poi.basketball">Basketball</span>
      <span id="bbq" i18n="@@poi.bbq">BBQ</span>
      <span id="beauty" i18n="@@poi.beauty">Beauty</span>
      <span id="bench" i18n="@@poi.bench">Bench</span>
      <span id="beverages" i18n="@@poi.beverages">Beverages</span>
      <span id="bicycle" i18n="@@poi.bicycle">Bicycle</span>
      <span id="bicycle-parking" i18n="@@poi.bicycle-parking">Bicycle parking</span>
      <span id="bicycle-rental" i18n="@@poi.bicycle-rental">Bicycle rental</span>
      <span id="bicycle-rental-2" i18n="@@poi.bicycle-rental">Bicycle rental</span>
      <span id="biergarten" i18n="@@poi.biergarten">Biergarten</span>
      <span id="books-stationary" i18n="@@poi.books-stationary">Books/stationary</span>
      <span id="boundary-stone" i18n="@@poi.boundary-stone">Boundary stone</span>
      <span id="buddhist-temple" i18n="@@poi.buddhist-temple">Buddhist temple</span>
      <span id="busstop" i18n="@@poi.busstop">Bus stop</span>
      <span id="butcher" i18n="@@poi.butcher">Butcher</span>
      <span id="cafe" i18n="@@poi.cafe">Cafe</span>
      <span id="campsite" i18n="@@poi.campsite">Camp site</span>
      <span id="car" i18n="@@poi.car">Car</span>
      <span id="casino" i18n="@@poi.casino">Casino</span>
      <span id="castle" i18n="@@poi.castle">Castle</span>
      <span id="cemetery" i18n="@@poi.cemetery">Cemetery</span>
      <span id="chalet" i18n="@@poi.chalet">Chalet</span>
      <span id="cheese" i18n="@@poi.cheese">Cheese</span>
      <span id="chemist" i18n="@@poi.chemist">Chemist</span>
      <span id="chocolate" i18n="@@poi.chocolate">Chocolate</span>
      <span id="church" i18n="@@poi.church">Church</span>
      <span id="cinema" i18n="@@poi.cinema">Cinema</span>
      <span id="clinic" i18n="@@poi.clinic">Clinic</span>
      <span id="clothes" i18n="@@poi.clothes">Clothes</span>
      <span id="coffee" i18n="@@poi.coffee">Coffee</span>
      <span id="confectionery" i18n="@@poi.confectionery">Confectionery</span>
      <span id="copyshop" i18n="@@poi.copyshop">Copyshop</span>
      <span id="cosmetics" i18n="@@poi.cosmetics">Cosmetics</span>
      <span id="cycling" i18n="@@poi.cycling">Cycling</span>
      <span id="dairy" i18n="@@poi.dairy">Dairy</span>
      <span id="defibrillator" i18n="@@poi.defibrillator">Defibrillator</span>
      <span id="deli" i18n="@@poi.deli">Deli</span>
      <span id="departmentstore" i18n="@@poi.departmentstore">Department store</span>
      <span id="diy-hardware" i18n="@@poi.diy-hardware">DIY hardware</span>
      <span id="drinking-water" i18n="@@poi.drinking-water">Drinking water</span>
      <span id="ebike-charging" i18n="@@poi.ebike-charging">Ebike charging</span>
      <span id="embassy" i18n="@@poi.embassy">Embassy</span>
      <span id="fastfood" i18n="@@poi.fastfood">Fast food</span>
      <span id="firestation" i18n="@@poi.firestation">Firestation</span>
      <span id="foodcourt" i18n="@@poi.foodcourt">Food court</span>
      <span id="fuel" i18n="@@poi.fuel">Fuel</span>
      <span id="gallery" i18n="@@poi.gallery">Gallery</span>
      <span id="garden-centre" i18n="@@poi.garden-centre">Garden centre</span>
      <span id="general" i18n="@@poi.general">General</span>
      <span id="gift" i18n="@@poi.gift">Gift</span>
      <span id="golf" i18n="@@poi.golf">Golf</span>
      <span id="grocery" i18n="@@poi.grocery">Grocery</span>
      <span id="guesthouse" i18n="@@poi.guesthouse">Guest house</span>
      <span id="gymnastics" i18n="@@poi.gymnastics">Gymnastics</span>
      <span id="hairdresser" i18n="@@poi.hairdresser">Hairdresser</span>
      <span id="heritage" i18n="@@poi.heritage">Heritage</span>
      <span id="hindu-temple" i18n="@@poi.hindu-temple">Hindu temple</span>
      <span id="historic" i18n="@@poi.historic">Historic</span>
      <span id="hockey" i18n="@@poi.hockey">Hockey</span>
      <span id="horseracing" i18n="@@poi.horseracing">Horse racing</span>
      <span id="hospital" i18n="@@poi.hospital">Hospital</span>
      <span id="hostel" i18n="@@poi.hostel">Hostel</span>
      <span id="hotel" i18n="@@poi.hotel">Hotel</span>
      <span id="icecream" i18n="@@poi.ice-cream">Ice cream</span>
      <span id="icehockey" i18n="@@poi.icehockey">Ice hockey</span>
      <span id="information" i18n="@@poi.information">Information</span>
      <span id="jewelry" i18n="@@poi.jewelry">Jewelry</span>
      <span id="kiosk" i18n="@@poi.kiosk">Kiosk</span>
      <span id="leather" i18n="@@poi.leather">Leather</span>
      <span id="library" i18n="@@poi.library">Library</span>
      <span id="marketplace" i18n="@@poi.marketplace">Marketplace</span>
      <span id="monumental-tree" i18n="@@poi.monumental-tree">Monumental tree</span>
      <span id="monument-memorial" i18n="@@poi.monument-memorial">Monument</span>
      <span id="mosque" i18n="@@poi.mosque">Mosque</span>
      <span id="motel" i18n="@@poi.motel">Motel</span>
      <span id="museum" i18n="@@poi.museum">Museum</span>
      <span id="musical-instrument" i18n="@@poi.musical-instrument">Musical instrument</span>
      <span id="musicschool" i18n="@@poi.musicschool">Music school</span>
      <span id="optician" i18n="@@poi.optician">Optician</span>
      <span id="organic" i18n="@@poi.organic">Organic</span>
      <span id="parking" i18n="@@poi.parking">Parking</span>
      <span id="pets" i18n="@@poi.pets">Pets</span>
      <span id="pharmacy" i18n="@@poi.pharmacy">Pharmacy</span>
      <span id="phone" i18n="@@poi.phone">Phone</span>
      <span id="photo" i18n="@@poi.photo">Photo</span>
      <span id="picnic" i18n="@@poi.picnic">Picnic</span>
      <span id="place-of-worship" i18n="@@poi.place-of-worship">Place of worship</span>
      <span id="police" i18n="@@poi.police">Police</span>
      <span id="postbox" i18n="@@poi.postbox">Post box</span>
      <span id="postoffice" i18n="@@poi.postoffice">Post office</span>
      <span id="pub" i18n="@@poi.pub">Pub</span>
      <span id="restaurant" i18n="@@poi.restaurant">Restaurant</span>
      <span id="sauna" i18n="@@poi.sauna">Sauna</span>
      <span id="seafood" i18n="@@poi.seafood">Seafood</span>
      <span id="shoes" i18n="@@poi.shoes">Shoes</span>
      <span id="shoppingcentre" i18n="@@poi.shoppingcentre">Shopping centre</span>
      <span id="soccer" i18n="@@poi.soccer">Soccer</span>
      <span id="sportscentre" i18n="@@poi.sportscentre">Sports centre</span>
      <span id="statue" i18n="@@poi.statue">Statue</span>
      <span id="supermarket" i18n="@@poi.supermarket">Supermarket</span>
      <span id="surfing" i18n="@@poi.surfing">Surfing</span>
      <span id="swimming" i18n="@@poi.swimming">Swimming</span>
      <span id="synagogue" i18n="@@poi.synagogue">Synagogue</span>
      <span id="taxi" i18n="@@poi.taxi">Taxi</span>
      <span id="tennis" i18n="@@poi.tennis">Tennis</span>
      <span id="textiles" i18n="@@poi.textiles">Textiles</span>
      <span id="theatre" i18n="@@poi.theatre">Theatre</span>
      <span id="themepark" i18n="@@poi.themepark">Themepark</span>
      <span id="toilets" i18n="@@poi.toilets">Toilets</span>
      <span id="tourism" i18n="@@poi.tourism">Tourism</span>
      <span id="toys" i18n="@@poi.toys">Toys</span>
      <span id="travelagency" i18n="@@poi.travelagency">Travelagency</span>
      <span id="university" i18n="@@poi.university">University</span>
      <span id="viewpoint" i18n="@@poi.viewpoint">Viewpoint</span>
      <span id="vineyard" i18n="@@poi.vineyard">Vineyard</span>
      <span id="volleyball" i18n="@@poi.volleyball">Volleyball</span>
      <span id="watermill" i18n="@@poi.watermill">Watermill</span>
      <span id="wayside-shrine" i18n="@@poi.wayside-shrine">Wayside shrine</span>
      <span id="windmill" i18n="@@poi.windmill">Windmill</span>
      <span id="wine" i18n="@@poi.wine">Wine</span>
      <span id="zoo" i18n="@@poi.zoo">Zoo</span>
      <!-- "school-college" i18n="@@poi.school-college" -->
      <!-- "spa" i18n="@@poi.spa" -->
    </div>
  `,
  styles: [`
    :host {
      display: none;
    }
  `]
})
export class PoiNamesComponent implements AfterViewInit {

  constructor(private element: ElementRef,
              private poiService: PoiService,
              private cdr: ChangeDetectorRef) {
  }

  ngAfterViewInit(): void {
    const divElement = this.element.nativeElement.children[0];
    if (divElement != null) {
      const poiElements = divElement.children;
      this.poiService.updatePoiNameRegistry(poiElements);
      this.cdr.detectChanges();
    }
  }

  isRegistryUpdated(): boolean {
    return this.poiService.isPoiNameRegistryUpdated();
  }
}
