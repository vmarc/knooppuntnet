import { Injectable } from '@angular/core';
import { Params } from '@angular/router';
import { LocationEditPage } from '@api/common/location/location-edit-page';
import { ApiResponse } from '@api/custom/api-response';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { switchMap } from 'rxjs/operators';
import { AppService } from '../../../app.service';
import { AppState } from '../../../core/core.state';
import { LocationService } from '../location.service';

@Injectable()
export class LocationEditPageService {
  readonly response$: Observable<ApiResponse<LocationEditPage>>;

  constructor(
    private locationService: LocationService,
    private appService: AppService,
    private store: Store<AppState>
  ) {
    this.response$ = locationService.locationKey$.pipe(
      switchMap((locationKey) =>
        this.appService.locationEdit(locationKey).pipe(
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
  }

  params(params: Params): void {
    this.locationService.location(params);
  }
}
