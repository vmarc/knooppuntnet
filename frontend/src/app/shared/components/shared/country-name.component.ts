import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { Country } from '@api/custom';
import { Translations } from '@app/i18n';
import { Util } from './util';

@Component({
  selector: 'kpn-country-name',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (country()) {
      {{ countryName() }}
    } @else {
      <ng-container i18n="@@country.unsupported">
        Unsupported (not Belgium, The Netherlands, Germany, France, Austria, Spain or Denmark)
      </ng-container>
    }
  `,
  standalone: true,
  imports: [],
})
export class CountryNameComponent {
  country = input.required<Country>();

  protected countryName = computed(() =>
    Translations.get('country.' + Util.safeGet(() => this.country()))
  );
}
