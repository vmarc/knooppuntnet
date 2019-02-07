import {Component} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";

@Component({
  selector: 'kpn-poi-group-places-to-stay',
  template: `
    <div [formGroup]="form">
      <kpn-poi-group name="Places to stay" i18n-name="@@poi.group.places-to-stay">
        <kpn-poi-config formControlName="alpine_hut" name="Alpine hut" i18n-name="@@poi.alpine_hut"></kpn-poi-config>
        <kpn-poi-config formControlName="apartment" name="Apartment" i18n-name="@@poi.apartment"></kpn-poi-config>
        <kpn-poi-config formControlName="camp_site" name="Camp site" i18n-name="@@poi.camp_site"></kpn-poi-config>
        <kpn-poi-config formControlName="chalet" name="Chalet" i18n-name="@@poi.chalet"></kpn-poi-config>
        <kpn-poi-config formControlName="guest_house" name="Guest house" i18n-name="@@poi.guest_house"></kpn-poi-config>
        <kpn-poi-config formControlName="hostel" name="Hostel" i18n-name="@@poi.hostel"></kpn-poi-config>
        <kpn-poi-config formControlName="hotel" name="Hotel" i18n-name="@@poi.hotel"></kpn-poi-config>
        <kpn-poi-config formControlName="motel" name="Motel" i18n-name="@@poi.motel"></kpn-poi-config>
        <!--<kpn-poi-config name="spa" i18n-name="@@poi.spa"></kpn-poi-config>-->
        <kpn-poi-config formControlName="sauna" name="Sauna" i18n-name="@@poi.sauna"></kpn-poi-config>
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
