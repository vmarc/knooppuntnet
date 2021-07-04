import { Injectable } from '@angular/core';
import { Params } from '@angular/router';
import { combineLatest } from 'rxjs';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { switchMap } from 'rxjs/operators';
import { AppService } from '../../../app.service';
import { LocationFactsPage } from '@api/common/location/location-facts-page';
import { ApiResponse } from '@api/custom/api-response';
import { LocationService } from '../location.service';

@Injectable()
export class LocationFactsPageService {
  readonly response: Observable<ApiResponse<LocationFactsPage>>;

  constructor(
    private locationService: LocationService,
    private appService: AppService
  ) {
    this.response = combineLatest([locationService.locationKey$]).pipe(
      switchMap(([locationKey]) =>
        this.appService.locationFacts(locationKey).pipe(
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
