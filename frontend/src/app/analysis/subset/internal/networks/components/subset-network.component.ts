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

    @if (network().connectionCount > 0) {
      <div>{{ network().connectionCount | integer }} connections</div>
    }

    @if (network().brokenRouteCount > 0) {
      <div class="kpn-comma-list">
        <span i18n="@@subset-network.broken-routes">
          {{ network().brokenRouteCount | integer }} broken routes</span
        >
        <span class="kpn-warning">{{ network().brokenRoutePercentage }}</span>
      </div>
    }

    @if (network().integrity.hasChecks) {
      <div>
        <div class="kpn-comma-list">
          <span>
            <span i18n="@@subset-network.integrity.checks">
              {{ network().integrity.count | integer }} integrity checks</span
            >
            <span class="kpn-brackets">{{ network().integrity.coverage }}</span>
          </span>
          <span>
            @if (network().integrity.nokCount > 0) {
              not ok:
              <span class="kpn-warning">{{ network().integrity.nokRate }}</span>
            } @else {
              <span i18n="@@subset-network.integrity.all-ok">all ok!</span>
            }
          </span>
        </div>
      </div>
    }
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
  imports: [IntegerFormatPipe, LinkNetworkDetailsComponent, SubsetNetworkHappyComponent],
})
export class SubsetNetworkComponent implements OnInit {
  readonly network = input.required<NetworkAttributes>();

  interpretedNetwork: InterpretedNetworkAttributes;

  ngOnInit(): void {
    this.interpretedNetwork = new InterpretedNetworkAttributes(this.network());
  }
}
