import {Component} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {flatMap} from "rxjs/operators";
import {tap} from "rxjs/operators";
import {map} from "rxjs/operators";
import {AppService} from "../../../app.service";
import {LocationFactsPage} from "../../../kpn/api/common/location/location-facts-page";
import {ApiResponse} from "../../../kpn/api/custom/api-response";
import {LocationKey} from "../../../kpn/api/custom/location-key";
import {NetworkType} from "../../../kpn/api/custom/network-type";
import {Countries} from "../../../kpn/common/countries";
import {Subscriptions} from "../../../util/Subscriptions";
import {LocationParams} from "../components/location-params";
import {LocationService} from "../location.service";

/* tslint:disable:template-i18n work-in-progress */
@Component({
  selector: "kpn-location-facts-page",
  template: `
    <kpn-location-page-header
      [locationKey]="locationKey"
      pageTitle="Facts"
      i18n-pageTitle="@@location-facts.title">
    </kpn-location-page-header>

    FACTS

  `
})
export class LocationFactsPageComponent {

  locationKey: LocationKey;
  response: ApiResponse<LocationFactsPage>;
  private readonly subscriptions = new Subscriptions();

  constructor(private activatedRoute: ActivatedRoute,
              private locationService: LocationService,
              private appService: AppService) {
    this.subscriptions.add(
      this.activatedRoute.params.pipe(
        map(params => LocationParams.toKey(params)),
        tap(locationKey => this.locationKey = locationKey),
        flatMap(locationKey => this.appService.locationFacts(locationKey))
      ).subscribe(response => {
        this.response = response;
        this.locationService.setSummary(this.locationKey.name, this.response.result.summary);
      })
    );
  }

}
