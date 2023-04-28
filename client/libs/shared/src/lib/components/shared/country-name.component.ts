import { NgIf } from '@angular/common';
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
    <ng-container *ngIf="country">{{ countryName() }}</ng-container>
    <ng-container *ngIf="!country" i18n="@@country.unsupported">
      Unsupported (not Belgium, The Netherlands, Germany, France, Austria or
      Spain)
    </ng-container>
  `,
  standalone: true,
  imports: [NgIf],
})
export class CountryNameComponent {
  @Input() country: Country;

  constructor(private i18nService: I18nService) {}

  countryName(): string {
    return this.i18nService.translation(
      '@@country.' + Util.safeGet(() => this.country)
    );
  }
}
