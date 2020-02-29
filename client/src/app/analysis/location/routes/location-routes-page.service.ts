import {Injectable} from "@angular/core";
import {Params} from "@angular/router";
import {Subject} from "rxjs";
import {combineLatest} from "rxjs";
import {BehaviorSubject} from "rxjs";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";
import {shareReplay} from "rxjs/operators";
import {tap} from "rxjs/operators";
import {switchMap} from "rxjs/operators";
import {AppService} from "../../../app.service";
import {LocationRoutesPage} from "../../../kpn/api/common/location/location-routes-page";
import {LocationRoutesParameters} from "../../../kpn/api/common/location/location-routes-parameters";
import {ApiResponse} from "../../../kpn/api/custom/api-response";
import {LocationKey} from "../../../kpn/api/custom/location-key";
import {NetworkType} from "../../../kpn/api/custom/network-type";
import {Countries} from "../../../kpn/common/countries";
import {LocationParams} from "../components/location-params";
import {LocationService} from "../location.service";

@Injectable()
export class LocationRoutesPageService {

  response: Observable<ApiResponse<LocationRoutesPage>>;
  private readonly locationKey = new Subject<LocationKey>();
  private readonly parameters: BehaviorSubject<LocationRoutesParameters>;

  constructor(private locationService: LocationService,
              private appService: AppService) {
    console.log("LocationRoutesPageService constructor");

    this.parameters = new BehaviorSubject<LocationRoutesParameters>(new LocationRoutesParameters(5, 0));

    this.response = combineLatest([this.parameters, this.locationKey]).pipe(
      tap(xxx => console.log("TAP " + JSON.stringify(xxx, null, 2))),
      switchMap(([parameters, locationKey]) =>
        this.appService.locationRoutes(locationKey, parameters).pipe(
          tap(xx => {
            this.locationService.setSummary(locationKey.name, xx.result.summary);
          }),
          shareReplay(1)
        )
      )
    );
  }

  setParams(params: Params): void {
    this.locationKey.next(LocationParams.toKey(params));
  }

}
