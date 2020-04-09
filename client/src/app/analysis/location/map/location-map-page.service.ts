import {Injectable} from "@angular/core";
import {Params} from "@angular/router";
import {combineLatest} from "rxjs";
import {Observable} from "rxjs";
import {tap} from "rxjs/operators";
import {switchMap} from "rxjs/operators";
import {AppService} from "../../../app.service";
import {LocationMapPage} from "../../../kpn/api/common/location/location-map-page";
import {ApiResponse} from "../../../kpn/api/custom/api-response";
import {NetworkType} from "../../../kpn/api/custom/network-type";
import {LocationService} from "../location.service";

@Injectable()
export class LocationMapPageService {

  readonly response$: Observable<ApiResponse<LocationMapPage>>;

  networkType: NetworkType;

  constructor(private locationService: LocationService, private appService: AppService) {
    this.response$ = combineLatest([locationService.locationKey]).pipe(
      switchMap(([locationKey]) =>
        this.appService.locationMap(locationKey).pipe(
          tap(response => {
            this.networkType = locationKey.networkType;
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
