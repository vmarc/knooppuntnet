import { NgIf } from '@angular/common';
import { AsyncPipe } from '@angular/common';
import { OnDestroy } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { ErrorComponent } from '@app/components/shared/error';
import { Store } from '@ngrx/store';
import { LocationPageHeaderComponent } from '../components/location-page-header.component';
import { LocationResponseComponent } from '../components/location-response.component';
import { actionLocationMapPageDestroy } from '../store/location.actions';
import { actionLocationMapPageInit } from '../store/location.actions';
import { selectLocationMapPage } from '../store/location.selectors';
import { selectLocationNetworkType } from '../store/location.selectors';
import { LocationMapComponent } from './location-map.component';
import { LocationMapService } from './location-map.service';

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

    <div *ngIf="apiResponse() as response">
      <kpn-location-response [response]="response">
        <kpn-location-map />
      </kpn-location-response>
    </div>
  `,
  standalone: true,
  imports: [
    LocationPageHeaderComponent,
    ErrorComponent,
    NgIf,
    LocationResponseComponent,
    LocationMapComponent,
    AsyncPipe,
  ],
})
export class LocationMapPageComponent implements OnInit, OnDestroy {
  protected readonly apiResponse = this.store.selectSignal(
    selectLocationMapPage
  );
  protected readonly networkType = this.store.selectSignal(
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
