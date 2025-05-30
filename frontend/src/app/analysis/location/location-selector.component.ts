import { output } from '@angular/core';
import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { FormsModule } from '@angular/forms';
import { UntypedFormBuilder } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { UntypedFormControl } from '@angular/forms';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatButtonModule } from '@angular/material/button';
import { MatOptionModule } from '@angular/material/core';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { Country } from '@api/common/country';
import { LocationNode } from '@api/common/location/location-node';
import { Util } from '@app/shared/components/util';
import { NzFormItemComponent } from 'ng-zorro-antd/form';
import { NzFormControlComponent } from 'ng-zorro-antd/form';
import { NzOptionComponent } from 'ng-zorro-antd/select';
import { NzSelectComponent } from 'ng-zorro-antd/select';
import { LocationOption } from './internal/selection/components/location-option';

@Component({
  selector: 'ui-location-selector',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (options()) {
      <nz-form-control style="margin-bottom: 24px">
        <nz-form-item>
          <nz-select
            nzShowSearch
            nzAllowClear
            [ngModel]="undefined"
            (ngModelChange)="selectionChanged($event)"
            (nzOnSearch)="inputChanged($event)"
            i18n-nzPlaceHolder="@@location.selector.input.label"
            nzPlaceHolder="municipality or other administrative boundary name"
          >
            @for (option of filteredOptions(); track option) {
              <nz-option [nzValue]="option" [nzLabel]="option.name" />
            }
          </nz-select>
        </nz-form-item>
      </nz-form-control>

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
      <!--      <nz-form-control style="background-color: yellow;">-->
      <!--        <nz-form-item>-->
      <!--          <button nz-button (submit)="select()" i18n="@@location.selector.button">-->
      <!--            Location overview-->
      <!--          </button>-->
      <!--        </nz-form-item>-->
      <!--      </nz-form-control>-->
    }
  `,
  imports: [
    FormsModule,
    MatAutocompleteModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatOptionModule,
    NzFormControlComponent,
    NzFormItemComponent,
    NzOptionComponent,
    NzSelectComponent,
    ReactiveFormsModule,
  ],
})
export class LocationSelectorComponent /* implements OnInit*/ {
  country = input.required<Country>();

  locationNode = input.required<LocationNode>();
  all = input(false);
  selection = output<string>();
  private readonly maxOptions = 2000;
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

  selectionChanged(value) {
    console.log('selection changed', value);
    this.selection.emit(value.path + ':' + value.name);
  }

  inputChanged(value) {
    console.log('input changed', value);
  }

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
