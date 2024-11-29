import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouteNode } from '@api/common/route/route-node';
import { BracketsComponent } from '@app/components/shared/link';
import { LinkNodeComponent } from '@app/components/shared/link';
import { OsmLinkNodeComponent } from '@app/components/shared/link';

@Component({
  selector: 'kpn-route-node',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <p class="kpn-line">
      <img [src]="'/assets/images/' + title()" class="image" title="" alt="" />
      <kpn-link-node [nodeId]="node().nodeId" [nodeName]="node().alternateName" />
      <kpn-brackets>
        <kpn-osm-link-node [nodeId]="node().nodeId" />
      </kpn-brackets>
    </p>
  `,
  imports: [LinkNodeComponent, BracketsComponent, OsmLinkNodeComponent],
})
export class RouteNodeComponent {
  title = input.required<string>();
  node = input.required<RouteNode>();
}
