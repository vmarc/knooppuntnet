import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { NetworkDetailsPage } from '@api/common/network/network-details-page';

@Component({
  selector: 'kpn-network-summary',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <p *ngIf="!page.active" class="warning" i18n="@@network-details.not-active">
      This network is not active anymore.
    </p>

    <p *ngIf="page.active" class="kpn-comma-list">
      <span>{{ page.attributes.km | integer }} km</span>
      <span
        >{{ page.summary.nodeCount | integer }}
        <ng-container i18n="@@network-details.nodes">nodes</ng-container>
      </span>
      <span>
        {{ page.summary.routeCount | integer }}
        <ng-container i18n="@@network-details.routes">routes</ng-container>
      </span>
    </p>

    <p>
      <kpn-country-name [country]="page.attributes.country"></kpn-country-name>
    </p>

    <p class="kpn-line">
      <kpn-osm-link-relation
        [relationId]="page.attributes.id"
      ></kpn-osm-link-relation>
      <kpn-josm-relation [relationId]="page.attributes.id"></kpn-josm-relation>
    </p>

    <p
      *ngIf="page.active && page.attributes.brokenRouteCount > 0"
      class="kpn-line"
    >
      <mat-icon svgIcon="warning"></mat-icon>
      <span i18n="@@network-details.contains-broken-routes"
        >This network contains broken (non-continuous) routes.</span
      >
    </p>

    <p *ngIf="page.active && isProposed()" class="kpn-line">
      <mat-icon svgIcon="warning" style="min-width: 24px"></mat-icon>
      <markdown i18n="@@network.proposed">
        Proposed: this network has tag _"state=proposed"_. The network is
        assumed to still be in a planning phase and likely not signposted in the
        field.
      </markdown>
    </p>
  `,
})
export class NetworkSummaryComponent {
  @Input() page: NetworkDetailsPage;

  isProposed() {
    const stateTag = this.page.tags.tags.find((t) => t.key === 'state');
    return stateTag && stateTag.value === 'proposed';
  }
}
