import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { NetworkAttributes } from '@api/common/network/network-attributes';
import { IntegerFormatPipe } from '@app/shared/components/format/integer-format.pipe';
import { LinkNetworkDetailsComponent } from '@app/shared/components/link/link-network-details.component';
import { InterpretedNetworkAttributes } from './interpreted-network-attributes';
import { SubsetNetworkHappyComponent } from './subset-network-happy.component';

@Component({
  selector: 'ui-subset-network',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="kpn-align-center">
      <ui-link-network-details
        [networkId]="network().id"
        [routeType]="network().routeType"
        [networkName]="network().name"
      />
      <span class="percentage">{{ interpretedNetwork.percentageOk() }}</span>
      <ui-subset-network-happy [network]="network()" class="happy" />
    </div>
    <div i18n="@@subset-network.summary">
      {{ network().km | integer }} km, {{ network().nodeCount | integer }} nodes,
      {{ network().routeCount | integer }} routes
    </div>
  `,
  styles: `
    .percentage {
      padding-left: 10px;
    }

    .happy {
      padding-left: 10px;
      height: 25px;
      white-space: nowrap;
    }
  `,
  imports: [
    IntegerFormatPipe,
    LinkNetworkDetailsComponent,
    SubsetNetworkHappyComponent,
    IntegerFormatPipe,
  ],
})
export class SubsetNetworkComponent implements OnInit {
  network = input.required<NetworkAttributes>();

  interpretedNetwork: InterpretedNetworkAttributes;

  ngOnInit(): void {
    this.interpretedNetwork = new InterpretedNetworkAttributes(this.network());
  }
}
