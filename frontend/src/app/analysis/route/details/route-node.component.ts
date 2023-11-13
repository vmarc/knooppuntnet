import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { RouteNetworkNodeInfo } from '@api/common/route';
import { BracketsComponent } from '@app/components/shared/link';
import { LinkNodeComponent } from '@app/components/shared/link';
import { OsmLinkNodeComponent } from '@app/components/shared/link';

@Component({
  selector: 'kpn-route-node',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <p class="kpn-line">
      <img [src]="'/assets/images/' + title" class="image" title="" alt="" />
      <kpn-link-node [nodeId]="node.id" [nodeName]="node.alternateName" />
      <kpn-brackets>
        <kpn-osm-link-node [nodeId]="node.id" />
      </kpn-brackets>
    </p>
  `,
  standalone: true,
  imports: [LinkNodeComponent, BracketsComponent, OsmLinkNodeComponent],
})
export class RouteNodeComponent {
  @Input() title: string;
  @Input() node: RouteNetworkNodeInfo;
}
