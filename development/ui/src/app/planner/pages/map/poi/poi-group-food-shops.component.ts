import {Component} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";

@Component({
  selector: 'kpn-poi-group-food-shops',
  template: `
    <div [formGroup]="form">
      <kpn-poi-group name="Food shops" i18n-name="@@poi.group.food-shops">
        <kpn-poi-config formControlName="alcohol" name="Alcohol" i18n-name="@@poi.alcohol"></kpn-poi-config>
        <kpn-poi-config formControlName="bakery" name="Bakery" i18n-name="@@poi.bakery"></kpn-poi-config>
        <kpn-poi-config formControlName="beverages" name="Beverages" i18n-name="@@poi.beverages"></kpn-poi-config>
        <kpn-poi-config formControlName="butcher" name="Butcher" i18n-name="@@poi.butcher"></kpn-poi-config>
        <kpn-poi-config formControlName="cheese" name="Cheese" i18n-name="@@poi.cheese"></kpn-poi-config>
        <kpn-poi-config formControlName="chocolate" name="Chocolate" i18n-name="@@poi.chocolate"></kpn-poi-config>
        <kpn-poi-config formControlName="confectionery" name="Confectionery" i18n-name="@@poi.confectionery"></kpn-poi-config>
        <kpn-poi-config formControlName="coffee" name="Coffee" i18n-name="@@poi.coffee"></kpn-poi-config>
        <kpn-poi-config formControlName="dairy" name="Dairy" i18n-name="@@poi.dairy"></kpn-poi-config>
        <kpn-poi-config formControlName="deli" name="Deli" i18n-name="@@poi.deli"></kpn-poi-config>
        <kpn-poi-config formControlName="drinking_water" name="Drinking water" i18n-name="@@poi.drinking_water"></kpn-poi-config>
        <kpn-poi-config formControlName="grocery" name="Grocery" i18n-name="@@poi.grocery"></kpn-poi-config>
        <kpn-poi-config formControlName="organic" name="Organic" i18n-name="@@poi.organic"></kpn-poi-config>
        <kpn-poi-config formControlName="seafood" name="Seafood" i18n-name="@@poi.seafood"></kpn-poi-config>
        <kpn-poi-config formControlName="supermarket" name="Supermarket" i18n-name="@@poi.supermarket"></kpn-poi-config>
        <kpn-poi-config formControlName="wine" name="Wine" i18n-name="@@poi.wine"></kpn-poi-config>
      </kpn-poi-group>
    </div>
  `
})
export class PoiGroupFoodShopsComponent {

  readonly form: FormGroup;

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

  constructor(private fb: FormBuilder) {
    this.form = this.buildForm();
  }

  private buildForm() {
    return this.fb.group(
      {
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
        wine: this.wine
      }
    );
  }

}
