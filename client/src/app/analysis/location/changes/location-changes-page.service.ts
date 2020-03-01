import {Injectable} from "@angular/core";
import {PageEvent} from "@angular/material/paginator";
import {Params} from "@angular/router";
import {combineLatest} from "rxjs";
import {BehaviorSubject} from "rxjs";
import {ReplaySubject} from "rxjs";
import {Observable} from "rxjs";
import {tap} from "rxjs/operators";
import {switchMap} from "rxjs/operators";
import {AppService} from "../../../app.service";
import {LocationChangesPage} from "../../../kpn/api/common/location/location-changes-page";
import {LocationChangesParameters} from "../../../kpn/api/common/location/location-changes-parameters";
import {LocationRoutesParameters} from "../../../kpn/api/common/location/location-routes-parameters";
import {ApiResponse} from "../../../kpn/api/custom/api-response";
import {LocationKey} from "../../../kpn/api/custom/location-key";
import {LocationParams} from "../components/location-params";
import {LocationService} from "../location.service";

@Injectable()
export class LocationChangesPageService {

  readonly locationKey: Observable<LocationKey>;
  readonly response: Observable<ApiResponse<LocationChangesPage>>;

  private readonly _locationKey: ReplaySubject<LocationKey>;
  private readonly _parameters: BehaviorSubject<LocationChangesParameters>;

  constructor(private locationService: LocationService, private appService: AppService) {
    this._locationKey = new ReplaySubject<LocationKey>(1);
    this.locationKey = this._locationKey.asObservable();
    this._parameters = new BehaviorSubject<LocationRoutesParameters>(new LocationChangesParameters(5, 0));
    this.response = combineLatest([this._parameters, this._locationKey]).pipe(
      switchMap(([parameters, locationKey]) =>
        this.appService.locationChanges(locationKey, parameters).pipe(
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

  pageChanged(event: PageEvent) {
    window.scroll(0, 0);
    this._parameters.next({...this._parameters.getValue(), pageIndex: event.pageIndex, itemsPerPage: event.pageSize});
  }

}
