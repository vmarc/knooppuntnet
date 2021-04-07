import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'kpn-network-fact-node-ids',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngFor="let elementId of elementIds">
      <kpn-osm-link-node
        [nodeId]="elementId"
        [title]="elementId.toString()"
      ></kpn-osm-link-node>
      <span class="kpn-brackets-link">
        <kpn-josm-node [nodeId]="elementId"></kpn-josm-node>
      </span>
    </div>
  `,
})
export class NetworkFactNodeIdsComponent {
  @Input() elementIds: number[];
}
