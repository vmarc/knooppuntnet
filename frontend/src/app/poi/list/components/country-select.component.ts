import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { MatOptionModule } from '@angular/material/core';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectChange } from '@angular/material/select';
import { MatSelectModule } from '@angular/material/select';
import { Country } from '@api/custom';
import { Translations } from '@app/i18n';
import { Countries } from '@app/kpn/common';
import { PoiLocationPoisPageService } from '../poi-location-pois-page.service';
import { CountryName } from './country-name';

@Component({
  selector: 'kpn-country-select',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-form-field class="group">
      <mat-label i18n="@@country.selector.label">Country</mat-label>
      <mat-select
        [formControl]="countryControl"
        (selectionChange)="countrySelectionChanged($event)"
      >
        @for (countryName of countryNames; track countryName) {
          <mat-option [value]="countryName.country">
            {{ countryName.name }}
          </mat-option>
        }
      </mat-select>
    </mat-form-field>
  `,
  standalone: true,
  imports: [MatFormFieldModule, MatOptionModule, MatSelectModule, ReactiveFormsModule],
})
export class CountrySelectComponent {
  protected readonly service = inject(PoiLocationPoisPageService);
  protected readonly countryControl = new FormControl<Country>(null);
  protected readonly countryNames = Countries.all.map((country) => {
    const name = Translations.get(`country.${country}`);
    return new CountryName(country, name);
  });

  countrySelectionChanged(event: MatSelectChange) {
    this.service.setCountry(event.value);
  }
}
