import {Component} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";

@Component({
  selector: 'kpn-poi-group-tourism',
  template: `
    <div [formGroup]="form">
      <kpn-poi-group name="Tourism" i18n-name="@@poi.group.tourism">
        <kpn-poi-config formControlName="arts_centre"></kpn-poi-config>
        <kpn-poi-config formControlName="artwork"></kpn-poi-config>
        <kpn-poi-config formControlName="attraction"></kpn-poi-config>
        <kpn-poi-config formControlName="casino"></kpn-poi-config>
        <kpn-poi-config formControlName="gallery"></kpn-poi-config>
        <kpn-poi-config formControlName="heritage"></kpn-poi-config>
        <kpn-poi-config formControlName="historic"></kpn-poi-config>
        <kpn-poi-config formControlName="castle"></kpn-poi-config>
        <kpn-poi-config formControlName="monument_memorial"></kpn-poi-config>
        <kpn-poi-config formControlName="statue"></kpn-poi-config>
        <kpn-poi-config formControlName="information"></kpn-poi-config>
        <kpn-poi-config formControlName="monumental_tree"></kpn-poi-config>
        <kpn-poi-config formControlName="museum"></kpn-poi-config>
        <kpn-poi-config formControlName="picnic"></kpn-poi-config>
        <kpn-poi-config formControlName="theme_park"></kpn-poi-config>
        <kpn-poi-config formControlName="viewpoint"></kpn-poi-config>
        <kpn-poi-config formControlName="vineyard"></kpn-poi-config>
        <kpn-poi-config formControlName="windmill"></kpn-poi-config>
        <kpn-poi-config formControlName="watermill"></kpn-poi-config>
        <kpn-poi-config formControlName="zoo"></kpn-poi-config>
        <kpn-poi-config formControlName="tourism"></kpn-poi-config>
      </kpn-poi-group>
    </div>
  `
})
export class PoiGroupTourismComponent {

  readonly form: FormGroup;

  readonly arts_centre = new FormControl();
  readonly artwork = new FormControl();
  readonly attraction = new FormControl();
  readonly casino = new FormControl();
  readonly gallery = new FormControl();
  readonly heritage = new FormControl();
  readonly historic = new FormControl();
  readonly castle = new FormControl();
  readonly monument_memorial = new FormControl();
  readonly statue = new FormControl();
  readonly information = new FormControl();
  readonly monumental_tree = new FormControl();
  readonly museum = new FormControl();
  readonly picnic = new FormControl();
  readonly theme_park = new FormControl();
  readonly viewpoint = new FormControl();
  readonly vineyard = new FormControl();
  readonly windmill = new FormControl();
  readonly watermill = new FormControl();
  readonly zoo = new FormControl();
  readonly tourism = new FormControl();

  constructor(private fb: FormBuilder) {
    this.form = this.buildForm();
  }

  private buildForm() {
    return this.fb.group(
      {
        arts_centre: this.arts_centre,
        artwork: this.artwork,
        attraction: this.attraction,
        casino: this.casino,
        gallery: this.gallery,
        heritage: this.heritage,
        historic: this.historic,
        castle: this.castle,
        monument_memorial: this.monument_memorial,
        statue: this.statue,
        information: this.information,
        monumental_tree: this.monumental_tree,
        museum: this.museum,
        picnic: this.picnic,
        theme_park: this.theme_park,
        viewpoint: this.viewpoint,
        vineyard: this.vineyard,
        windmill: this.windmill,
        watermill: this.watermill,
        zoo: this.zoo,
        tourism: this.tourism
      }
    );
  }

}
