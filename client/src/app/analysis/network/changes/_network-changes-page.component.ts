import { ChangeDetectionStrategy } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { selectUserLoggedIn } from '@app/core/user/user.selectors';
import { Store } from '@ngrx/store';
import { AppState } from '../../../core/core.state';
import { actionNetworkChangesImpact } from '../store/network.actions';
import { actionNetworkChangesPageSize } from '../store/network.actions';
import { actionNetworkChangesPageIndex } from '../store/network.actions';
import { actionNetworkChangesPageInit } from '../store/network.actions';
import { selectNetworkChangesPageSize } from '../store/network.selectors';
import { selectNetworkChangesImpact } from '../store/network.selectors';
import { selectNetworkChangesPageIndex } from '../store/network.selectors';
import { selectNetworkChangesPage } from '../store/network.selectors';

@Component({
  selector: 'kpn-network-changes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-network-page-header
      pageName="changes"
      pageTitle="Changes"
      i18n-pageTitle="@@network-changes.title"
    />

    <div *ngIf="response$ | async as response" class="kpn-spacer-above">
      <p
        *ngIf="!response.result; else networkFound"
        i18n="@@network-page.network-not-found"
      >
        Network not found
      </p>
      <ng-template #networkFound>
        <div
          *ngIf="(loggedIn$ | async) === false; else loggedIn"
          i18n="@@network-changes.login-required"
        >
          The details of network changes history are available to registered
          OpenStreetMap contributors only, after
          <kpn-link-login />
          .
        </div>
        <ng-template #loggedIn>
          <p>
            <kpn-situation-on [timestamp]="response.situationOn" />
          </p>
          <kpn-changes
            [impact]="impact$ | async"
            [pageSize]="pageSize$ | async"
            [pageIndex]="pageIndex$ | async"
            (impactChange)="onImpactChange($event)"
            (pageSizeChange)="onPageSizeChange($event)"
            (pageIndexChange)="onPageIndexChange($event)"
            [totalCount]="response.result.totalCount"
            [changeCount]="response.result.changes.length"
          >
            <kpn-items>
              <kpn-item
                *ngFor="let networkChangeInfo of response.result.changes"
                [index]="networkChangeInfo.rowIndex"
              >
                <kpn-network-change-set [networkChangeInfo]="networkChangeInfo" />
              </kpn-item>
            </kpn-items>
          </kpn-changes>
        </ng-template>
      </ng-template>
    </div>
  `,
})
export class NetworkChangesPageComponent implements OnInit {
  readonly response$ = this.store.select(selectNetworkChangesPage);
  readonly impact$ = this.store.select(selectNetworkChangesImpact);
  readonly pageSize$ = this.store.select(selectNetworkChangesPageSize);
  readonly pageIndex$ = this.store.select(selectNetworkChangesPageIndex);
  readonly loggedIn$ = this.store.select(selectUserLoggedIn);

  constructor(private store: Store) {}

  ngOnInit(): void {
    this.store.dispatch(actionNetworkChangesPageInit());
  }

  onImpactChange(impact: boolean): void {
    this.store.dispatch(actionNetworkChangesImpact({ impact }));
  }

  onPageSizeChange(pageSize: number): void {
    this.store.dispatch(actionNetworkChangesPageSize({ pageSize }));
  }

  onPageIndexChange(pageIndex: number): void {
    this.store.dispatch(actionNetworkChangesPageIndex({ pageIndex }));
  }
}
