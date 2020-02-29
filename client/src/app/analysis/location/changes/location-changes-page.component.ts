import {Component} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {flatMap} from "rxjs/operators";
import {tap} from "rxjs/operators";
import {map} from "rxjs/operators";
import {AppService} from "../../../app.service";
import {LocationChangesPage} from "../../../kpn/api/common/location/location-changes-page";
import {ApiResponse} from "../../../kpn/api/custom/api-response";
import {LocationKey} from "../../../kpn/api/custom/location-key";
import {NetworkType} from "../../../kpn/api/custom/network-type";
import {Countries} from "../../../kpn/common/countries";
import {Subscriptions} from "../../../util/Subscriptions";
import {LocationService} from "../location.service";

/* tslint:disable:template-i18n work-in-progress */
@Component({
  selector: "kpn-location-changes-page",
  template: `
    <kpn-location-page-header
      [locationKey]="locationKey"
      pageTitle="Changes"
      i18n-pageTitle="@@location-changes.title">
    </kpn-location-page-header>

    CHANGES

  `
})
export class LocationChangesPageComponent {

  locationKey: LocationKey;
  response: ApiResponse<LocationChangesPage>;
  private readonly subscriptions = new Subscriptions();

  constructor(private activatedRoute: ActivatedRoute,
              private locationService: LocationService,
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
        flatMap(locationKey => this.appService.locationChanges(locationKey, null))
      ).subscribe(response => {
        this.response = response;
        this.locationService.setSummary(this.locationKey.name, this.response.result.summary);
      })
    );
  }

}
