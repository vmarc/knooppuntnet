import { ChangeDetectionStrategy } from '@angular/core';
import { Input } from '@angular/core';
import { Component } from '@angular/core';

@Component({
  selector: 'kpn-node-location',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <p *ngIf="!hasLocation()" i18n="@@node.location.none">None</p>
    <div class="kpn-comma-list">
      <span *ngFor="let name of locationNames()">{{ name }}</span>
    </div>
  `,
})
export class NodeLocationComponent {
  @Input() locations: string[];

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
}
