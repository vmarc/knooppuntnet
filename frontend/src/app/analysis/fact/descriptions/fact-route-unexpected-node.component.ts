import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { OsmLinkNodeComponent } from '@app/components/shared/link';
import { FactInfo } from '../fact-info';

@Component({
  selector: 'kpn-fact-route-unexpected-node',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <p class="kpn-sentence">
      <span class="kpn-label" i18n="@@fact.description.route-unexpected-node">
        The route relation contains 1 or more unexpected nodes
      </span>
      <span class="kpn-comma-list">
        @for (nodeId of factInfo().unexpectedNodeIds; track $index) {
          <kpn-osm-link-node [nodeId]="nodeId" [title]="nodeId.toString()" />
        }
      </span>
    </p>
  `,
  standalone: true,
  imports: [OsmLinkNodeComponent],
})
export class FactRouteUnexpectedNodeComponent {
  factInfo = input<FactInfo | undefined>();
}
