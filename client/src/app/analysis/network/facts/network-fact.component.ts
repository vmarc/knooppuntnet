import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { NetworkFact } from '@api/common';
import { NetworkType } from '@api/custom';

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
})
export class NetworkFactComponent {
  @Input() networkType: NetworkType;
  @Input() fact: NetworkFact;
}
