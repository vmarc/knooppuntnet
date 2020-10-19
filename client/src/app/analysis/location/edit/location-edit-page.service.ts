import {Injectable} from "@angular/core";
import {Params} from "@angular/router";
import {Observable} from "rxjs";
import {tap} from "rxjs/operators";
import {switchMap} from "rxjs/operators";
import {AppService} from "../../../app.service";
import {LocationEditPage} from "../../../kpn/api/common/location/location-edit-page";
import {ApiResponse} from "../../../kpn/api/custom/api-response";
import {LocationService} from "../location.service";

@Injectable()
export class LocationEditPageService {

  readonly response$: Observable<ApiResponse<LocationEditPage>>;
  readonly httpError$: Observable<string>;

  constructor(private locationService: LocationService,
              private appService: AppService) {
    this.httpError$ = appService.httpError$;
    this.response$ = locationService.locationKey$.pipe(
      switchMap(locationKey =>
        this.appService.locationEdit(locationKey).pipe(
          tap(response => {
            this.locationService.nextSummary(locationKey.name, response.result.summary);
          })
        )
      )
    );
  }

  params(params: Params): void {
    this.locationService.location(params);
  }
}

