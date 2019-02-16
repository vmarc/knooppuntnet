import {Component} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";

@Component({
  selector: 'kpn-poi-group-sports',
  template: `
    <div [formGroup]="form">
      <kpn-poi-group name="Sports" i18n-name="@@poi.group.sports">
        <kpn-poi-config formControlName="american_football"></kpn-poi-config>
        <kpn-poi-config formControlName="baseball"></kpn-poi-config>
        <kpn-poi-config formControlName="basketball"></kpn-poi-config>
        <kpn-poi-config formControlName="cycling"></kpn-poi-config>
        <kpn-poi-config formControlName="gymnastics"></kpn-poi-config>
        <kpn-poi-config formControlName="golf"></kpn-poi-config>
        <kpn-poi-config formControlName="hockey"></kpn-poi-config>
        <kpn-poi-config formControlName="horse_racing"></kpn-poi-config>
        <kpn-poi-config formControlName="ice_hockey"></kpn-poi-config>
        <kpn-poi-config formControlName="soccer"></kpn-poi-config>
        <kpn-poi-config formControlName="sports_centre"></kpn-poi-config>
        <kpn-poi-config formControlName="surfing"></kpn-poi-config>
        <kpn-poi-config formControlName="swimming"></kpn-poi-config>
        <kpn-poi-config formControlName="tennis"></kpn-poi-config>
        <kpn-poi-config formControlName="volleyball"></kpn-poi-config>
      </kpn-poi-group>
    </div>
  `
})
export class PoiGroupSportsComponent {

  readonly form: FormGroup;

  readonly american_football = new FormControl();
  readonly baseball = new FormControl();
  readonly basketball = new FormControl();
  readonly cycling = new FormControl();
  readonly gymnastics = new FormControl();
  readonly golf = new FormControl();
  readonly hockey = new FormControl();
  readonly horse_racing = new FormControl();
  readonly ice_hockey = new FormControl();
  readonly soccer = new FormControl();
  readonly sports_centre = new FormControl();
  readonly surfing = new FormControl();
  readonly swimming = new FormControl();
  readonly tennis = new FormControl();
  readonly volleyball = new FormControl();

  constructor(private fb: FormBuilder) {
    this.form = this.buildForm();
  }

  private buildForm() {
    return this.fb.group(
      {
        american_football: this.american_football,
        baseball: this.baseball,
        basketball: this.basketball,
        cycling: this.cycling,
        gymnastics: this.gymnastics,
        golf: this.golf,
        hockey: this.hockey,
        horse_racing: this.horse_racing,
        ice_hockey: this.ice_hockey,
        soccer: this.soccer,
        sports_centre: this.sports_centre,
        surfing: this.surfing,
        swimming: this.swimming,
        tennis: this.tennis,
        volleyball: this.volleyball
      }
    );
  }

}
