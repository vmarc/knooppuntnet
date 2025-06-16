import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { NetworkNotFoundComponent } from '@app/analysis/network/internal/components/network-not-found.component';
import { PageComponent } from '@app/shared/components/page/page.component';
import { RouterService } from '@app/shared/services/router.service';
import { NetworkPageHeaderComponent } from '../components/network-page-header.component';
import { NetworkMapComponent } from './components/network-map.component';
import { NetworkMapService } from './components/network-map.service';
import { NetworkMapPageService } from './network-map-page.service';

@Component({
  selector: 'ui-network-map-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-page>
      <ui-network-page-header pageName="map" pageTitle="Map" i18n-pageTitle="@@network-map.title" />

      @if (service.response(); as response) {
        <div>
          @if (!response.result) {
            <ui-network-not-found />
          } @else {
            <ui-network-map [networkId]="service.networkId()" [page]="response.result" />
          }
        </div>
      }
    </ui-page>
  `,
  providers: [NetworkMapService, NetworkMapPageService, RouterService],
  imports: [
    NetworkMapComponent,
    NetworkNotFoundComponent,
    NetworkPageHeaderComponent,
    PageComponent,
  ],
})
export class NetworkMapPageComponent implements OnInit {
  protected readonly service = inject(NetworkMapPageService);

  ngOnInit(): void {
    this.service.onInit();
  }
}
