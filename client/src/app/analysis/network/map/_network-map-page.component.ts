import { ChangeDetectionStrategy } from '@angular/core';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { PageService } from '../../../components/shared/page.service';
import { AppState } from '../../../core/core.state';
import { actionNetworkMapPageInit } from '../store/network.actions';
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
        ></kpn-network-map>
      </div>
    </div>
  `,
})
export class NetworkMapPageComponent implements OnInit, OnDestroy {
  readonly networkId$ = this.store.select(selectNetworkId);
  readonly response$ = this.store.select(selectNetworkMapPage);

  constructor(
    private store: Store<AppState>,
    private pageService: PageService
  ) {
    this.pageService.showFooter = false;
  }

  ngOnInit(): void {
    this.store.dispatch(actionNetworkMapPageInit());
  }

  ngOnDestroy(): void {
    this.pageService.showFooter = true;
  }
}
