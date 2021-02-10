import {ChangeDetectionStrategy} from '@angular/core';
import {Input} from '@angular/core';
import {Component} from '@angular/core';
import {Location} from '@api/common/location/location';

@Component({
  selector: 'kpn-node-location',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <p *ngIf="!hasLocation()" i18n="@@node.location.none">None</p>
    <div class="kpn-comma-list">
      <span *ngFor="let name of locationNames()">{{name}}</span>
    </div>
  `
})
export class NodeLocationComponent {

  @Input() location: Location;

  hasLocation() {
    return this.location && this.location.names.length > 0;
  }

  locationNames(): string[] {
    if (this.location) {
      const country = this.location.names[0].toUpperCase();
      const names = [country].concat(this.location.names.slice(1));
      return names.reverse();
    }
    return [];
  }
}
