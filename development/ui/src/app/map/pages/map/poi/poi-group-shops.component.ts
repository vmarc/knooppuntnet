import {Component} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";

@Component({
  selector: 'kpn-poi-group-shops',
  template: `
    <div [formGroup]="form">
      <kpn-poi-group name="Shops" i18n-name="@@poi.group.shops">
        <kpn-poi-config formControlName="beauty" name="Beauty" i18n-name="@@poi.beauty"></kpn-poi-config>
        <kpn-poi-config formControlName="bicycle" name="Bicycle" i18n-name="@@poi.bicycle"></kpn-poi-config>
        <kpn-poi-config formControlName="books_stationary" name="Books/stationary" i18n-name="@@poi.books_stationary"></kpn-poi-config>
        <kpn-poi-config formControlName="car" name="Car" i18n-name="@@poi.car"></kpn-poi-config>
        <kpn-poi-config formControlName="chemist" name="Chemist" i18n-name="@@poi.chemist"></kpn-poi-config>
        <kpn-poi-config formControlName="clothes" name="Clothes" i18n-name="@@poi.clothes"></kpn-poi-config>
        <kpn-poi-config formControlName="copyshop" name="Copyshop" i18n-name="@@poi.copyshop"></kpn-poi-config>
        <kpn-poi-config formControlName="cosmetics" name="Cosmetics" i18n-name="@@poi.cosmetics"></kpn-poi-config>
        <kpn-poi-config formControlName="department_store" name="Department store" i18n-name="@@poi.department_store"></kpn-poi-config>
        <kpn-poi-config formControlName="diy_hardware" name="DIY hardware" i18n-name="@@poi.diy_hardware"></kpn-poi-config>
        <kpn-poi-config formControlName="garden_centre" name="Garden centre" i18n-name="@@poi.garden_centre"></kpn-poi-config>
        <kpn-poi-config formControlName="general" name="General" i18n-name="@@poi.general"></kpn-poi-config>
        <kpn-poi-config formControlName="gift" name="Gift" i18n-name="@@poi.gift"></kpn-poi-config>
        <kpn-poi-config formControlName="hairdresser" name="Hairdresser" i18n-name="@@poi.hairdresser"></kpn-poi-config>
        <kpn-poi-config formControlName="jewelry" name="Jewelry" i18n-name="@@poi.jewelry"></kpn-poi-config>
        <kpn-poi-config formControlName="kiosk" name="Kiosk" i18n-name="@@poi.kiosk"></kpn-poi-config>
        <kpn-poi-config formControlName="leather" name="Leather" i18n-name="@@poi.leather"></kpn-poi-config>
        <kpn-poi-config formControlName="marketplace" name="Marketplace" i18n-name="@@poi.marketplace"></kpn-poi-config>
        <kpn-poi-config formControlName="musical_instrument" name="Musical instrument" i18n-name="@@poi.musical_instrument"></kpn-poi-config>
        <kpn-poi-config formControlName="optician" name="Optician" i18n-name="@@poi.optician"></kpn-poi-config>
        <kpn-poi-config formControlName="pets" name="Pets" i18n-name="@@poi.pets"></kpn-poi-config>
        <kpn-poi-config formControlName="phone" name="Phone" i18n-name="@@poi.phone"></kpn-poi-config>
        <kpn-poi-config formControlName="photo" name="Photo" i18n-name="@@poi.photo"></kpn-poi-config>
        <kpn-poi-config formControlName="shoes" name="Shoes" i18n-name="@@poi.shoes"></kpn-poi-config>
        <kpn-poi-config formControlName="shopping_centre" name="Shopping centre" i18n-name="@@poi.shopping_centre"></kpn-poi-config>
        <kpn-poi-config formControlName="textiles" name="Textiles" i18n-name="@@poi.textiles"></kpn-poi-config>
        <kpn-poi-config formControlName="toys" name="Toys" i18n-name="@@poi.toys"></kpn-poi-config>
      </kpn-poi-group>
    </div>
  `
})
export class PoiGroupShopsComponent {

  readonly form: FormGroup;

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

  constructor(private fb: FormBuilder) {
    this.form = this.buildForm();
  }

  private buildForm() {
    return this.fb.group(
      {
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
        toys: this.toys
      }
    );
  }

}
