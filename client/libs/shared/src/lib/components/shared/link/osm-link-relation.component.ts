import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { OsmLinkComponent } from './osm-link.component';

@Component({
  selector: 'kpn-osm-link-relation',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-osm-link
      kind="relation"
      [elementId]="relationId.toString()"
      [title]="title"
    />
  `,
  standalone: true,
  imports: [OsmLinkComponent],
})
export class OsmLinkRelationComponent {
  @Input() relationId: number;
  @Input() title = 'osm';
}
