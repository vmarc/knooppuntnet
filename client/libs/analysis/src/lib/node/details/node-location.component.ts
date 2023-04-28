import { NgIf } from '@angular/common';
import { NgFor } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { NetworkType } from '@api/custom';
import { Util } from '@app/components/shared';
import { I18nService } from '@app/i18n';

@Component({
  selector: 'kpn-node-location',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <p *ngIf="!hasLocation()" i18n="@@node.location.none">None</p>
    <div class="kpn-comma-list">
      <a
        *ngFor="let name of locationNames(); let i = index"
        [routerLink]="locationLink(i)"
        >{{ name }}</a
      >
    </div>
  `,
  standalone: true,
  imports: [NgIf, NgFor, RouterLink],
})
export class NodeLocationComponent {
  @Input() networkType: NetworkType;
  @Input() locations: string[];

  constructor(private i18nService: I18nService) {}

  hasLocation() {
    return this.locations && this.locations.length > 0;
  }

  locationNames(): string[] {
    if (this.locations) {
      const country = this.locations[0].toUpperCase();
      const names = [country].concat(this.locations.slice(1));
      return names.reverse();
    }
    return [];
  }

  locationLink(index: number): string {
    const country = this.locations[0].toLowerCase();
    const countryName = this.i18nService.translation(
      '@@country.' + Util.safeGet(() => country)
    );
    const locationParts = [countryName].concat(
      this.locations.slice(1, this.locations.length - index)
    );
    const location = locationParts.join(':');
    return `/analysis/${this.networkType}/${country}/${location}/nodes`;
  }
}
