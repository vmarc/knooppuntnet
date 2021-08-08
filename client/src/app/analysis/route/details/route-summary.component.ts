import {ChangeDetectionStrategy} from '@angular/core';
import {Component, Input} from '@angular/core';
import {RouteInfo} from '@api/common/route/route-info';

@Component({
  selector: 'kpn-route-summary',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div>
      <p i18n="@@route.meters">{{ route.summary.meters }} m</p>

      <p>
        <kpn-osm-link-relation
          [relationId]="route.summary.id"
        ></kpn-osm-link-relation>
        <span class="kpn-brackets-link">
          <kpn-josm-relation
            [relationId]="route.summary.id"
          ></kpn-josm-relation>
        </span>
      </p>

      <kpn-network-type
        [networkType]="route.summary.networkType"
      ></kpn-network-type>

      <p *ngIf="route.summary.country">
        <kpn-country-name [country]="route.summary.country"></kpn-country-name>
      </p>

      <p *ngIf="isRouteBroken()" class="kpn-line">
        <mat-icon svgIcon="warning"></mat-icon>
        <span i18n="@@route.broken">Something seems wrong with this route.</span>
      </p>

      <p *ngIf="isRouteIncomplete()" class="kpn-line">
        <mat-icon svgIcon="warning"></mat-icon>
        <markdown i18n="@@route.incomplete">
          Route definition is incomplete (has tag _"fixme=incomplete"_).
        </markdown>
      </p>

      <p *ngIf="!route.active" class="warning" i18n="@@route.not-active">
        This route is not active anymore.
      </p>

      <p *ngIf="isProposed()" class="kpn-line">
        <mat-icon svgIcon="warning" style="min-width: 24px"></mat-icon>
        <markdown i18n="@@route.proposed">
          Proposed: this route has tag _"state=proposed"_. The route is assumed to still be in
          a planning phase and likely not signposted in the field.
        </markdown>
      </p>

      <p *ngIf="isProposed()">
      </p>
    </div>
  `,
})
export class RouteSummaryComponent {
  @Input() route: RouteInfo;

  isRouteBroken() {
    return this.route.facts.map((fact) => fact.name).contains('RouteBroken');
  }

  isRouteIncomplete() {
    return this.route.facts
      .map((fact) => fact.name)
      .contains('RouteIncomplete');
  }

  isProposed() {
    const state = this.route.tags.get('state');
    return 'proposed' === state;
  }
}
