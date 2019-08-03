import {Component, Input} from "@angular/core";
import {RouteInfo} from "../../../../kpn/shared/route/route-info";

@Component({
  selector: "kpn-route-summary",
  template: `
    <div>

      <p *ngIf="!route.ignored">
        {{route.summary.meters}} m
      </p>

      <p>
        <kpn-osm-link-relation [relationId]="route.summary.id"></kpn-osm-link-relation>
        (
        <kpn-josm-relation [relationId]="route.summary.id"></kpn-josm-relation>
        )
      </p>

      <kpn-network-type [networkType]="route.summary.networkType"></kpn-network-type>

      <p *ngIf="route.summary.country">
        <kpn-country-name [country]="route.summary.country"></kpn-country-name>
      </p>

      <p *ngIf="!route.ignored && isRouteBroken()" class="kpn-line">
        <mat-icon svgIcon="warning"></mat-icon>
        <span i18n="@@route.broken">This route seems broken.</span>
      </p>

      <p *ngIf="!route.ignored && isRouteIncomplete()" class="kpn-line">
        <mat-icon svgIcon="warning"></mat-icon>
        <markdown i18n="@@route.incomplete">
          Route definition is incomplete (has tag _"fixme=incomplete"_).
        </markdown>        
      </p>

      <p *ngIf="route.ignored" i18n="@@route.ignored">
        This route is not included in the analysis.
      </p>

      <p *ngIf="!route.ignored && !route.active" class="warning" i18n="@@route.not-active">
        This route is not active anymore.
      </p>

    </div>
  `
})
export class RouteSummaryComponent {

  @Input() route: RouteInfo;

  isRouteBroken() {
    return this.route.facts.map(fact => fact.name).contains("RouteBroken");
  }

  isRouteIncomplete() {
    return this.route.facts.map(fact => fact.name).contains("RouteIncomplete");
  }

}
