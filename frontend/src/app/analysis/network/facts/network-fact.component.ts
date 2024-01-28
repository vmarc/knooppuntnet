import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
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
    <kpn-network-fact-header [fact]="fact()" />

    @if (fact().elementIds) {
      @switch (fact().elementType) {
        @case ('node') {
          <kpn-network-fact-node-ids [elementIds]="fact().elementIds" />
        }
        @case ('way') {
          <kpn-network-fact-way-ids [elementIds]="fact().elementIds" />
        }
        @case ('relation') {
          <kpn-network-fact-relation-ids [elementIds]="fact().elementIds" />
        }
        @case ('route') {
          <kpn-network-fact-routes [networkType]="networkType()" [routes]="fact().elements" />
        }
      }
    }

    @if (fact().elements && fact().elementType === 'node') {
      <kpn-network-fact-nodes [nodes]="fact().elements" />
    }

    @if (fact().checks && fact().checks.length > 0) {
      <kpn-network-fact-checks [checks]="fact().checks" />
    }
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
  ],
})
export class NetworkFactComponent {
  networkType = input<NetworkType | undefined>();
  fact = input<NetworkFact | undefined>();
}
