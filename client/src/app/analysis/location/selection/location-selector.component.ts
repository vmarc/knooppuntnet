import { ChangeDetectionStrategy } from '@angular/core';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { LocationNode } from '@api/common/location/location-node';
import { Country } from '@api/custom/country';
import { Observable } from 'rxjs';
import { map, startWith } from 'rxjs/operators';
import { Util } from '../../../components/shared/util';
import { LocationOption } from './location-option';

@Component({
  selector: 'kpn-location-selector',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <form class="selector-form" [formGroup]="formGroup" (submit)="select()">
      <mat-form-field class="selector-full-width">
        <input
          type="text"
          placeholder="enter municipality or other administrative boundary name"
          i18n-placeholder="@@location.selector.input.place-holder"
          matInput
          [formControl]="locationInputControl"
          [matAutocomplete]="auto"
        />
        <mat-autocomplete
          autoActiveFirstOption
          #auto="matAutocomplete"
          [displayWith]="displayName"
          (opened)="resetWarning()"
        >
          <mat-option
            *ngFor="let option of filteredOptions | async"
            [value]="option"
          >
            {{ option.name }}
            <span class="node-count">({{ option.nodeCount }})</span>
          </mat-option>
        </mat-autocomplete>
      </mat-form-field>
      <p
        *ngIf="warningSelectionMandatory"
        class="warning"
        i18n="@@location.selector.warning-selection-mandatory"
      >
        Please make a selection in the field above
      </p>
      <p
        *ngIf="warningSelectionInvalid"
        class="warning"
        i18n="@@location.selector.warning-selection-invalid"
      >
        Please select a value from the list
      </p>
      <button
        mat-stroked-button
        (submit)="select()"
        i18n="@@location.selector.button"
      >
        Location overview
      </button>
    </form>
  `,
  styles: [
    `
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
    `,
  ],
})
export class LocationSelectorComponent implements OnInit {
  @Input() country: Country;
  @Input() locationNode: LocationNode;
  @Output() selection = new EventEmitter<string>();

  warningSelectionMandatory = false;
  warningSelectionInvalid = false;
  options: LocationOption[] = [];
  locationInputControl = new FormControl();
  filteredOptions: Observable<LocationOption[]>;
  readonly formGroup: FormGroup;

  constructor(private fb: FormBuilder) {
    this.formGroup = this.fb.group({
      locationInputControl: this.locationInputControl,
    });
  }

  ngOnInit(): void {
    this.options = this.toOptions('', this.locationNode);
    this.filteredOptions = this.locationInputControl.valueChanges.pipe(
      startWith(''),
      map((value) => (typeof value === 'string' ? value : value.locationName)),
      map((name) => (name ? this._filter(name) : this.options))
    );
  }

  select(): void {
    if (this.locationInputControl.value) {
      const selection = this.locationInputControl.value.locationName;
      const selectedLocationOptions = this.options.filter(
        (locationOption) => locationOption.name === selection
      );
      if (selectedLocationOptions.length > 0) {
        const selectedLocationOption = selectedLocationOptions[0];
        this.selection.emit(selectedLocationOption.name);
        this.warningSelectionMandatory = false;
        this.warningSelectionInvalid = false;
      } else {
        this.warningSelectionMandatory = false;
        this.warningSelectionInvalid = true;
      }
    } else {
      this.warningSelectionMandatory = true;
      this.warningSelectionInvalid = false;
    }
  }

  resetWarning(): void {
    this.warningSelectionMandatory = false;
  }

  displayName(locationOption?: LocationOption): string | undefined {
    return locationOption ? locationOption.name : undefined;
  }

  private _filter(filterValue: string): LocationOption[] {
    const normalizedFilterValue = Util.normalize(filterValue);
    return this.options.filter(
      (option) =>
        option.normalizedLocationName.indexOf(normalizedFilterValue) >= 0
    );
  }

  private toOptions(path: string, location: LocationNode): LocationOption[] {
    const locationOptions: LocationOption[] = [];
    if (location.nodeCount > 0) {
      const normalizedLocationName = Util.normalize(location.name);
      locationOptions.push(
        new LocationOption(
          location.name,
          path,
          normalizedLocationName,
          location.nodeCount
        )
      );
      const childPath = path + ':' + location.name;
      location.children.forEach((child) => {
        const childLocationOptions = this.toOptions(childPath, child);
        childLocationOptions.forEach((loc) => locationOptions.push(loc));
      });
    }
    return locationOptions;
  }
}
