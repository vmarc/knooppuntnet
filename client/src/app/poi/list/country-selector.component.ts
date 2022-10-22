import { EventEmitter } from '@angular/core';
import { Output } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FormControl } from '@angular/forms';
import { Country } from '@api/custom/country';
import { I18nService } from '../../i18n/i18n.service';
import { Countries } from '../../kpn/common/countries';

@Component({
  selector: 'kpn-country-selector',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-form-field class="group">
      <mat-label i18n="@@country.selector.label">Country</mat-label>
      <mat-select [formControl]="selectedCountry">
        <mat-option *ngFor="let country of countries" [value]="country">
          {{ country }}
        </mat-option>
      </mat-select>
    </mat-form-field>
  `,
})
export class CountrySelectorComponent {
  @Output() country = new EventEmitter<Country | null>();

  countries: string[];

  readonly selectedCountry = new FormControl<Country | null>(null);

  constructor(i18nService: I18nService) {
    this.countries = Countries.all.map((country) =>
      i18nService.translation(`@@country.${country}`)
    );
    this.selectedCountry.valueChanges.subscribe((c) => this.country.emit(c));
  }
}
