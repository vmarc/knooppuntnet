import {Component, EventEmitter, Input, OnDestroy, OnInit, Output} from "@angular/core";
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";
import {List} from "immutable";
import {Observable} from "rxjs";
import {map, startWith} from "rxjs/operators";
import {Countries} from "../../kpn/common/countries";
import {Country} from "../../kpn/api/custom/country";
import {Subscriptions} from "../../util/Subscriptions";
import {LocationOption} from "./location-option";
import {LocationNode, locations} from "./locations";

/* tslint:disable:template-i18n work-in-progress */
@Component({
  selector: "kpn-location-selector",
  template: `
    <form class="selector-form" [formGroup]="formGroup" (submit)="select()">
      <mat-form-field class="selector-full-width">
        <input
          type="text"
          placeholder="enter municipality or other administrative boundary name"
          matInput [formControl]="locationInputControl"
          [matAutocomplete]="auto">
        <mat-autocomplete autoActiveFirstOption #auto="matAutocomplete" [displayWith]="displayName">
          <mat-option *ngFor="let option of filteredOptions | async" [value]="option">
            {{option.locationName}} <span class="node-count">({{option.nodeCount}})</span>
          </mat-option>
        </mat-autocomplete>
      </mat-form-field>
      <button mat-stroked-button (submit)="select()">Location overview</button>
    </form>
  `,
  styles: [`
    .selector-form {
      min-width: 250px;
      max-width: 500px;
      width: 100%;
    }

    .selector-full-width {
      width: 100%;
    }

    .node-count {
      padding-left: 20px;
      color: gray;
    }
  `]
})
export class LocationSelectorComponent implements OnInit, OnDestroy {

  @Input() country: Country;
  @Output() selection = new EventEmitter<string>();
  options: List<LocationOption> = List();
  locationInputControl = new FormControl();
  filteredOptions: Observable<LocationOption[]>;
  readonly formGroup: FormGroup;
  private readonly subscriptions = new Subscriptions();

  constructor(private fb: FormBuilder) {
    this.formGroup = this.fb.group({locationInputControl: this.locationInputControl});
  }

  ngOnInit() {
    let countryIndex = 0;
    if (this.country.domain === Countries.nl.domain) {
      countryIndex = 0;
    } else if (this.country.domain === Countries.be.domain) {
      countryIndex = 1;
    } else if (this.country.domain === Countries.de.domain) {
      countryIndex = 2;
    } else if (this.country.domain === Countries.fr.domain) {
      countryIndex = 3;
    } else if (this.country.domain === Countries.at.domain) {
      countryIndex = 4;
    }
    this.options = List(locations[countryIndex].children).flatMap(location => this.toOptions(location));
    this.filteredOptions = this.locationInputControl.valueChanges.pipe(
      startWith(""),
      map(value => typeof value === "string" ? value : value.locationName),
      map(name => name ? this._filter(name) : this.options.toArray())
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  select() {
    this.selection.emit(this.locationInputControl.value.locationName);
  }

  displayName(locationOption?: LocationOption): string | undefined {
    return locationOption ? locationOption.locationName : undefined;
  }

  private _filter(value: string): LocationOption[] {
    const filterValue = value.toLowerCase();
    return this.options.filter(option => option.locationName.toLowerCase().indexOf(filterValue) >= 0).toArray();
  }

  private toOptions(location: LocationNode): List<LocationOption> {
    return List([this.extractOption(location.name)])
      .concat(List(location.children).flatMap(child => this.toOptions(child)))
      .sortBy(locationOption => locationOption.locationName);
  }

  private extractOption(name: string): LocationOption {
    const locationName = name.split("/")[1].split("_")[0];
    return new LocationOption(locationName, 123);
  }

}
