import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { JosmNodeComponent } from '@app/components/shared/link';
import { OsmLinkNodeComponent } from '@app/components/shared/link';

@Component({
  selector: 'kpn-network-fact-node-ids',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @for (elementId of elementIds(); track elementId) {
      <div>
        <kpn-osm-link-node [nodeId]="elementId" [title]="elementId.toString()" />
        <span class="kpn-brackets-link">
          <kpn-josm-node [nodeId]="elementId" />
        </span>
      </div>
    }
  `,
  standalone: true,
  imports: [OsmLinkNodeComponent, JosmNodeComponent],
})
export class NetworkFactNodeIdsComponent {
  elementIds = input.required<number[]>();
}
