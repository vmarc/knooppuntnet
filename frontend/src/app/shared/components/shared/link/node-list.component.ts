import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { OsmLinkNodeComponent } from './osm-link-node.component';

@Component({
  selector: 'kpn-node-list',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="kpn-comma-list">
      @for (nodeId of nodeIds(); track nodeId) {
        <span>
          <kpn-osm-link-node [nodeId]="nodeId" [title]="nodeId.toString()" />
        </span>
      }
    </div>
  `,
  standalone: true,
  imports: [OsmLinkNodeComponent],
})
export class NodeListComponent {
  nodeIds = input.required<number[]>();
}
