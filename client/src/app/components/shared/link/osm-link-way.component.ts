import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'kpn-osm-link-way',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-osm-link
      kind="way"
      [elementId]="wayId.toString()"
      [title]="title"
    />
  `,
})
export class OsmLinkWayComponent {
  @Input() wayId: number;
  @Input() title = 'osm';
}
