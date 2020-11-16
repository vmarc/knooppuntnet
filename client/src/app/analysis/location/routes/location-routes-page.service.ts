import {Injectable} from '@angular/core';
import {PageEvent} from '@angular/material/paginator';
import {Params} from '@angular/router';
import {combineLatest} from 'rxjs';
import {BehaviorSubject} from 'rxjs';
import {Observable} from 'rxjs';
import {switchMap} from 'rxjs/operators';
import {tap} from 'rxjs/operators';
import {AppService} from '../../../app.service';
import {LocationRoutesPage} from '../../../kpn/api/common/location/location-routes-page';
import {LocationRoutesParameters} from '../../../kpn/api/common/location/location-routes-parameters';
import {ApiResponse} from '../../../kpn/api/custom/api-response';
import {BrowserStorageService} from '../../../services/browser-storage.service';
import {LocationService} from '../location.service';

@Injectable()
export class LocationRoutesPageService {

  readonly response: Observable<ApiResponse<LocationRoutesPage>>;

  private readonly _parameters: BehaviorSubject<LocationRoutesParameters>;

  constructor(private locationService: LocationService,
              private appService: AppService,
              private browserStorageService: BrowserStorageService) {

    const itemsPerPage = this.browserStorageService.itemsPerPage;
    this._parameters = new BehaviorSubject<LocationRoutesParameters>(new LocationRoutesParameters(itemsPerPage, 0));
    this.response = combineLatest([this._parameters, locationService.locationKey$]).pipe(
      switchMap(([parameters, locationKey]) =>
        this.appService.locationRoutes(locationKey, parameters).pipe(
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
    this.browserStorageService.itemsPerPage = event.pageSize;
    this._parameters.next({...this._parameters.getValue(), pageIndex: event.pageIndex, itemsPerPage: event.pageSize});
  }

}
