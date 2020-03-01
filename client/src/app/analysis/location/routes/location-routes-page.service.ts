import {Injectable} from "@angular/core";
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
import {LocationService} from "../location.service";

@Injectable()
export class LocationRoutesPageService {

  response: Observable<ApiResponse<LocationRoutesPage>>;
  private readonly locationKey = new ReplaySubject<LocationKey>(1);
  private readonly parameters: BehaviorSubject<LocationRoutesParameters>;

  constructor(private locationService: LocationService,
              private appService: AppService) {

    this.parameters = new BehaviorSubject<LocationRoutesParameters>(new LocationRoutesParameters(5, 0));

    this.response = combineLatest([this.parameters, this.locationKey]).pipe(
      switchMap(([parameters, locationKey]) =>
        this.appService.locationRoutes(locationKey, parameters).pipe(
          tap(xx => {
            this.locationService.setSummary(locationKey.name, xx.result.summary);
          })
        )
      )
    );
  }

  setParams(locationKey): void {
    this.locationKey.next(locationKey);
  }
}
