import {Component} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";

@Component({
  selector: 'kpn-poi-group-various',
  template: `
    <div [formGroup]="form">
      <kpn-poi-group name="Various" i18n-name="@@poi.group.various">
        <kpn-poi-config formControlName="bus_stop" name="Bus stop" i18n-name="@@poi.bus_stop"></kpn-poi-config>
        <kpn-poi-config formControlName="ebike_charging" name="Ebike charging" i18n-name="@@poi.ebike_charging"></kpn-poi-config>
        <kpn-poi-config formControlName="travel_agency" name="Travelagency" i18n-name="@@poi.travel_agency"></kpn-poi-config>
        <kpn-poi-config formControlName="defibrillator" name="Defibrillator" i18n-name="@@poi.defibrillator"></kpn-poi-config>
      </kpn-poi-group>
    </div>
  `
})
export class PoiGroupVariousComponent {

  readonly form: FormGroup;

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
        bus_stop: this.bus_stop,
        ebike_charging: this.ebike_charging,
        travel_agency: this.travel_agency,
        defibrillator: this.defibrillator
      }
    );
  }

}
