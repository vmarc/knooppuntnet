import { NgIf } from '@angular/common';
import { AsyncPipe } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { PageComponent } from '@app/components/shared/page';
import { Store } from '@ngrx/store';
import { NetworkPageHeaderComponent } from '../components/network-page-header.component';
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

      <div *ngIf="apiResponse() as response">
        <p
          *ngIf="!response.result; else networkFound"
          class="kpn-spacer-above"
          i18n="@@network-page.network-not-found"
        >
          Network not found
        </p>
        <ng-template #networkFound>
          <kpn-network-map
            [networkId]="networkId()"
            [page]="response.result"
            [mapPositionFromUrl]="mapPositionFromUrl()"
          />
        </ng-template>
      </div>
      <kpn-network-map-sidebar sidebar />
    </kpn-page>
  `,
  standalone: true,
  imports: [
    NetworkPageHeaderComponent,
    NgIf,
    NetworkMapComponent,
    AsyncPipe,
    PageComponent,
    NetworkMapSidebarComponent,
  ],
})
export class NetworkMapPageComponent implements OnInit {
  readonly networkId = this.store.selectSignal(selectNetworkId);
  readonly apiResponse = this.store.selectSignal(selectNetworkMapPage);
  readonly mapPositionFromUrl = this.store.selectSignal(
    selectNetworkMapPositionFromUrl
  );

  constructor(private store: Store) {}

  ngOnInit(): void {
    this.store.dispatch(actionNetworkMapPageInit());
  }
}
