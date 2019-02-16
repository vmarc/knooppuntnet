import {Component} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";

@Component({
  selector: 'kpn-poi-group-restaurants',
  template: `
    <div [formGroup]="form">
      <kpn-poi-group name="Restaurants" i18n-name="@@poi.group.restaurants">
        <kpn-poi-config formControlName="bar"></kpn-poi-config>
        <kpn-poi-config formControlName="bbq"></kpn-poi-config>
        <kpn-poi-config formControlName="biergarten"></kpn-poi-config>
        <kpn-poi-config formControlName="cafe"></kpn-poi-config>
        <kpn-poi-config formControlName="fast_food"></kpn-poi-config>
        <kpn-poi-config formControlName="food_court"></kpn-poi-config>
        <kpn-poi-config formControlName="ice_cream"></kpn-poi-config>
        <kpn-poi-config formControlName="pub"></kpn-poi-config>
        <kpn-poi-config formControlName="restaurant"></kpn-poi-config>
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
