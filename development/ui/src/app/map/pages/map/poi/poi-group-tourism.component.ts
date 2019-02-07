import {Component} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";

@Component({
  selector: 'kpn-poi-group-tourism',
  template: `
    <div [formGroup]="form">
      <kpn-poi-group name="Tourism" i18n-name="@@poi.group.tourism">
        <kpn-poi-config formControlName="arts_centre" name="Arts centre" i18n-name="@@poi.arts_centre"></kpn-poi-config>
        <kpn-poi-config formControlName="artwork" name="Artwork" i18n-name="@@poi.artwork"></kpn-poi-config>
        <kpn-poi-config formControlName="attraction" name="Attraction" i18n-name="@@poi.attraction"></kpn-poi-config>
        <kpn-poi-config formControlName="casino" name="Casino" i18n-name="@@poi.casino"></kpn-poi-config>
        <kpn-poi-config formControlName="gallery" name="Gallery" i18n-name="@@poi.gallery"></kpn-poi-config>
        <kpn-poi-config formControlName="heritage" name="Heritage" i18n-name="@@poi.heritage"></kpn-poi-config>
        <kpn-poi-config formControlName="historic" name="Historic" i18n-name="@@poi.historic"></kpn-poi-config>
        <kpn-poi-config formControlName="castle" name="Castle" i18n-name="@@poi.castle"></kpn-poi-config>
        <kpn-poi-config formControlName="monument_memorial" name="Monument" i18n-name="@@poi.monument_memorial"></kpn-poi-config>
        <kpn-poi-config formControlName="statue" name="Statue" i18n-name="@@poi.statue"></kpn-poi-config>
        <kpn-poi-config formControlName="information" name="Information" i18n-name="@@poi.information"></kpn-poi-config>
        <kpn-poi-config formControlName="monumental_tree" name="Monumental tree" i18n-name="@@poi.monumental_tree"></kpn-poi-config>
        <kpn-poi-config formControlName="museum" name="Museum" i18n-name="@@poi.museum"></kpn-poi-config>
        <kpn-poi-config formControlName="picnic" name="Picnic" i18n-name="@@poi.picnic"></kpn-poi-config>
        <kpn-poi-config formControlName="theme_park" name="Themepark" i18n-name="@@poi.theme_park"></kpn-poi-config>
        <kpn-poi-config formControlName="viewpoint" name="Viewpoint" i18n-name="@@poi.viewpoint"></kpn-poi-config>
        <kpn-poi-config formControlName="vineyard" name="Vineyard" i18n-name="@@poi.vineyard"></kpn-poi-config>
        <kpn-poi-config formControlName="windmill" name="Windmill" i18n-name="@@poi.windmill"></kpn-poi-config>
        <kpn-poi-config formControlName="watermill" name="Watermill" i18n-name="@@poi.watermill"></kpn-poi-config>
        <kpn-poi-config formControlName="zoo" name="Zoo" i18n-name="@@poi.zoo"></kpn-poi-config>
        <kpn-poi-config formControlName="tourism" name="Tourism" i18n-name="@@poi.tourism"></kpn-poi-config>
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
