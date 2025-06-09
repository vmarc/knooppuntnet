import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { OsmLinkComponent } from './osm-link.component';

@Component({
  selector: 'ui-osm-link-relation',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-osm-link kind="relation" [elementId]="relationId().toString()" [title]="title()" />
  `,
  imports: [OsmLinkComponent],
})
export class OsmLinkRelationComponent {
  readonly relationId = input.required<number>();
  readonly title = input('osm');
}
