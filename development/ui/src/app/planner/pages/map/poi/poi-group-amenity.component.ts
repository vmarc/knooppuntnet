import {Component} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";

@Component({
  selector: 'kpn-poi-group-amenity',
  template: `
    <div [formGroup]="form">
      <kpn-poi-group name="Amenity" i18n-name="@@poi.group.amenity">
        <kpn-poi-config formControlName="atm" name="Atm" i18n-name="@@poi.atm"></kpn-poi-config>
        <kpn-poi-config formControlName="bank" name="Bank" i18n-name="@@poi.bank"></kpn-poi-config>
        <kpn-poi-config formControlName="bench" name="Bench" i18n-name="@@poi.bench"></kpn-poi-config>
        <kpn-poi-config formControlName="bicycle_parking" name="Bicycle parking" i18n-name="@@poi.bicycle_parking"></kpn-poi-config>
        <kpn-poi-config formControlName="bicycle_rental" name="Bicycle rental" i18n-name="@@poi.bicycle_rental"></kpn-poi-config>
        <kpn-poi-config formControlName="cinema" name="Cinema" i18n-name="@@poi.cinema"></kpn-poi-config>
        <kpn-poi-config formControlName="clinic" name="Clinic" i18n-name="@@poi.clinic"></kpn-poi-config>
        <kpn-poi-config formControlName="embassy" name="Embassy" i18n-name="@@poi.embassy"></kpn-poi-config>
        <kpn-poi-config formControlName="firestation" name="Firestation" i18n-name="@@poi.firestation"></kpn-poi-config>
        <kpn-poi-config formControlName="fuel" name="Fuel" i18n-name="@@poi.fuel"></kpn-poi-config>
        <kpn-poi-config formControlName="hospital" name="Hospital" i18n-name="@@poi.hospital"></kpn-poi-config>
        <kpn-poi-config formControlName="library" name="Library" i18n-name="@@poi.library"></kpn-poi-config>
        <kpn-poi-config formControlName="music_school" name="Music school" i18n-name="@@poi.music_school"></kpn-poi-config>
        <kpn-poi-config formControlName="parking" name="Parking" i18n-name="@@poi.parking"></kpn-poi-config>
        <kpn-poi-config formControlName="pharmacy" name="Pharmacy" i18n-name="@@poi.pharmacy"></kpn-poi-config>
        <kpn-poi-config formControlName="police" name="Police" i18n-name="@@poi.police"></kpn-poi-config>
        <kpn-poi-config formControlName="post_box" name="Post box" i18n-name="@@poi.post_box"></kpn-poi-config>
        <kpn-poi-config formControlName="post_office" name="Post office" i18n-name="@@poi.post_office"></kpn-poi-config>
        <!--    <kpn-poi-config name="school_college" i18n-name="@@poi.school_college"></kpn-poi-config> -->
        <kpn-poi-config formControlName="taxi" name="Taxi" i18n-name="@@poi.taxi"></kpn-poi-config>
        <kpn-poi-config formControlName="theatre" name="Theatre" i18n-name="@@poi.theatre"></kpn-poi-config>
        <kpn-poi-config formControlName="toilets" name="Toilets" i18n-name="@@poi.toilets"></kpn-poi-config>
        <kpn-poi-config formControlName="university" name="University" i18n-name="@@poi.university"></kpn-poi-config>
        <kpn-poi-config formControlName="place_of_worship" name="Place of worship" i18n-name="@@poi.place_of_worship"></kpn-poi-config>
        <kpn-poi-config formControlName="church" name="Church" i18n-name="@@poi.church"></kpn-poi-config>
        <kpn-poi-config formControlName="mosque" name="Mosque" i18n-name="@@poi.mosque"></kpn-poi-config>
        <kpn-poi-config formControlName="buddhist_temple" name="Buddhist temple" i18n-name="@@poi.buddhist_temple"></kpn-poi-config>
        <kpn-poi-config formControlName="hindu_temple" name="Hindu temple" i18n-name="@@poi.hindu_temple"></kpn-poi-config>
        <kpn-poi-config formControlName="synagogue" name="Synagogue" i18n-name="@@poi.synagogue"></kpn-poi-config>
        <kpn-poi-config formControlName="cemetery" name="Cemetery" i18n-name="@@poi.cemetery"></kpn-poi-config>
      </kpn-poi-group>
    </div>
  `
})
export class PoiGroupAmenityComponent {

  readonly form: FormGroup;

  readonly atm = new FormControl();
  readonly bank = new FormControl();
  readonly bench = new FormControl();
  readonly bicycle_parking = new FormControl();
  readonly bicycle_rental = new FormControl();
  readonly cinema = new FormControl();
  readonly clinic = new FormControl();
  readonly embassy = new FormControl();
  readonly firestation = new FormControl();
  readonly fuel = new FormControl();
  readonly hospital = new FormControl();
  readonly library = new FormControl();
  readonly music_school = new FormControl();
  readonly parking = new FormControl();
  readonly pharmacy = new FormControl();
  readonly police = new FormControl();
  readonly post_box = new FormControl();
  readonly post_office = new FormControl();
  readonly school_college = new FormControl();
  readonly taxi = new FormControl();
  readonly theatre = new FormControl();
  readonly toilets = new FormControl();
  readonly university = new FormControl();
  readonly place_of_worship = new FormControl();
  readonly church = new FormControl();
  readonly mosque = new FormControl();
  readonly buddhist_temple = new FormControl();
  readonly hindu_temple = new FormControl();
  readonly synagogue = new FormControl();
  readonly cemetery = new FormControl();

  constructor(private fb: FormBuilder) {
    this.form = this.buildForm();
  }

  private buildForm() {
    return this.fb.group(
      {
        atm: this.atm,
        bank: this.bank,
        bench: this.bench,
        bicycle_parking: this.bicycle_parking,
        bicycle_rental: this.bicycle_rental,
        cinema: this.cinema,
        clinic: this.clinic,
        embassy: this.embassy,
        firestation: this.firestation,
        fuel: this.fuel,
        hospital: this.hospital,
        library: this.library,
        music_school: this.music_school,
        parking: this.parking,
        pharmacy: this.pharmacy,
        police: this.police,
        post_box: this.post_box,
        post_office: this.post_office,
        school_college: this.school_college,
        taxi: this.taxi,
        theatre: this.theatre,
        toilets: this.toilets,
        university: this.university,
        place_of_worship: this.place_of_worship,
        church: this.church,
        mosque: this.mosque,
        buddhist_temple: this.buddhist_temple,
        hindu_temple: this.hindu_temple,
        synagogue: this.synagogue,
        cemetery: this.cemetery
      }
    );
  }

}
