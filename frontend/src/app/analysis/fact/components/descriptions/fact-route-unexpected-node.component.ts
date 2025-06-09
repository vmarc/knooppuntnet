import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { OsmLinkNodeComponent } from '@app/shared/components/link/osm-link-node.component';
import { FactInfo } from '../fact-info';

@Component({
  selector: 'ui-fact-route-unexpected-node',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <p class="kpn-sentence">
      <span class="kpn-label" i18n="@@fact.description.route-unexpected-node">
        The route relation contains 1 or more unexpected nodes
      </span>
      <span class="kpn-comma-list">
        @for (nodeId of factInfo().unexpectedNodeIds; track $index) {
          <ui-osm-link-node [nodeId]="nodeId" [title]="nodeId.toString()" />
        }
      </span>
    </p>
  `,
  imports: [OsmLinkNodeComponent],
})
export class FactRouteUnexpectedNodeComponent {
  readonly factInfo = input.required<FactInfo>();
}
