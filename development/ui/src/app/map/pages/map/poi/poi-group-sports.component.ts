import {Component} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";

@Component({
  selector: 'kpn-poi-group-sports',
  template: `
    <div [formGroup]="form">
      <kpn-poi-group name="Sports" i18n-name="@@poi.group.sports">
        <kpn-poi-config formControlName="american_football" name="American football" i18n-name="@@poi.american_football"></kpn-poi-config>
        <kpn-poi-config formControlName="baseball" name="Baseball" i18n-name="@@poi.baseball"></kpn-poi-config>
        <kpn-poi-config formControlName="basketball" name="Basketball" i18n-name="@@poi.basketball"></kpn-poi-config>
        <kpn-poi-config formControlName="cycling" name="Cycling" i18n-name="@@poi.cycling"></kpn-poi-config>
        <kpn-poi-config formControlName="gymnastics" name="Gymnastics" i18n-name="@@poi.gymnastics"></kpn-poi-config>
        <kpn-poi-config formControlName="golf" name="Golf" i18n-name="@@poi.golf"></kpn-poi-config>
        <kpn-poi-config formControlName="hockey" name="Hockey" i18n-name="@@poi.hockey"></kpn-poi-config>
        <kpn-poi-config formControlName="horse_racing" name="Horse racing" i18n-name="@@poi.horse_racing"></kpn-poi-config>
        <kpn-poi-config formControlName="ice_hockey" name="Ice hockey" i18n-name="@@poi.ice_hockey"></kpn-poi-config>
        <kpn-poi-config formControlName="soccer" name="Soccer" i18n-name="@@poi.soccer"></kpn-poi-config>
        <kpn-poi-config formControlName="sports_centre" name="Sports centre" i18n-name="@@poi.sports_centre"></kpn-poi-config>
        <kpn-poi-config formControlName="surfing" name="Surfing" i18n-name="@@poi.surfing"></kpn-poi-config>
        <kpn-poi-config formControlName="swimming" name="Swimming" i18n-name="@@poi.swimming"></kpn-poi-config>
        <kpn-poi-config formControlName="tennis" name="Tennis" i18n-name="@@poi.tennis"></kpn-poi-config>
        <kpn-poi-config formControlName="volleyball" name="Volleyball" i18n-name="@@poi.volleyball"></kpn-poi-config>
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
