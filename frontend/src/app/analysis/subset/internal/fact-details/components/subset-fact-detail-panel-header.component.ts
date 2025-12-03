import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { NetworkFactRefs } from '@api/common/subset/network-fact-refs';
import { FactDefinition } from '@app/analysis/fact/components/facts';
import { IconNetworkComponent } from '@app/shared/components/icon/icon-network.component';
import { ActionButtonNetworkComponent } from '../../../../components/action/action-button-network.component';

@Component({
  selector: 'ui-subset-fact-detail-panel-header',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="kpn-line">
      <ui-icon-network />
      @if (networkFactRefs().networkId === 0) {
        <!-- TODO add button to load all routes -->
        <span i18n="@@subset-facts.orphan-routes" class="free-route-indent">Free routes</span>
      } @else {
        <ui-action-button-network [relationId]="networkFactRefs().networkId" />
        <a [routerLink]="link()">
          {{ networkFactRefs().networkName }}
        </a>
      }
      @if (fact().hasNodeRefs()) {
        <span>{{ networkFactRefs().factRefs.length }}</span>
        <span i18n="@@subset-facts.nodes"> node(s) </span>
      }
      @if (fact().hasRouteRefs()) {
        <span>{{ networkFactRefs().factRefs.length }}</span>
        <span i18n="@@subset-facts.routes">route(s)</span>
      }
    </div>
  `,
  styleUrl: '../subset-fact-details-page.component.scss',
  imports: [ActionButtonNetworkComponent, IconNetworkComponent, RouterLink],
})
export class SubsetFactDetailPanelHeaderComponent {
  readonly fact = input.required<FactDefinition>();
  readonly networkFactRefs = input.required<NetworkFactRefs>();
  protected readonly link = computed(() => '/analysis/network/' + this.networkFactRefs().networkId);
}
