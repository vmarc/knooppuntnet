import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouteNode } from '@api/common/route/route-node';
import { BracketsComponent } from '@app/shared/components/link/brackets.component';
import { LinkNodeComponent } from '@app/shared/components/link/link-node.component';
import { OsmLinkNodeComponent } from '@app/shared/components/link/osm-link-node.component';

@Component({
  selector: 'ui-route-node',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <p class="kpn-line">
      <img [src]="imageSrc()" class="image" title="" alt="" />
      <ui-link-node [nodeId]="node().nodeId" [nodeName]="node().alternateName" />
      <ui-brackets>
        <ui-osm-link-node [nodeId]="node().nodeId" />
      </ui-brackets>
    </p>
  `,
  imports: [LinkNodeComponent, BracketsComponent, OsmLinkNodeComponent],
})
export class RouteNodeComponent {
  readonly title = input.required<string>();
  readonly node = input.required<RouteNode>();
  readonly imageSrc = computed(() => `/assets/images/${this.title()}`);
}
