import { NgIf } from '@angular/common';
import { AsyncPipe } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { selectDefined } from '@app/core';
import { Store } from '@ngrx/store';
import { NetworkPageHeaderComponent } from '../components/network-page-header.component';
import { actionNetworkMapPageInit } from '../store/network.actions';
import { selectNetworkMapPositionFromUrl } from '../store/network.selectors';
import { selectNetworkId } from '../store/network.selectors';
import { selectNetworkMapPage } from '../store/network.selectors';
import { NetworkMapComponent } from './network-map.component';

@Component({
  selector: 'kpn-network-map-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-network-page-header
      pageName="map"
      pageTitle="Map"
      i18n-pageTitle="@@network-map.title"
    />

    <div *ngIf="response$ | async as response">
      <p
        *ngIf="!response.result; else networkFound"
        class="kpn-spacer-above"
        i18n="@@network-page.network-not-found"
      >
        Network not found
      </p>
      <ng-template #networkFound>
        <kpn-network-map
          [networkId]="networkId$ | async"
          [page]="response.result"
          [mapPositionFromUrl]="mapPositionFromUrl$ | async"
        />
      </ng-template>
    </div>
  `,
  standalone: true,
  imports: [NetworkPageHeaderComponent, NgIf, NetworkMapComponent, AsyncPipe],
})
export class NetworkMapPageComponent implements OnInit {
  readonly networkId$ = this.store.select(selectNetworkId);
  readonly response$ = selectDefined(this.store, selectNetworkMapPage);
  readonly mapPositionFromUrl$ = this.store.select(
    selectNetworkMapPositionFromUrl
  );

  constructor(private store: Store) {}

  ngOnInit(): void {
    this.store.dispatch(actionNetworkMapPageInit());
  }
}
