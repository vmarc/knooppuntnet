import { OnDestroy } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { actionLocationMapPageDestroy } from '../store/location.actions';
import { actionLocationMapPageInit } from '../store/location.actions';
import { selectLocationMapPage } from '../store/location.selectors';
import { selectLocationMapLayerStates } from '../store/location.selectors';
import { selectLocationNetworkType } from '../store/location.selectors';
import { LocationMapLayerService } from '@app/analysis/location/map/location-map-layer.service';

@Component({
  selector: 'kpn-location-map-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-location-page-header
      pageName="map"
      pageTitle="Map"
      i18n-pageTitle="@@location-map.title"
    />

    <kpn-error />

    <div *ngIf="response$ | async as response">
      <kpn-location-response [response]="response">
        <kpn-location-map
          [networkType]="networkType$ | async"
          [geoJson]="response.result.geoJson"
          [bounds]="response.result.bounds"
          [layerStates]="layerStates$ | async"
          [layers]="layers$ | async"
        />
      </kpn-location-response>
    </div>
  `,
})
export class LocationMapPageComponent implements OnInit, OnDestroy {
  protected readonly response$ = this.store.select(selectLocationMapPage);
  protected readonly layerStates$ = this.store.select(
    selectLocationMapLayerStates
  );
  protected readonly layers$ = this.locationMapLayerService.layers$;
  protected readonly networkType$ = this.store.select(
    selectLocationNetworkType
  );

  constructor(
    private locationMapLayerService: LocationMapLayerService,
    private store: Store
  ) {}

  ngOnInit(): void {
    this.store.dispatch(actionLocationMapPageInit());
  }

  ngOnDestroy(): void {
    this.store.dispatch(actionLocationMapPageDestroy());
  }
}
