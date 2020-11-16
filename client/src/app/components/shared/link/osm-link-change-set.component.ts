import {ChangeDetectionStrategy} from '@angular/core';
import {Component, Input} from '@angular/core';

@Component({
  selector: 'kpn-osm-link-change-set',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-osm-link kind="changeset" [elementId]="changeSetId.toString()" title="osm"></kpn-osm-link>
  `
})
export class OsmLinkChangeSetComponent {

  @Input() changeSetId: number;

}
