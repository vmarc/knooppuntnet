import {Input} from "@angular/core";
import {Component} from "@angular/core";
import {LocationRoutesPage} from "../../../kpn/api/common/location/location-routes-page";
import {LocationRoutesPageService} from "./location-routes-page.service";

@Component({
  selector: "kpn-location-routes",
  template: `
    <div *ngIf="page.routes.isEmpty()" i18n="@@location-routes.no-routes">
      No routes
    </div>
    <kpn-location-route-table
      *ngIf="!page.routes.isEmpty()"
      (page)="service.pageChanged($event)"
      [timeInfo]="page.timeInfo"
      [routes]="page.routes"
      [routeCount]="page.summary.routeCount">
    </kpn-location-route-table>
  `
})
export class LocationRoutesComponent {

  @Input() page: LocationRoutesPage;

  constructor(public service: LocationRoutesPageService) {
  }

}
