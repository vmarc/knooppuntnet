import {ChangeDetectionStrategy} from '@angular/core';
import {Component, Input} from '@angular/core';
import {NetworkFact} from '@api/common/network-fact';

@Component({
  selector: 'kpn-network-fact',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-network-fact-header [fact]="fact"></kpn-network-fact-header>

    <div *ngIf="fact.elementIds && fact.elementType === 'node'">
      <kpn-network-fact-node-ids [elementIds]="fact.elementIds"></kpn-network-fact-node-ids>
    </div>

    <div *ngIf="fact.elementIds && fact.elementType === 'way'">
      <kpn-network-fact-way-ids [elementIds]="fact.elementIds"></kpn-network-fact-way-ids>
    </div>

    <div *ngIf="fact.elementIds && fact.elementType === 'relation'">
      <kpn-network-fact-relation-ids [elementIds]="fact.elementIds"></kpn-network-fact-relation-ids>
    </div>

    <div *ngIf="fact.elements && fact.elementType === 'node'">
      <kpn-network-fact-nodes [nodes]="fact.elements"></kpn-network-fact-nodes>
    </div>

    <div *ngIf="fact.elements && fact.elementType === 'route'">
      <kpn-network-fact-routes [routes]="fact.elements"></kpn-network-fact-routes>
    </div>

    <div *ngIf="!fact.checks.isEmpty()">
      <kpn-network-fact-checks [checks]="fact.checks"></kpn-network-fact-checks>
    </div>
  `
})
export class NetworkFactComponent {
  @Input() fact: NetworkFact;
}
