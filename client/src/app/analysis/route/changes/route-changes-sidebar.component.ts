import {ChangeDetectionStrategy} from "@angular/core";
import {Component} from "@angular/core";
import {RouteChangesService} from "./route-changes.service";

@Component({
  selector: "kpn-route-changes-sidebar",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-sidebar>
      <kpn-change-filter [filterOptions]="routeChangesService.filterOptions$ | async"></kpn-change-filter>
    </kpn-sidebar>
  `
})
export class RouteChangesSidebarComponent {
  constructor(public routeChangesService: RouteChangesService) {
  }
}
