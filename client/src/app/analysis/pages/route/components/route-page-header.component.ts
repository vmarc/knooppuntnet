import {Component, Input} from "@angular/core";

@Component({
  selector: "kpn-route-page-header",
  template: `

    <kpn-page-header [pageTitle]="routeName" subject="route-page">
      <span i18n="@@route.title">Route</span>
      <span *ngIf="routeName">&nbsp;{{routeName}}</span>
    </kpn-page-header>

    <kpn-page-menu>

      <kpn-page-menu-option
        [link]="linkRouteDetails()"
        pageTitle="Details"
        i18n-pageTitle="@@route.menu.details">
      </kpn-page-menu-option>

      <kpn-page-menu-option
        [link]="linkRouteMap()"
        pageTitle="Map"
        i18n-pageTitle="@@route.menu.map">
      </kpn-page-menu-option>

      <kpn-page-menu-option
        [link]="linkRouteChanges()"
        pageTitle="Changes"
        i18n-pageTitle="@@route.menu.changes">
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
