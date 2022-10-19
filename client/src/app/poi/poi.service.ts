import { HttpParams } from '@angular/common/http';
import { HttpClient } from '@angular/common/http';
import { LOCALE_ID } from '@angular/core';
import { Inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { LocationPoiParameters } from '@api/common/poi/location-poi-parameters';
import { LocationPoiSummaryPage } from '@api/common/poi/location-poi-summary-page';
import { LocationPoisPage } from '@api/common/poi/location-pois-page';
import { ApiResponse } from '@api/custom/api-response';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { AppState } from '../core/core.state';

@Injectable()
export class PoiService {
  constructor(
    @Inject(LOCALE_ID) public locale: string,
    private http: HttpClient,
    private store: Store<AppState>
  ) {}

  locationPois(
    country: string,
    location: string,
    layers: string,
    pageSize: number,
    pageIndex: number
  ): Observable<ApiResponse<LocationPoisPage>> {
    const parameters: LocationPoiParameters = {
      pageSize,
      pageIndex,
    };
    const url = `/api/pois/${country}/${location}?layers=${layers}`;
    return this.http.post(url, parameters, { params: this.languageParams() });
  }

  locationPoiSummary(
    country: string,
    location: string
  ): Observable<ApiResponse<LocationPoiSummaryPage>> {
    const url = `/api/pois/${country}/${location}/summary`;
    return this.http.post(url, {}, { params: this.languageParams() });
  }

  private languageParams(): HttpParams {
    return new HttpParams().set('language', this.locale);
  }
}
