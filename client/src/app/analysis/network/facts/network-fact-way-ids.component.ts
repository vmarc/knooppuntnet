import {ChangeDetectionStrategy} from '@angular/core';
import {Component, Input} from '@angular/core';

@Component({
  selector: 'kpn-network-fact-way-ids',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngFor="let elementId of elementIds">
      <kpn-osm-link-way [wayId]="elementId" [title]="elementId.toString()"></kpn-osm-link-way>
      <span class="kpn-brackets-link">
        <kpn-josm-way [wayId]="elementId"></kpn-josm-way>
      </span>
    </div>
  `
})
export class NetworkFactWayIdsComponent {
  @Input() elementIds: number[];
}
