import {Component, Input} from "@angular/core";

@Component({
  selector: "kpn-route-page-header",
  template: `

    <kpn-page-header subject="route-page">{{'Route ' + routeName}}</kpn-page-header>

    <kpn-page-menu>

      <kpn-page-menu-option
        [link]="linkRouteDetails()"
        pageTitle="Details">
      </kpn-page-menu-option>

      <kpn-page-menu-option
        [link]="linkRouteMap()"
        pageTitle="Map">
      </kpn-page-menu-option>

      <kpn-page-menu-option
        [link]="linkRouteChanges()"
        pageTitle="Changes">
      </kpn-page-menu-option>

    </kpn-page-menu>

  `
})
export class RoutePageHeaderComponent {

  @Input() routeId;
  @Input() routeName;
  @Input() pageName;

  linkRouteDetails(): string {
    return this.linkRoute("");
  }

  linkRouteMap(): string {
    return this.linkRoute("/map");
  }

  linkRouteChanges(): string {
    return this.linkRoute("/changes");
  }

  private linkRoute(suffix: string): string {
    return `/analysis/route/${this.routeId}${suffix}`;
  }

}
