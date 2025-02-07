import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { Country } from '@api/common';
import { Translations } from '@app/i18n';
import { Countries } from '@app/kpn/common';
import { NzSelectComponent } from 'ng-zorro-antd/select';
import { NzOptionComponent } from 'ng-zorro-antd/select';
import { PoiLocationPoisPageService } from '../poi-location-pois-page.service';
import { CountryName } from './country-name';

@Component({
  selector: 'kpn-country-select',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <nz-select
      [ngModel]="undefined"
      (ngModelChange)="countrySelectionChanged($event)"
      i18n-nzPlaceHolder="@@country.selector.label"
      nzPlaceHolder="Select country"
    >
      @for (countryName of countryNames; track countryName) {
        <nz-option [nzValue]="countryName.country" [nzLabel]="countryName.name" />
      }
    </nz-select>
  `,
  imports: [FormsModule, NzOptionComponent, NzSelectComponent, ReactiveFormsModule],
})
export class CountrySelectComponent {
  readonly service = inject(PoiLocationPoisPageService);
  readonly countryNames = Countries.all.map((country) => {
    const name = Translations.get(`country.${country}`);
    return new CountryName(country, name);
  });

  countrySelectionChanged(value: Country) {
    this.service.updateCountry(value);
  }
}
