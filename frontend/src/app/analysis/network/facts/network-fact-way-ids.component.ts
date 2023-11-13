import { NgFor } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { JosmWayComponent } from '@app/components/shared/link';
import { OsmLinkWayComponent } from '@app/components/shared/link';

@Component({
  selector: 'kpn-network-fact-way-ids',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngFor="let elementId of elementIds">
      <kpn-osm-link-way [wayId]="elementId" [title]="elementId.toString()" />
      <span class="kpn-brackets-link">
        <kpn-josm-way [wayId]="elementId" />
      </span>
    </div>
  `,
  standalone: true,
  imports: [NgFor, OsmLinkWayComponent, JosmWayComponent],
})
export class NetworkFactWayIdsComponent {
  @Input() elementIds: number[];
}
