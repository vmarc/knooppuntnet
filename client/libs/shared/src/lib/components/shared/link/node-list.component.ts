import { NgFor } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { OsmLinkNodeComponent } from './osm-link-node.component';

@Component({
  selector: 'kpn-node-list',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="kpn-comma-list">
      <span *ngFor="let nodeId of nodeIds">
        <kpn-osm-link-node [nodeId]="nodeId" [title]="nodeId.toString()" />
      </span>
    </div>
  `,
  standalone: true,
  imports: [NgFor, OsmLinkNodeComponent],
})
export class NodeListComponent {
  @Input({ required: true }) nodeIds: number[];
}
