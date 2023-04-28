import { AsyncPipe, NgFor, NgIf } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { EventEmitter } from '@angular/core';
import { Input } from '@angular/core';
import { OnInit } from '@angular/core';
import { Output } from '@angular/core';
import { UntypedFormBuilder } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { UntypedFormControl } from '@angular/forms';
import { UntypedFormGroup } from '@angular/forms';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatButtonModule } from '@angular/material/button';
import { MatOptionModule } from '@angular/material/core';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { LocationNode } from '@api/common/location';
import { Country } from '@api/custom';
import { Util } from '@app/components/shared';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { startWith } from 'rxjs/operators';
import { LocationOption } from './location-option';

@Component({
  selector: 'kpn-location-selector',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <form class="selector-form" [formGroup]="formGroup" (submit)="select()">
      <mat-form-field class="selector-full-width">
        <mat-label i18n="@@location.selector.input.label"
          >municipality or other administrative boundary name
        </mat-label>
        <input
          type="text"
          placeholder=""
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
            <span *ngIf="nodeCount(option) > 0" class="node-count"
              >({{ nodeCount(option) }})</span
            >
          </mat-option>
        </mat-autocomplete>
      </mat-form-field>
      <p
        *ngIf="warningSelectionMandatory"
        class="kpn-warning"
        i18n="@@location.selector.warning-selection-mandatory"
      >
        Please make a selection in the field above
      </p>
      <p
        *ngIf="warningSelectionInvalid"
        class="kpn-warning"
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
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatAutocompleteModule,
    NgFor,
    MatOptionModule,
    NgIf,
    MatButtonModule,
    AsyncPipe,
  ],
})
export class LocationSelectorComponent implements OnInit {
  @Input() country: Country;
  @Input() locationNode: LocationNode;
  @Input() all = false;
  @Output() selection = new EventEmitter<string>();

  warningSelectionMandatory = false;
  warningSelectionInvalid = false;
  options: LocationOption[] = [];
  locationInputControl = new UntypedFormControl();
  filteredOptions: Observable<LocationOption[]>;
  readonly formGroup: UntypedFormGroup;

  constructor(private fb: UntypedFormBuilder) {
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
      let selection = this.locationInputControl.value;
      if (!(selection instanceof LocationOption)) {
        const normalized = Util.normalize(selection);
        const selectedLocationOptions = this.options.filter(
          (locationOption) =>
            locationOption.normalizedLocationName === normalized
        );
        if (selectedLocationOptions.length > 0) {
          selection = selectedLocationOptions[0];
        }
      }
      if (selection instanceof LocationOption) {
        const selectedLocationName =
          selection.path.length > 0
            ? selection.path + ':' + selection.name
            : selection.name;
        this.selection.emit(selectedLocationName);
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
    if (
      this.all ||
      (location && location.nodeCount && location.nodeCount > 0)
    ) {
      const normalizedLocationName = Util.normalize(location.name);
      locationOptions.push(
        new LocationOption(
          location.name,
          path,
          normalizedLocationName,
          location.nodeCount
        )
      );
      const childPath =
        path.length > 0 ? path + ':' + location.name : location.name;
      if (location.children) {
        location.children.forEach((child) => {
          const childLocationOptions = this.toOptions(childPath, child);
          childLocationOptions.forEach((loc) => locationOptions.push(loc));
        });
      }
    }
    return locationOptions;
  }

  nodeCount(option: LocationOption): number {
    if (option.nodeCount) {
      return option.nodeCount;
    }
    return 0;
  }
}
