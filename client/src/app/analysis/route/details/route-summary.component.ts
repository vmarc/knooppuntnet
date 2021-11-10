import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { RouteInfo } from '@api/common/route/route-info';

@Component({
  selector: 'kpn-route-summary',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div>
      <p i18n="@@route.meters">{{ route.summary.meters | integer }} m</p>

      <p *ngIf="route.summary.country">
        <kpn-country-name [country]="route.summary.country"></kpn-country-name>
      </p>

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

      <p *ngIf="isRouteBroken()" class="kpn-line">
        <mat-icon svgIcon="warning"></mat-icon>
        <span i18n="@@route.broken"
          >There seems to be something wrong with this route.</span
        >
      </p>

      <p *ngIf="isRouteIncomplete()" class="kpn-line">
        <mat-icon svgIcon="warning"></mat-icon>
        <markdown i18n="@@route.incomplete">
          Route definition is incomplete (has tag *"fixme=incomplete"*).
        </markdown>
      </p>

      <p *ngIf="!route.active" class="warning" i18n="@@route.not-active">
        This route is not active anymore.
      </p>

      <p *ngIf="isProposed()" class="kpn-line">
        <mat-icon svgIcon="warning" style="min-width: 24px"></mat-icon>
        <markdown i18n="@@route.proposed">
          Proposed: this route has a tag _"state=proposed"_. The route is
          assumed to still be in a planning phase and likely not signposted in
          the field.
        </markdown>
      </p>

      <p *ngIf="isRouteNameDerivedFromNodes()" class="kpn-line">
        <span i18n="@@route.name-derived-from-nodes">
          The route name is derived from the route nodes, rather than the tags
          in the route relation.
        </span>
      </p>
    </div>
  `,
})
export class RouteSummaryComponent {
  @Input() route: RouteInfo;

  isRouteBroken() {
    return this.route.facts.includes('RouteBroken');
  }

  isRouteIncomplete() {
    return this.route.facts.includes('RouteIncomplete');
  }

  isProposed() {
    const stateTag = this.route.tags.tags.find((t) => t.key === 'state');
    return stateTag && stateTag.value === 'proposed';
  }

  isRouteNameDerivedFromNodes(): boolean {
    return this.route.analysis?.nameDerivedFromNodes === true;
  }
}
