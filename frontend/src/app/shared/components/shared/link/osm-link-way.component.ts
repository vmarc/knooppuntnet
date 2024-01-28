import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { OsmLinkComponent } from './osm-link.component';

@Component({
  selector: 'kpn-osm-link-way',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: ` <kpn-osm-link kind="way" [elementId]="wayId().toString()" [title]="title()" /> `,
  standalone: true,
  imports: [OsmLinkComponent],
})
export class OsmLinkWayComponent {
  wayId = input.required<number>();
  title = input('osm');
}
