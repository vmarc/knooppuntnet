import { OnDestroy } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { PageComponent } from '@app/components/shared/page';
import { Store } from '@ngrx/store';
import { NetworkPageHeaderComponent } from '../components/network-page-header.component';
import { actionNetworkMapPageDestroy } from '../store/network.actions';
import { actionNetworkMapPageInit } from '../store/network.actions';
import { selectNetworkMapPositionFromUrl } from '../store/network.selectors';
import { selectNetworkId } from '../store/network.selectors';
import { selectNetworkMapPage } from '../store/network.selectors';
import { NetworkMapSidebarComponent } from './network-map-sidebar.component';
import { NetworkMapComponent } from './network-map.component';

@Component({
  selector: 'kpn-network-map-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      <kpn-network-page-header
        pageName="map"
        pageTitle="Map"
        i18n-pageTitle="@@network-map.title"
      />

      @if (apiResponse(); as response) {
        <div>
          @if (!response.result) {
            <p class="kpn-spacer-above" i18n="@@network-page.network-not-found">
              Network not found
            </p>
          } @else {
            <kpn-network-map
              [networkId]="networkId()"
              [page]="response.result"
              [mapPositionFromUrl]="mapPositionFromUrl()"
            />
          }
        </div>
      }
      <kpn-network-map-sidebar sidebar />
    </kpn-page>
  `,
  standalone: true,
  imports: [
    NetworkMapComponent,
    NetworkMapSidebarComponent,
    NetworkPageHeaderComponent,
    PageComponent,
  ],
})
export class NetworkMapPageComponent implements OnInit, OnDestroy {
  readonly networkId = this.store.selectSignal(selectNetworkId);
  readonly apiResponse = this.store.selectSignal(selectNetworkMapPage);
  readonly mapPositionFromUrl = this.store.selectSignal(selectNetworkMapPositionFromUrl);

  constructor(private store: Store) {}

  ngOnInit(): void {
    this.store.dispatch(actionNetworkMapPageInit());
  }

  ngOnDestroy(): void {
    this.store.dispatch(actionNetworkMapPageDestroy());
  }
}
