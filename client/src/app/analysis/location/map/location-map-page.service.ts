import {Injectable} from "@angular/core";
import {Params} from "@angular/router";
import {combineLatest} from "rxjs";
import {Observable} from "rxjs";
import {tap} from "rxjs/operators";
import {switchMap} from "rxjs/operators";
import {AppService} from "../../../app.service";
import {MapService} from "../../../components/ol/services/map.service";
import {LocationMapPage} from "../../../kpn/api/common/location/location-map-page";
import {ApiResponse} from "../../../kpn/api/custom/api-response";
import {LocationService} from "../location.service";

@Injectable()
export class LocationMapPageService {

  readonly response$: Observable<ApiResponse<LocationMapPage>>;

  constructor(private locationService: LocationService, private appService: AppService, private mapService: MapService) {
    this.response$ = combineLatest([locationService.locationKey]).pipe(
      tap(([locationKey]) => {
        this.mapService.nextNetworkType(locationKey.networkType);
      }),
      switchMap(([locationKey]) =>
        this.appService.locationMap(locationKey).pipe(
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
