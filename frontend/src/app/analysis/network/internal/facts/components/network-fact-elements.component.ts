import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { NetworkFact } from '@api/common/network-fact';
import { RouteType } from '@api/common/route-type';
import { NetworkFactNodeIdsComponent } from './network-fact-node-ids.component';
import { NetworkFactNodesComponent } from './network-fact-nodes.component';
import { NetworkFactRelationIdsComponent } from './network-fact-relation-ids.component';
import { NetworkFactRoutesComponent } from './network-fact-routes.component';
import { NetworkFactWayIdsComponent } from './network-fact-way-ids.component';

@Component({
  selector: 'ui-network-fact-elements',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @let fact = networkFact();
    @if (fact.elements) {
      @if (fact.elementType === 'route') {
        <ui-network-fact-routes [routes]="fact.elements" [routeType]="routeType()" />
      } @else if (fact.elementType === 'node') {
        <ui-network-fact-nodes [nodes]="fact.elements" />
      }
    } @else if (fact.elementIds) {
      @switch (fact.elementType) {
        @case ('node') {
          <ui-network-fact-node-ids [nodeIds]="fact.elementIds" />
        }
        @case ('way') {
          <ui-network-fact-way-ids [elementIds]="fact.elementIds" />
        }
        @case ('relation') {
          <ui-network-fact-relation-ids [elementIds]="fact.elementIds" />
        }
      }
    }
  `,
  imports: [
    NetworkFactNodeIdsComponent,
    NetworkFactNodesComponent,
    NetworkFactRelationIdsComponent,
    NetworkFactRoutesComponent,
    NetworkFactWayIdsComponent,
  ],
})
export class NetworkFactElementsComponent {
  readonly routeType = input.required<RouteType>();
  readonly networkFact = input.required<NetworkFact>();
}
