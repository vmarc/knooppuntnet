import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'kpn-network-fact-relation-ids',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngFor="let elementId of elementIds">
      <kpn-osm-link-relation
        [relationId]="elementId"
        [title]="elementId.toString()"
      />
      <span class="kpn-brackets-link">
        <kpn-josm-relation [relationId]="elementId" />
      </span>
    </div>
  `,
})
export class NetworkFactRelationIdsComponent {
  @Input() elementIds: number[];
}
