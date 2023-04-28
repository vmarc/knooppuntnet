import { ChangeDetectionStrategy } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { PageWidthService } from '@app/components/shared';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
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
    />

    <kpn-error />

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
          <kpn-subset-network-table [networks]="response.result.networks" />
        </ng-template>
        <ng-template #list>
          <kpn-subset-network-list [networks]="response.result.networks" />
        </ng-template>
      </div>
    </div>
  `,
})
export class SubsetNetworksPageComponent implements OnInit {
  large$: Observable<boolean>;

  readonly response$ = this.store.select(selectSubsetNetworksPage);

  constructor(
    private store: Store,
    private pageWidthService: PageWidthService
  ) {
    this.large$ = pageWidthService.current$.pipe(
      map(() => this.pageWidthService.isVeryLarge())
    );
  }

  ngOnInit(): void {
    this.store.dispatch(actionSubsetNetworksPageInit());
  }
}
