import {Injectable} from "@angular/core";
import {PageEvent} from "@angular/material/paginator";
import {Params} from "@angular/router";
import {combineLatest} from "rxjs";
import {BehaviorSubject} from "rxjs";
import {Observable} from "rxjs";
import {tap} from "rxjs/operators";
import {switchMap} from "rxjs/operators";
import {AppService} from "../../../app.service";
import {LocationChangesPage} from "../../../kpn/api/common/location/location-changes-page";
import {LocationChangesParameters} from "../../../kpn/api/common/location/location-changes-parameters";
import {LocationRoutesParameters} from "../../../kpn/api/common/location/location-routes-parameters";
import {ApiResponse} from "../../../kpn/api/custom/api-response";
import {LocationService} from "../location.service";

@Injectable()
export class LocationChangesPageService {

  readonly response: Observable<ApiResponse<LocationChangesPage>>;

  private readonly _parameters: BehaviorSubject<LocationChangesParameters>;

  constructor(private locationService: LocationService, private appService: AppService) {
    this._parameters = new BehaviorSubject<LocationRoutesParameters>(new LocationChangesParameters(5, 0));
    this.response = combineLatest([this._parameters, locationService.locationKey$]).pipe(
      switchMap(([parameters, locationKey]) =>
        this.appService.locationChanges(locationKey, parameters).pipe(
          tap(response => {
            if (response.result) {
              this.locationService.nextSummary(locationKey.name, response.result.summary);
            }
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
