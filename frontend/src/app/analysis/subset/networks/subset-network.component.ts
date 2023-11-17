import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { NetworkAttributes } from '@api/common/network';
import { IntegerFormatPipe } from '@app/components/shared/format';
import { JosmRelationComponent } from '@app/components/shared/link';
import { LinkNetworkDetailsComponent } from '@app/components/shared/link';
import { OsmLinkRelationComponent } from '@app/components/shared/link';
import { InterpretedNetworkAttributes } from './interpreted-network-attributes';
import { SubsetNetworkHappyComponent } from './subset-network-happy.component';

@Component({
  selector: 'kpn-subset-network',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="title">
      <kpn-link-network-details
        [networkId]="network.id"
        [networkType]="network.networkType"
        [networkName]="network.name"
      />
      <span class="percentage">{{ interpretedNetwork.percentageOk() }}</span>
      <kpn-subset-network-happy [network]="network" class="happy" />
    </div>
    <div i18n="@@subset-network.summary">
      {{ network.km | integer }} km, {{ network.nodeCount | integer }} nodes,
      {{ network.routeCount | integer }} routes
    </div>
    <div class="kpn-line">
      <kpn-osm-link-relation [relationId]="network.id" />
      <kpn-josm-relation [relationId]="network.id" />
    </div>
  `,
  styles: `
    .title {
      display: flex;
      align-items: center;
    }

    .percentage {
      padding-left: 10px;
    }

    .happy {
      padding-left: 10px;
      height: 25px;
      white-space: nowrap;
    }
  `,
  standalone: true,
  imports: [
    IntegerFormatPipe,
    JosmRelationComponent,
    LinkNetworkDetailsComponent,
    OsmLinkRelationComponent,
    SubsetNetworkHappyComponent,
  ],
})
export class SubsetNetworkComponent implements OnInit {
  @Input() network: NetworkAttributes;

  interpretedNetwork: InterpretedNetworkAttributes;

  ngOnInit(): void {
    this.interpretedNetwork = new InterpretedNetworkAttributes(this.network);
  }
}
