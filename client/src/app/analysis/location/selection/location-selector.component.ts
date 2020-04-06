import {Component, EventEmitter, Input, OnInit, Output} from "@angular/core";
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";
import {List} from "immutable";
import {Observable} from "rxjs";
import {map, startWith} from "rxjs/operators";
import {LocationNode} from "../../../kpn/api/common/location/location-node";
import {Country} from "../../../kpn/api/custom/country";
import {LocationOption} from "./location-option";

@Component({
  selector: "kpn-location-selector",
  template: `
    <form class="selector-form" [formGroup]="formGroup" (submit)="select()">
      <mat-form-field class="selector-full-width">
        <input
          type="text"
          placeholder="enter municipality or other administrative boundary name"
          i18n-placeholder="@@location.selector.input.place-holder"
          matInput [formControl]="locationInputControl"
          [matAutocomplete]="auto">
        <mat-autocomplete autoActiveFirstOption #auto="matAutocomplete" [displayWith]="displayName">
          <mat-option *ngFor="let option of filteredOptions | async" [value]="option">
            {{option.locationName}} <span class="node-count">({{option.nodeCount}})</span>
          </mat-option>
        </mat-autocomplete>
      </mat-form-field>
      <button mat-stroked-button (submit)="select()" i18n="@@location.selector.button">Location overview</button>
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
export class LocationSelectorComponent implements OnInit {

  @Input() country: Country;
  @Input() locationNode: LocationNode;
  @Output() selection = new EventEmitter<string>();

  options: List<LocationOption> = List();
  locationInputControl = new FormControl();
  filteredOptions: Observable<LocationOption[]>;
  readonly formGroup: FormGroup;

  constructor(private fb: FormBuilder) {
    this.formGroup = this.fb.group({locationInputControl: this.locationInputControl});
  }

  ngOnInit() {
    this.options = this.toOptions(this.locationNode);
    this.filteredOptions = this.locationInputControl.valueChanges.pipe(
      startWith(""),
      map(value => typeof value === "string" ? value : value.locationName),
      map(name => name ? this._filter(name) : this.options.toArray())
    );
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
    return List([new LocationOption(location.name, location.nodeCount)])
      .concat(location.children.flatMap(child => this.toOptions(child)))
      .sortBy(locationOption => locationOption.locationName);
  }
}
