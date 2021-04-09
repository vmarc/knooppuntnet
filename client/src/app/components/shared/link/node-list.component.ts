import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'kpn-node-list',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="kpn-comma-list">
      <span *ngFor="let nodeId of nodeIds">
        <kpn-osm-link-node
          [nodeId]="nodeId"
          [title]="nodeId.toString()"
        ></kpn-osm-link-node>
      </span>
    </div>
  `,
})
export class NodeListComponent {
  @Input() nodeIds: number[];
}
