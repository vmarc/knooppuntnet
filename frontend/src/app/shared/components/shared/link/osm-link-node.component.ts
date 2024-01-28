import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { OsmLinkComponent } from './osm-link.component';

@Component({
  selector: 'kpn-osm-link-node',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: ` <kpn-osm-link kind="node" [elementId]="nodeId().toString()" [title]="title()" /> `,
  standalone: true,
  imports: [OsmLinkComponent],
})
export class OsmLinkNodeComponent {
  nodeId = input.required<number>();
  title = input('osm');
}
