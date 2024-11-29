import { output } from '@angular/core';
import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { UntypedFormBuilder } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { UntypedFormControl } from '@angular/forms';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatButtonModule } from '@angular/material/button';
import { MatOptionModule } from '@angular/material/core';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { LocationNode } from '@api/common/location';
import { Country } from '@api/custom';
import { Util } from '@app/components/shared';
import { LocationOption } from './location-option';

@Component({
  selector: 'kpn-location-selector',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (options()) {
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
            @for (option of filteredOptions(); track option) {
              <mat-option [value]="option">
                {{ option.name }}
                @if (nodeCount(option) > 0) {
                  <span class="node-count">({{ nodeCount(option) }})</span>
                }
              </mat-option>
            }
          </mat-autocomplete>
        </mat-form-field>
        @if (warningSelectionMandatory) {
          <p class="kpn-warning" i18n="@@location.selector.warning-selection-mandatory">
            Please make a selection in the field above
          </p>
        }
        @if (warningSelectionInvalid) {
          <p class="kpn-warning" i18n="@@location.selector.warning-selection-invalid">
            Please select a value from the list
          </p>
        }
        <button mat-stroked-button (submit)="select()" i18n="@@location.selector.button">
          Location overview
        </button>
      </form>
    }
  `,
  styles: `
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
      color: grey;
    }
  `,
  imports: [
    MatAutocompleteModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatOptionModule,
    ReactiveFormsModule,
  ],
})
export class LocationSelectorComponent /* implements OnInit*/ {
  country = input.required<Country>();
  locationNode = input.required<LocationNode>();
  all = input(false);
  selection = output<string>();
  private readonly maxOptions = 20;
  private readonly fb = inject(UntypedFormBuilder);

  protected warningSelectionMandatory = false;
  protected warningSelectionInvalid = false;
  protected readonly options = computed(() => this.initOptions(this.locationNode()));
  protected locationInputControl = new UntypedFormControl();
  protected readonly formGroup = this.fb.group({
    locationInputControl: this.locationInputControl,
  });

  private readonly inputControlValue = toSignal(this.locationInputControl.valueChanges);
  protected readonly filteredOptions = computed(() => {
    const options = this.options();
    const value = this.inputControlValue();
    if (typeof value === 'string') {
      return this._filter(value);
    }
    if (value instanceof LocationOption) {
      return this._filter(value.name);
    }
    return options.slice(0, this.maxOptions);
  });

  select(): void {
    if (this.locationInputControl.value) {
      let selection = this.locationInputControl.value;
      if (!(selection instanceof LocationOption)) {
        const normalized = Util.normalize(selection);
        const selectedLocationOptions = this.options().filter(
          (locationOption) => locationOption.normalizedLocationName === normalized
        );
        if (selectedLocationOptions.length > 0) {
          selection = selectedLocationOptions[0];
        }
      }
      if (selection instanceof LocationOption) {
        const selectedLocationName =
          selection.path.length > 0 ? selection.path + ':' + selection.name : selection.name;
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
    return this.options()
      .filter((option) => option.normalizedLocationName.startsWith(normalizedFilterValue))
      .slice(0, this.maxOptions);
  }

  private initOptions(location: LocationNode): LocationOption[] {
    const result = this.toOptions('', location);
    result.sort((a, b) =>
      a.normalizedLocationName > b.normalizedLocationName
        ? 1
        : a.normalizedLocationName < b.normalizedLocationName
          ? -1
          : 0
    );
    return result;
  }

  private toOptions(path: string, location: LocationNode): LocationOption[] {
    const locationOptions: LocationOption[] = [];
    if (this.all() || (location && location.nodeCount && location.nodeCount > 0)) {
      const normalizedLocationName = Util.normalize(location.name);
      locationOptions.push(
        new LocationOption(location.name, path, normalizedLocationName, location.nodeCount)
      );
      const childPath = path.length > 0 ? path + ':' + location.name : location.name;
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
