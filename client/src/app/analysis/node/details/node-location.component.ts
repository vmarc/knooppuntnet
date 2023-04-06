import { ChangeDetectionStrategy } from '@angular/core';
import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { NetworkType } from '@api/custom/network-type';
import { Util } from '@app/components/shared/util';
import { I18nService } from '@app/i18n/i18n.service';

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
