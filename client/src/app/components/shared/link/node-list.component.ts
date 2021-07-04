import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { List } from 'immutable';

@Component({
  selector: 'kpn-node-list',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngFor="let nodeId of nodeIds" class="kpn-comma-list">
      <kpn-osm-link-node
        [nodeId]="nodeId"
        [title]="nodeId.toString()"
      ></kpn-osm-link-node>
    </div>
  `,
})
export class NodeListComponent {
  @Input() nodeIds: List<number>;
}
