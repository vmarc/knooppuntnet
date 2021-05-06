import { ChangeDetectionStrategy } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { PageWidthService } from '../../../components/shared/page-width.service';
import { AppState } from '../../../core/core.state';
import { NetworkCacheService } from '../../../services/network-cache.service';
import { actionSubsetNetworksPageInit } from '../store/subset.actions';
import { selectSubsetNetworksPage } from '../store/subset.selectors';

@Component({
  selector: 'kpn-subset-networks-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-subset-page-header-block
      pageName="networks"
      pageTitle="Networks"
      i18n-pageTitle="@@subset-networks.title"
    ></kpn-subset-page-header-block>

    <kpn-error></kpn-error>

    <div *ngIf="response$ | async as response" class="kpn-spacer-above">
      <div
        *ngIf="response.result.networks.length === 0"
        i18n="@@subset-networks.no-networks"
      >
        No networks
      </div>
      <div *ngIf="response.result.networks.length > 0">
        <p>
          <kpn-situation-on
            [timestamp]="response.situationOn"
          ></kpn-situation-on>
        </p>

        <markdown i18n="@@subset-networks.summary">
          _There are __{{ response.result.networkCount | integer }}__ networks,
          with a total of __{{ response.result.nodeCount | integer }}__ nodes
          and __{{ response.result.routeCount | integer }}__ routes with an
          overall length of __{{ response.result.km | integer }}__ km._
        </markdown>

        <ng-container
          *ngIf="large$ | async; then table; else list"
        ></ng-container>
        <ng-template #table>
          <kpn-subset-network-table
            [networks]="response.result.networks"
          ></kpn-subset-network-table>
        </ng-template>
        <ng-template #list>
          <kpn-subset-network-list
            [networks]="response.result.networks"
          ></kpn-subset-network-list>
        </ng-template>
      </div>
    </div>
  `,
})
export class SubsetNetworksPageComponent implements OnInit {
  large$: Observable<boolean>;

  readonly response$ = this.store.select(selectSubsetNetworksPage).pipe(
    tap((response) => {
      if (response && response.result) {
        response.result.networks.forEach((networkAttributes) => {
          this.networkCacheService.setNetworkName(
            networkAttributes.id,
            networkAttributes.name
          );
        });
      }
    })
  );

  constructor(
    private store: Store<AppState>,
    private pageWidthService: PageWidthService,
    private networkCacheService: NetworkCacheService
  ) {
    this.large$ = pageWidthService.current$.pipe(
      map(() => this.pageWidthService.isVeryLarge())
    );
  }

  ngOnInit(): void {
    this.store.dispatch(actionSubsetNetworksPageInit());
  }
}
