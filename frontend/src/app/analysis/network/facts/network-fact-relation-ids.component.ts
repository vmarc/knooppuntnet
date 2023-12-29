import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { JosmRelationComponent } from '@app/components/shared/link';
import { OsmLinkRelationComponent } from '@app/components/shared/link';

@Component({
  selector: 'kpn-network-fact-relation-ids',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @for (elementId of elementIds; track elementId) {
      <div>
        <kpn-osm-link-relation [relationId]="elementId" [title]="elementId.toString()" />
        <span class="kpn-brackets-link">
          <kpn-josm-relation [relationId]="elementId" />
        </span>
      </div>
    }
  `,
  standalone: true,
  imports: [OsmLinkRelationComponent, JosmRelationComponent],
})
export class NetworkFactRelationIdsComponent {
  @Input() elementIds: number[];
}
