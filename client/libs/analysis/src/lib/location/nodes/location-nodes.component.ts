import { ChangeDetectionStrategy } from '@angular/core';
import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { LocationNodesPage } from '@api/common/location';
import { LocationNodeTableComponent } from './location-node-table.component';
import { NgIf } from '@angular/common';

@Component({
  selector: 'kpn-location-nodes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div
      *ngIf="page.nodes.length === 0"
      class="kpn-spacer-above"
      i18n="@@location-nodes.no-nodes"
    >
      No nodes
    </div>
    <kpn-location-node-table
      *ngIf="page.nodes.length > 0"
      [timeInfo]="page.timeInfo"
      [nodes]="page.nodes"
      [nodeCount]="page.nodeCount"
    />
  `,
  standalone: true,
  imports: [NgIf, LocationNodeTableComponent],
})
export class LocationNodesComponent {
  @Input() page: LocationNodesPage;
}
