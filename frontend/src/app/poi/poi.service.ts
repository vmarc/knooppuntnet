import { HttpParams } from '@angular/common/http';
import { HttpClient } from '@angular/common/http';
import { LOCALE_ID } from '@angular/core';
import { Inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { LocationPoiParameters } from '@api/common/poi';
import { LocationPoiSummaryPage } from '@api/common/poi';
import { LocationPoisPage } from '@api/common/poi';
import { PoiLocationsPage } from '@api/common/poi';
import { ApiResponse } from '@api/custom';
import { Country } from '@api/custom';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';

@Injectable()
export class PoiService {
  constructor(
    @Inject(LOCALE_ID) public locale: string,
    private http: HttpClient,
    private store: Store
  ) {}

  locationPois(
    location: string,
    layers: string,
    pageSize: number,
    pageIndex: number
  ): Observable<ApiResponse<LocationPoisPage>> {
    const parameters: LocationPoiParameters = {
      pageSize,
      pageIndex,
    };
    const url = `/api/pois/${location}?layers=${layers}`;
    return this.http.post(url, parameters, { params: this.languageParams() });
  }

  locationPoiSummary(location: string): Observable<ApiResponse<LocationPoiSummaryPage>> {
    const url = `/api/pois/${location}/summary`;
    return this.http.post(url, {}, { params: this.languageParams() });
  }

  locations(country: Country): Observable<ApiResponse<PoiLocationsPage>> {
    const url = `/api/poi/locations/${country}/${this.locale}`;
    return this.http.get(url);
  }

  private languageParams(): HttpParams {
    return new HttpParams().set('language', this.locale);
  }
}
