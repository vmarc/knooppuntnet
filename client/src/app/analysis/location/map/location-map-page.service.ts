import {Injectable} from "@angular/core";
import {Params} from "@angular/router";
import {combineLatest} from "rxjs";
import {ReplaySubject} from "rxjs";
import {Observable} from "rxjs";
import {tap} from "rxjs/operators";
import {switchMap} from "rxjs/operators";
import {AppService} from "../../../app.service";
import {LocationMapPage} from "../../../kpn/api/common/location/location-map-page";
import {ApiResponse} from "../../../kpn/api/custom/api-response";
import {LocationKey} from "../../../kpn/api/custom/location-key";
import {LocationParams} from "../components/location-params";
import {LocationService} from "../location.service";

@Injectable()
export class LocationMapPageService {

  readonly locationKey: Observable<LocationKey>;
  readonly response: Observable<ApiResponse<LocationMapPage>>;

  private readonly _locationKey: ReplaySubject<LocationKey>;

  constructor(private locationService: LocationService, private appService: AppService) {
    this._locationKey = new ReplaySubject<LocationKey>(1);
    this.locationKey = this._locationKey.asObservable();
    this.response = combineLatest([this._locationKey]).pipe(
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
    this._locationKey.next(LocationParams.toKey(params));
  }
}
