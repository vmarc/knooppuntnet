import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { OsmLinkComponent } from './osm-link.component';

@Component({
  selector: 'kpn-osm-link-node',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-osm-link kind="node" [elementId]="nodeId.toString()" [title]="title" />
  `,
  standalone: true,
  imports: [OsmLinkComponent],
})
export class OsmLinkNodeComponent {
  @Input() nodeId: number;
  @Input() title = 'osm';
}
