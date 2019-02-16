import {Component} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";

@Component({
  selector: 'kpn-poi-group-places-to-stay',
  template: `
    <div [formGroup]="form">
      <kpn-poi-group name="Places to stay" i18n-name="@@poi.group.places-to-stay">
        <kpn-poi-config formControlName="alpine_hut"></kpn-poi-config>
        <kpn-poi-config formControlName="apartment"></kpn-poi-config>
        <kpn-poi-config formControlName="camp_site"></kpn-poi-config>
        <kpn-poi-config formControlName="chalet"></kpn-poi-config>
        <kpn-poi-config formControlName="guest_house"></kpn-poi-config>
        <kpn-poi-config formControlName="hostel"></kpn-poi-config>
        <kpn-poi-config formControlName="hotel"></kpn-poi-config>
        <kpn-poi-config formControlName="motel"></kpn-poi-config>
        <!--<kpn-poi-config formControlName="spa"></kpn-poi-config>-->
        <kpn-poi-config formControlName="sauna"></kpn-poi-config>
      </kpn-poi-group>
    </div>
  `
})
export class PoiGroupPlacesToStayComponent {

  readonly form: FormGroup;

  readonly alpine_hut = new FormControl();
  readonly apartment = new FormControl();
  readonly camp_site = new FormControl();
  readonly chalet = new FormControl();
  readonly guest_house = new FormControl();
  readonly hostel = new FormControl();
  readonly hotel = new FormControl();
  readonly motel = new FormControl();
  readonly sauna = new FormControl();

  constructor(private fb: FormBuilder) {
    this.form = this.buildForm();
  }

  private buildForm() {
    return this.fb.group(
      {
        alpine_hut: this.alpine_hut,
        apartment: this.apartment,
        camp_site: this.camp_site,
        chalet: this.chalet,
        guest_house: this.guest_house,
        hostel: this.hostel,
        hotel: this.hotel,
        motel: this.motel,
        sauna: this.sauna
      }
    );
  }

}
