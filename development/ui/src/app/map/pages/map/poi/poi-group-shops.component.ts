import {Component} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";

@Component({
  selector: 'kpn-poi-group-shops',
  template: `
    <div [formGroup]="form">
      <kpn-poi-group name="Shops" i18n-name="@@poi.group.shops">
        <kpn-poi-config formControlName="beauty"></kpn-poi-config>
        <kpn-poi-config formControlName="bicycle"></kpn-poi-config>
        <kpn-poi-config formControlName="books_stationary"></kpn-poi-config>
        <kpn-poi-config formControlName="car"></kpn-poi-config>
        <kpn-poi-config formControlName="chemist"></kpn-poi-config>
        <kpn-poi-config formControlName="clothes"></kpn-poi-config>
        <kpn-poi-config formControlName="copyshop"></kpn-poi-config>
        <kpn-poi-config formControlName="cosmetics"></kpn-poi-config>
        <kpn-poi-config formControlName="department_store"></kpn-poi-config>
        <kpn-poi-config formControlName="diy_hardware"></kpn-poi-config>
        <kpn-poi-config formControlName="garden_centre"></kpn-poi-config>
        <kpn-poi-config formControlName="general"></kpn-poi-config>
        <kpn-poi-config formControlName="gift"></kpn-poi-config>
        <kpn-poi-config formControlName="hairdresser"></kpn-poi-config>
        <kpn-poi-config formControlName="jewelry"></kpn-poi-config>
        <kpn-poi-config formControlName="kiosk"></kpn-poi-config>
        <kpn-poi-config formControlName="leather"></kpn-poi-config>
        <kpn-poi-config formControlName="marketplace"></kpn-poi-config>
        <kpn-poi-config formControlName="musical_instrument"></kpn-poi-config>
        <kpn-poi-config formControlName="optician"></kpn-poi-config>
        <kpn-poi-config formControlName="pets"></kpn-poi-config>
        <kpn-poi-config formControlName="phone"></kpn-poi-config>
        <kpn-poi-config formControlName="photo"></kpn-poi-config>
        <kpn-poi-config formControlName="shoes"></kpn-poi-config>
        <kpn-poi-config formControlName="shopping_centre"></kpn-poi-config>
        <kpn-poi-config formControlName="textiles"></kpn-poi-config>
        <kpn-poi-config formControlName="toys"></kpn-poi-config>
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
