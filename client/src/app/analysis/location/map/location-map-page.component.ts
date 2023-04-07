import { OnDestroy } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { actionLocationMapPageDestroy } from '../store/location.actions';
import { actionLocationMapPageInit } from '../store/location.actions';
import { selectLocationMapPage } from '../store/location.selectors';
import { selectLocationNetworkType } from '../store/location.selectors';
import { LocationMapService } from '@app/analysis/location/map/location-map.service';

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
        <kpn-location-map />
      </kpn-location-response>
    </div>
  `,
})
export class LocationMapPageComponent implements OnInit, OnDestroy {
  protected readonly response$ = this.store.select(selectLocationMapPage);
  protected readonly networkType$ = this.store.select(
    selectLocationNetworkType
  );

  constructor(
    private locationMapLayerService: LocationMapService,
    private store: Store
  ) {}

  ngOnInit(): void {
    this.store.dispatch(actionLocationMapPageInit());
  }

  ngOnDestroy(): void {
    this.store.dispatch(actionLocationMapPageDestroy());
  }
}
