import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { OsmLinkComponent } from './osm-link.component';

@Component({
  selector: 'ui-osm-link-node',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: ` <ui-osm-link kind="node" [elementId]="nodeId().toString()" [title]="title()" /> `,
  imports: [OsmLinkComponent],
})
export class OsmLinkNodeComponent {
  readonly nodeId = input.required<number>();
  readonly title = input('osm');
}
