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
        i18n="@@route.menu.details">
        Details
      </kpn-page-menu-option>

      <kpn-page-menu-option
        [link]="linkRouteMap()"
        i18n="@@route.menu.map">
        Map
      </kpn-page-menu-option>

      <kpn-page-menu-option
        [link]="linkRouteChanges()"
        [elementCount]="changeCount"
        i18n="@@route.menu.changes">
        Changes
      </kpn-page-menu-option>

    </kpn-page-menu>

  `
})
export class RoutePageHeaderComponent {

  @Input() routeId;
  @Input() routeName;
  @Input() pageName;
  @Input() changeCount;

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
