import { ChangeDetectionStrategy } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { filter } from 'rxjs/operators';
import { AppState } from '../../../core/core.state';
import { actionNetworkMapPageInit } from '../store/network.actions';
import { selectNetworkMapPositionFromUrl } from '../store/network.selectors';
import { selectNetworkId } from '../store/network.selectors';
import { selectNetworkMapPage } from '../store/network.selectors';

@Component({
  selector: 'kpn-network-map-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-network-page-header
      pageName="map"
      pageTitle="Map"
      i18n-pageTitle="@@network-map.title"
    >
    </kpn-network-page-header>

    <div *ngIf="response$ | async as response">
      <div *ngIf="!response.result">
        <p i18n="@@network-page.network-not-found">Network not found</p>
      </div>
      <div *ngIf="response.result">
        <kpn-network-map
          [networkId]="networkId$ | async"
          [page]="response.result"
          [mapPositionFromUrl]="mapPositionFromUrl$ | async"
        ></kpn-network-map>
      </div>
    </div>
  `,
})
export class NetworkMapPageComponent implements OnInit {
  readonly networkId$ = this.store.select(selectNetworkId);
  readonly response$ = this.store
    .select(selectNetworkMapPage)
    .pipe(filter((x) => x !== null));
  readonly mapPositionFromUrl$ = this.store.select(
    selectNetworkMapPositionFromUrl
  );

  constructor(private store: Store<AppState>) {}

  ngOnInit(): void {
    this.store.dispatch(actionNetworkMapPageInit());
  }
}
