import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { PageComponent } from '@app/components/shared/page';
import { RouterService } from '../../../shared/services/router.service';
import { NetworkPageHeaderComponent } from '../components/network-page-header.component';
import { NetworkMapSidebarComponent } from './components/network-map-sidebar.component';
import { NetworkMapComponent } from './components/network-map.component';
import { NetworkMapService } from './components/network-map.service';
import { NetworkMapPageService } from './network-map-page.service';

@Component({
  selector: 'kpn-network-map-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page [showFooter]="false">
      <kpn-network-page-header
        pageName="map"
        pageTitle="Map"
        i18n-pageTitle="@@network-map.title"
      />

      @if (service.response(); as response) {
        <div>
          @if (!response.result) {
            <p class="kpn-spacer-above" i18n="@@network-page.network-not-found">
              Network not found
            </p>
          } @else {
            <kpn-network-map
              [networkId]="service.networkId()"
              [page]="response.result"
              [mapPositionFromUrl]="mapPositionFromUrl()"
            />
          }
        </div>
      }
      <kpn-network-map-sidebar sidebar />
    </kpn-page>
  `,
  providers: [NetworkMapService, NetworkMapPageService, RouterService],
  standalone: true,
  imports: [
    NetworkMapComponent,
    NetworkMapSidebarComponent,
    NetworkPageHeaderComponent,
    PageComponent,
  ],
})
export class NetworkMapPageComponent implements OnInit {
  protected readonly service = inject(NetworkMapPageService);
  protected readonly mapPositionFromUrl = this.service.mapPositionFromUrl;

  ngOnInit(): void {
    this.service.onInit();
  }
}
