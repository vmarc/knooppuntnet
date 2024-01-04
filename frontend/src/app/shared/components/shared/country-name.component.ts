import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { Country } from '@api/custom';
import { I18nService } from '@app/i18n';
import { Util } from './util';

@Component({
  selector: 'kpn-country-name',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (country) {
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
  @Input({ required: true }) country: Country;

  constructor(private i18nService: I18nService) {}

  countryName(): string {
    return this.i18nService.translation('@@country.' + Util.safeGet(() => this.country));
  }
}
