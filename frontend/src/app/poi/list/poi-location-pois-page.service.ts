import { effect } from '@angular/core';
import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { signal } from '@angular/core';
import { PoiLocationsPage } from '@api/common';
import { LocationPoiSummaryPage } from '@api/common';
import { LocationPoisPage } from '@api/common';
import { Country } from '@api/custom';
import { ApiResponse } from '@api/custom';
import { PreferencesService } from '@app/core';
import { RouterService } from '../../shared/services/router.service';
import { PoiService } from '../poi.service';

export class PoiLocationPoisPageService {
  private readonly poiService = inject(PoiService);
  private readonly routerService = inject(RouterService);
  private readonly preferencesService = inject(PreferencesService);

  private readonly _country = signal<Country | null>(null);
  private readonly _location = signal<string | null>(null);
  private readonly _layers = signal<string | null>(null);

  private readonly _locationsResponse = signal<ApiResponse<PoiLocationsPage>>(null);
  private readonly _summaryResponse = signal<ApiResponse<LocationPoiSummaryPage>>(null);
  private readonly _poisResponse = signal<ApiResponse<LocationPoisPage>>(null);
  private readonly _pageIndex = signal<number>(0);

  readonly country = this._country.asReadonly();
  readonly location = this._location.asReadonly();
  readonly layers = this._layers.asReadonly();
  readonly locationsResponse = this._locationsResponse.asReadonly();
  readonly summaryResponse = this._summaryResponse.asReadonly();
  readonly poisResponse = this._poisResponse.asReadonly();
  readonly locationNode = computed(() => this.locationsResponse()?.result.locationNode);

  readonly pageIndex = this._pageIndex.asReadonly();
  readonly pageSize = this.preferencesService.pageSize;

  constructor() {
    effect(() => {});
  }

  onInit(): void {
    // TODO SIGNAL interprete location and filter queryParams to go directly to poi list
    // const location = this.routerService.queryParam('location');
    // const layers = this.routerService.queryParam('layers');
  }

  setCountry(country: Country) {
    this._country.set(country);
    this.loadLocations();
  }

  setLocation(location: string) {
    this._location.set(location);
    this.loadSummary();
  }

  setLayers(layers: string) {
    this._layers.set(layers);
  }

  listPois() {
    this.loadPois();
  }

  setPageSize(pageSize: number) {
    this.preferencesService.setPageSize(pageSize);
  }

  setPageIndex(pageIndex: number) {
    this._pageIndex.set(pageIndex);
  }

  private loadLocations(): void {
    this.poiService
      .locations(this.country())
      .subscribe((response) => this._locationsResponse.set(response));
  }

  private loadPois(): void {
    this.poiService
      .locationPois(this.location(), this.layers(), this.pageSize(), this.pageIndex())
      .subscribe((response) => this._poisResponse.set(response));
  }

  private loadSummary(): void {
    this.poiService
      .locationPoiSummary(this.location())
      .subscribe((response) => this._summaryResponse.set(response));
  }
}
