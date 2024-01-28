import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { OsmLinkComponent } from './osm-link.component';

@Component({
  selector: 'kpn-osm-link-change-set',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-osm-link kind="changeset" [elementId]="changeSetId().toString()" title="osm" />
  `,
  standalone: true,
  imports: [OsmLinkComponent],
})
export class OsmLinkChangeSetComponent {
  changeSetId = input.required<number>();
}
