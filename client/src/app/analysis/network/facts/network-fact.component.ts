import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { NetworkFact } from '@api/common/network-fact';
import { NetworkType } from '@api/custom/network-type';

@Component({
  selector: 'kpn-network-fact',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-network-fact-header [fact]="fact"></kpn-network-fact-header>

    <div *ngIf="fact.elementIds && fact.elementType === 'node'">
      <kpn-network-fact-node-ids
        [elementIds]="fact.elementIds"
      ></kpn-network-fact-node-ids>
    </div>

    <div *ngIf="fact.elementIds && fact.elementType === 'way'">
      <kpn-network-fact-way-ids
        [elementIds]="fact.elementIds"
      ></kpn-network-fact-way-ids>
    </div>

    <div *ngIf="fact.elementIds && fact.elementType === 'relation'">
      <kpn-network-fact-relation-ids
        [elementIds]="fact.elementIds"
      ></kpn-network-fact-relation-ids>
    </div>

    <div *ngIf="fact.elements && fact.elementType === 'node'">
      <kpn-network-fact-nodes [nodes]="fact.elements"></kpn-network-fact-nodes>
    </div>

    <div *ngIf="fact.elements && fact.elementType === 'route'">
      <kpn-network-fact-routes
        [networkType]="networkType"
        [routes]="fact.elements"
      ></kpn-network-fact-routes>
    </div>

    <div *ngIf="fact.checks && fact.checks.length > 0">
      <kpn-network-fact-checks [checks]="fact.checks"></kpn-network-fact-checks>
    </div>
  `,
})
export class NetworkFactComponent {
  @Input() networkType: NetworkType;
  @Input() fact: NetworkFact;
}
