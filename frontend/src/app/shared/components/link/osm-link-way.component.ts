import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { OsmLinkComponent } from './osm-link.component';

@Component({
  selector: 'ui-osm-link-way',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: ` <ui-osm-link kind="way" [elementId]="wayId().toString()" [title]="title()" /> `,
  imports: [OsmLinkComponent],
})
export class OsmLinkWayComponent {
  wayId = input.required<number>();
  title = input('osm');
}
