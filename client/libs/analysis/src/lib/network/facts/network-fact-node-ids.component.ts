import { NgFor } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { JosmNodeComponent } from '@app/components/shared/link';
import { OsmLinkNodeComponent } from '@app/components/shared/link';

@Component({
  selector: 'kpn-network-fact-node-ids',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngFor="let elementId of elementIds">
      <kpn-osm-link-node [nodeId]="elementId" [title]="elementId.toString()" />
      <span class="kpn-brackets-link">
        <kpn-josm-node [nodeId]="elementId" />
      </span>
    </div>
  `,
  standalone: true,
  imports: [NgFor, OsmLinkNodeComponent, JosmNodeComponent],
})
export class NetworkFactNodeIdsComponent {
  @Input() elementIds: number[];
}
