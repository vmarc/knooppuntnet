import {Injectable} from "@angular/core";
import {PageEvent} from "@angular/material/paginator";
import {Params} from "@angular/router";
import {ReplaySubject} from "rxjs";
import {combineLatest} from "rxjs";
import {BehaviorSubject} from "rxjs";
import {Observable} from "rxjs";
import {switchMap} from "rxjs/operators";
import {tap} from "rxjs/operators";
import {AppService} from "../../../app.service";
import {LocationRoutesPage} from "../../../kpn/api/common/location/location-routes-page";
import {LocationRoutesParameters} from "../../../kpn/api/common/location/location-routes-parameters";
import {ApiResponse} from "../../../kpn/api/custom/api-response";
import {LocationKey} from "../../../kpn/api/custom/location-key";
import {LocationParams} from "../components/location-params";
import {LocationService} from "../location.service";

@Injectable()
export class LocationRoutesPageService {

  readonly locationKey: Observable<LocationKey>;
  readonly response: Observable<ApiResponse<LocationRoutesPage>>;

  private readonly _locationKey: ReplaySubject<LocationKey>;
  private readonly _parameters: BehaviorSubject<LocationRoutesParameters>;

  constructor(private locationService: LocationService, private appService: AppService) {
    this._locationKey = new ReplaySubject<LocationKey>(1);
    this.locationKey = this._locationKey.asObservable();
    this._parameters = new BehaviorSubject<LocationRoutesParameters>(new LocationRoutesParameters(5, 0));
    this.response = combineLatest([this._parameters, this._locationKey]).pipe(
      switchMap(([parameters, locationKey]) =>
        this.appService.locationRoutes(locationKey, parameters).pipe(
          tap(locationRoutesResponse => {
            this.locationService.setSummary(locationKey.name, locationRoutesResponse.result.summary);
          })
        )
      )
    );
  }

  params(params: Params): void {
    this._locationKey.next(LocationParams.toKey(params));
  }

  pageChanged(event: PageEvent) {
    window.scroll(0, 0);
    this._parameters.next({...this._parameters.getValue(), pageIndex: event.pageIndex, itemsPerPage: event.pageSize});
  }

}
