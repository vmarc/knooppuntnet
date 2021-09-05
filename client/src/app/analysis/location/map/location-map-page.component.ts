import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { AppState } from '../../../core/core.state';
import { actionLocationMapPageInit } from '../store/location.actions';
import { selectLocationMapPage } from '../store/location.selectors';
import {selectLocationKey} from "../store/location.selectors";
import {map} from "rxjs/operators";

@Component({
  selector: 'kpn-location-map-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-location-page-header
      pageName="map"
      pageTitle="Map"
      i18n-pageTitle="@@location-map.title"
    >
    </kpn-location-page-header>

    <kpn-error></kpn-error>

    <div *ngIf="response$ | async as response">
      <kpn-location-response [response]="response">
        <kpn-location-map
          [networkType]="networkType$ | async"
          [geoJson]="response.result.geoJson"
          [bounds]="response.result.bounds"
        >
        </kpn-location-map>
      </kpn-location-response>
    </div>
  `,
})
export class LocationMapPageComponent implements OnInit {
  readonly response$ = this.store.select(selectLocationMapPage);
  readonly networkType$ = this.store
    .select(selectLocationKey)
    .pipe(map((key) => key.networkType));

  constructor(private store: Store<AppState>) {}

  ngOnInit(): void {
    this.store.dispatch(actionLocationMapPageInit());
  }
}
