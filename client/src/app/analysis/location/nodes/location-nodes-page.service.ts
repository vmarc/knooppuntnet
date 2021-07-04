import { Injectable } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { Params } from '@angular/router';
import { LocationNodesPage } from '@api/common/location/location-nodes-page';
import { LocationNodesParameters } from '@api/common/location/location-nodes-parameters';
import { LocationRoutesParameters } from '@api/common/location/location-routes-parameters';
import { ApiResponse } from '@api/custom/api-response';
import { select } from '@ngrx/store';
import { Store } from '@ngrx/store';
import { combineLatest } from 'rxjs';
import { BehaviorSubject } from 'rxjs';
import { Observable } from 'rxjs';
import { first } from 'rxjs/operators';
import { tap } from 'rxjs/operators';
import { switchMap } from 'rxjs/operators';
import { AppService } from '../../../app.service';
import { AppState } from '../../../core/core.state';
import { selectPreferencesItemsPerPage } from '../../../core/preferences/preferences.selectors';
import { LocationService } from '../location.service';

@Injectable()
export class LocationNodesPageService {
  response$: Observable<ApiResponse<LocationNodesPage>>;

  private _parameters$: BehaviorSubject<LocationNodesParameters>;

  constructor(
    private locationService: LocationService,
    private appService: AppService,
    private store: Store<AppState>
  ) {
    this.store
      .pipe(select(selectPreferencesItemsPerPage), first())
      .subscribe((itemsPerPage) => {
        this._parameters$ = new BehaviorSubject<LocationRoutesParameters>(
          new LocationNodesParameters(itemsPerPage, 0)
        );
        this.response$ = combineLatest([
          this._parameters$,
          locationService.locationKey$,
        ]).pipe(
          switchMap(([parameters, locationKey]) =>
            this.appService.locationNodes(locationKey, parameters).pipe(
              tap((response) => {
                if (response.result) {
                  this.locationService.nextSummary(
                    locationKey.name,
                    response.result.summary
                  );
                }
              })
            )
          )
        );
      });
  }

  params(params: Params): void {
    this.locationService.location(params);
  }

  pageChanged(event: PageEvent) {
    window.scroll(0, 0);
    this._parameters$.next({
      ...this._parameters$.getValue(),
      pageIndex: event.pageIndex,
      itemsPerPage: event.pageSize,
    });
  }
}
