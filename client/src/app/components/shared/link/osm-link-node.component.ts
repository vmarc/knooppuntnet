import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'kpn-osm-link-node',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-osm-link
      kind="node"
      [elementId]="nodeId.toString()"
      [title]="title"
    ></kpn-osm-link>
  `,
})
export class OsmLinkNodeComponent {
  @Input() nodeId: number;
  @Input() title = 'osm';
}
