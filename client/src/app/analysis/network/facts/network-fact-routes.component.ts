import {Component, Input} from "@angular/core";
import {List} from "immutable";
import {Ref} from "../../../kpn/shared/common/ref";

@Component({
  selector: "kpn-network-fact-routes",
  template: `
    <div *ngIf="routes.size == 1">
      <span i18n="TODO">Route</span>:
    </div>
    <div *ngIf="routes.size > 1">
      <span i18n="TODO">Routes</span>:
    </div>
    <div class="kpn-comma-list">
      <kpn-link-route
        *ngFor="let route of routes"
        [routeId]="route.id"
        [title]="route.name">
      </kpn-link-route>
    </div>
  `
})
export class NetworkFactRoutesComponent {
  @Input() routes: List<Ref>;
}
