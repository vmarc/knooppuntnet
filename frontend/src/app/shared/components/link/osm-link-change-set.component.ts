import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { OsmLinkComponent } from './osm-link.component';

@Component({
  selector: 'ui-osm-link-change-set',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: ` <ui-osm-link kind="changeset" [elementId]="changeSetId().toString()" title="osm" /> `,
  imports: [OsmLinkComponent],
})
export class OsmLinkChangeSetComponent {
  changeSetId = input.required<number>();
}
