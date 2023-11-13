import { NgIf } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { NetworkFact } from '@api/common';
import { NetworkType } from '@api/custom';
import { NetworkFactChecksComponent } from './network-fact-checks.component';
import { NetworkFactHeaderComponent } from './network-fact-header.component';
import { NetworkFactNodeIdsComponent } from './network-fact-node-ids.component';
import { NetworkFactNodesComponent } from './network-fact-nodes.component';
import { NetworkFactRelationIdsComponent } from './network-fact-relation-ids.component';
import { NetworkFactRoutesComponent } from './network-fact-routes.component';
import { NetworkFactWayIdsComponent } from './network-fact-way-ids.component';

@Component({
  selector: 'kpn-network-fact',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-network-fact-header [fact]="fact" />

    <div *ngIf="fact.elementIds && fact.elementType === 'node'">
      <kpn-network-fact-node-ids [elementIds]="fact.elementIds" />
    </div>

    <div *ngIf="fact.elementIds && fact.elementType === 'way'">
      <kpn-network-fact-way-ids [elementIds]="fact.elementIds" />
    </div>

    <div *ngIf="fact.elementIds && fact.elementType === 'relation'">
      <kpn-network-fact-relation-ids [elementIds]="fact.elementIds" />
    </div>

    <div *ngIf="fact.elements && fact.elementType === 'node'">
      <kpn-network-fact-nodes [nodes]="fact.elements" />
    </div>

    <div *ngIf="fact.elements && fact.elementType === 'route'">
      <kpn-network-fact-routes
        [networkType]="networkType"
        [routes]="fact.elements"
      />
    </div>

    <div *ngIf="fact.checks && fact.checks.length > 0">
      <kpn-network-fact-checks [checks]="fact.checks" />
    </div>
  `,
  standalone: true,
  imports: [
    NetworkFactChecksComponent,
    NetworkFactHeaderComponent,
    NetworkFactNodeIdsComponent,
    NetworkFactNodesComponent,
    NetworkFactRelationIdsComponent,
    NetworkFactRoutesComponent,
    NetworkFactWayIdsComponent,
    NgIf,
  ],
})
export class NetworkFactComponent {
  @Input() networkType: NetworkType;
  @Input() fact: NetworkFact;
}
