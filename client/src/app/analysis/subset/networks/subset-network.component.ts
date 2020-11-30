import {OnInit} from '@angular/core';
import {ChangeDetectionStrategy} from '@angular/core';
import {Component, Input} from '@angular/core';
import {NetworkAttributes} from '../../../kpn/api/common/network/network-attributes';
import {InterpretedNetworkAttributes} from './interpreted-network-attributes';

@Component({
  selector: 'kpn-subset-network',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="title">
      <kpn-link-network-details [networkId]="network.id" [title]="network.name"></kpn-link-network-details>
      <span class="percentage">{{interpretedNetwork.percentageOk()}}</span>
      <kpn-subset-network-happy [network]="network" class="happy"></kpn-subset-network-happy>
    </div>
    <div i18n="@@subset-network.summary">{{network.km}} km, {{network.nodeCount}} nodes, {{network.routeCount}} routes</div>
    <div>
      <kpn-osm-link-relation [relationId]="network.id"></kpn-osm-link-relation>
      <kpn-josm-relation [relationId]="network.id"></kpn-josm-relation>
    </div>
  `,
  styles: [`

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

  `]
})
export class SubsetNetworkComponent implements OnInit {

  @Input() network: NetworkAttributes;

  interpretedNetwork: InterpretedNetworkAttributes;

  ngOnInit(): void {
    this.interpretedNetwork = new InterpretedNetworkAttributes(this.network);
  }
}
