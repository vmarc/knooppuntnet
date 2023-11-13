import { NgFor } from '@angular/common';
import { Input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OsmLinkNodeComponent } from '@app/components/shared/link';
import { FactInfo } from '../fact-info';

@Component({
  selector: 'kpn-fact-route-unexpected-node',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <p class="kpn-sentence">
      <span class="kpn-label" i18n="@@fact.description.route-unexpected-node">
        The route relation contains 1 or more unexpected nodes</span
      >
      <span class="kpn-comma-list">
        <kpn-osm-link-node
          *ngFor="let nodeId of factInfo.unexpectedNodeIds"
          [nodeId]="nodeId"
          [title]="nodeId.toString()"
        />
      </span>
    </p>
  `,
  standalone: true,
  imports: [NgFor, OsmLinkNodeComponent],
})
export class FactRouteUnexpectedNodeComponent {
  @Input() factInfo: FactInfo;
}
