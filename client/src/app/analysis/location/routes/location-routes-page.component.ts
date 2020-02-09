import {Component} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {flatMap} from "rxjs/operators";
import {tap} from "rxjs/operators";
import {map} from "rxjs/operators";
import {AppService} from "../../../app.service";
import {LocationRoutesPage} from "../../../kpn/api/common/location/location-routes-page";
import {ApiResponse} from "../../../kpn/api/custom/api-response";
import {LocationKey} from "../../../kpn/api/custom/location-key";
import {NetworkType} from "../../../kpn/api/custom/network-type";
import {Countries} from "../../../kpn/common/countries";
import {Subscriptions} from "../../../util/Subscriptions";

/* tslint:disable:template-i18n work-in-progress */
@Component({
  selector: "kpn-location-routes-page",
  template: `
    <kpn-location-page-header
      [locationKey]="locationKey"
      pageTitle="Routes"
      i18n-pageTitle="@@location-routes.title">
    </kpn-location-page-header>

    ROUTES

  `
})
export class LocationRoutesPageComponent {

  locationKey: LocationKey;
  response: ApiResponse<LocationRoutesPage>;
  private readonly subscriptions = new Subscriptions();

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService) {
    this.subscriptions.add(
      this.activatedRoute.params.pipe(
        map(params => {
          const networkType = NetworkType.withName(params["networkType"]);
          const country = Countries.withDomain(params["country"]);
          const name = params["location"];
          return new LocationKey(networkType, country, name);
        }),
        tap(locationKey => this.locationKey = locationKey),
        flatMap(locationKey => this.appService.locationRoutes(locationKey))
      ).subscribe(response => this.response = response)
    );
  }

}
