import {Component} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";

@Component({
  selector: 'kpn-poi-group-restaurants',
  template: `
    <div [formGroup]="form">
      <kpn-poi-group name="Restaurants" i18n-name="@@poi.group.restaurants">
        <kpn-poi-config formControlName="bar" name="Bar" i18n-name="@@poi.bar"></kpn-poi-config>
        <kpn-poi-config formControlName="bbq" name="BBQ" i18n-name="@@poi.bbq"></kpn-poi-config>
        <kpn-poi-config formControlName="biergarten" name="Biergarten" i18n-name="@@poi.biergarten"></kpn-poi-config>
        <kpn-poi-config formControlName="cafe" name="Cafe" i18n-name="@@poi.cafe"></kpn-poi-config>
        <kpn-poi-config formControlName="fast_food" name="Fast food" i18n-name="@@poi.fast_food"></kpn-poi-config>
        <kpn-poi-config formControlName="food_court" name="Food court" i18n-name="@@poi.food_court"></kpn-poi-config>
        <kpn-poi-config formControlName="ice_cream" name="Ice cream" i18n-name="@@poi.ice_cream"></kpn-poi-config>
        <kpn-poi-config formControlName="pub" name="Pub" i18n-name="@@poi.pub"></kpn-poi-config>
        <kpn-poi-config formControlName="restaurant" name="Restaurant" i18n-name="@@poi.restaurant"></kpn-poi-config>
      </kpn-poi-group>
    </div>
  `
})
export class PoiGroupRestaurantsComponent {

  readonly form: FormGroup;

  readonly bar = new FormControl();
  readonly bbq = new FormControl();
  readonly biergarten = new FormControl();
  readonly cafe = new FormControl();
  readonly fast_food = new FormControl();
  readonly food_court = new FormControl();
  readonly ice_cream = new FormControl();
  readonly pub = new FormControl();
  readonly restaurant = new FormControl();

  constructor(private fb: FormBuilder) {
    this.form = this.buildForm();
  }

  private buildForm() {
    return this.fb.group(
      {
        bar: this.bar,
        bbq: this.bbq,
        biergarten: this.biergarten,
        cafe: this.cafe,
        fast_food: this.fast_food,
        food_court: this.food_court,
        ice_cream: this.ice_cream,
        pub: this.pub,
        restaurant: this.restaurant,
      }
    );
  }

}
