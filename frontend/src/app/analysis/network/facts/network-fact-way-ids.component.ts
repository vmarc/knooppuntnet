import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { JosmWayComponent } from '@app/components/shared/link';
import { OsmLinkWayComponent } from '@app/components/shared/link';

@Component({
  selector: 'kpn-network-fact-way-ids',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @for (elementId of elementIds; track elementId) {
      <div>
        <kpn-osm-link-way [wayId]="elementId" [title]="elementId.toString()" />
        <span class="kpn-brackets-link">
          <kpn-josm-way [wayId]="elementId" />
        </span>
      </div>
    }
  `,
  standalone: true,
  imports: [OsmLinkWayComponent, JosmWayComponent],
})
export class NetworkFactWayIdsComponent {
  @Input() elementIds: number[];
}
