import {Injectable} from "@angular/core";
import {PageEvent} from "@angular/material/paginator";
import {Params} from "@angular/router";
import {combineLatest} from "rxjs";
import {BehaviorSubject} from "rxjs";
import {Observable} from "rxjs";
import {tap} from "rxjs/operators";
import {switchMap} from "rxjs/operators";
import {AppService} from "../../../app.service";
import {LocationNodesPage} from "../../../kpn/api/common/location/location-nodes-page";
import {LocationNodesParameters} from "../../../kpn/api/common/location/location-nodes-parameters";
import {LocationRoutesParameters} from "../../../kpn/api/common/location/location-routes-parameters";
import {ApiResponse} from "../../../kpn/api/custom/api-response";
import {LocationService} from "../location.service";

@Injectable()
export class LocationNodesPageService {

  readonly response: Observable<ApiResponse<LocationNodesPage>>;

  private readonly _parameters: BehaviorSubject<LocationNodesParameters>;

  constructor(private locationService: LocationService, private appService: AppService) {
    this._parameters = new BehaviorSubject<LocationRoutesParameters>(new LocationNodesParameters(5, 0));
    this.response = combineLatest([this._parameters, locationService.locationKey]).pipe(
      switchMap(([parameters, locationKey]) =>
        this.appService.locationNodes(locationKey, parameters).pipe(
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

  pageChanged(event: PageEvent) {
    window.scroll(0, 0);
    this._parameters.next({...this._parameters.getValue(), pageIndex: event.pageIndex, itemsPerPage: event.pageSize});
  }
}
