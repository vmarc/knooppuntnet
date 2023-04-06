import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';

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
})
export class NetworkFactWayIdsComponent {
  @Input() elementIds: number[];
}
