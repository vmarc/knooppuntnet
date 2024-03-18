import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { PageComponent } from '@app/components/shared/page';
import { RouterService } from '../../../shared/services/router.service';
import { NetworkPageHeaderComponent } from '../components/network-page-header.component';
import { NetworkMapSidebarComponent } from './components/network-map-sidebar.component';
import { NetworkMapComponent } from './components/network-map.component';
import { NetworkMapService } from './components/network-map.service';
import { NetworkMapStore } from './network-map.store';

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

      @if (store.response(); as response) {
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
  providers: [NetworkMapService, NetworkMapStore, RouterService],
  standalone: true,
  imports: [
    NetworkMapComponent,
    NetworkMapSidebarComponent,
    NetworkPageHeaderComponent,
    PageComponent,
  ],
})
export class NetworkMapPageComponent {
  protected readonly store = inject(NetworkMapStore);
  protected readonly networkId = this.store.networkId();
  protected readonly mapPositionFromUrl = this.store.mapPositionFromUrl;
}
