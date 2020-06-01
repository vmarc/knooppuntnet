import {Injectable} from "@angular/core";
import {Params} from "@angular/router";
import {Observable} from "rxjs";
import {tap} from "rxjs/operators";
import {switchMap} from "rxjs/operators";
import {AppService} from "../../../app.service";
import {LocationSummaryPage} from "../../../kpn/api/common/location/location-summary-page";
import {ApiResponse} from "../../../kpn/api/custom/api-response";
import {LocationService} from "../location.service";

@Injectable()
export class LocationSummaryPageService {

  readonly response$: Observable<ApiResponse<LocationSummaryPage>>;

  constructor(private locationService: LocationService,
              private appService: AppService) {

    this.response$ = locationService.locationKey$.pipe(
      switchMap(locationKey =>
        this.appService.locationSummary(locationKey).pipe(
          tap(response => {
            this.locationService.setSummary(locationKey.name, response.result.summary);
          })
        )
      )
    );
  }

  params(params: Params): void {
    this.locationService.location(params);
  }
}

