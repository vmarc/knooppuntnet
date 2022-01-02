import { ChangeDetectionStrategy } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { AppState } from '../../../core/core.state';
import { UserService } from '../../../services/user.service';
import { actionNetworkChangesImpact } from '../store/network.actions';
import { actionNetworkChangesItemsPerPage } from '../store/network.actions';
import { actionNetworkChangesPageIndex } from '../store/network.actions';
import { actionNetworkChangesPageInit } from '../store/network.actions';
import { selectNetworkChangesParametersItemsPerPage } from '../store/network.selectors';
import { selectNetworkChangesParametersImpact } from '../store/network.selectors';
import { selectNetworkChangesParametersPageIndex } from '../store/network.selectors';
import { selectNetworkChangesPage } from '../store/network.selectors';

@Component({
  selector: 'kpn-network-changes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-network-page-header
      pageName="changes"
      pageTitle="Changes"
      i18n-pageTitle="@@network-changes.title"
    >
    </kpn-network-page-header>

    <div class="kpn-spacer-above">
      <div *ngIf="!isLoggedIn()" i18n="@@network-changes.login-required">
        The details of network changes history are available to registered
        OpenStreetMap contributors only, after
        <kpn-link-login></kpn-link-login>
        .
      </div>

      <div *ngIf="isLoggedIn() && response$ | async as response">
        <div *ngIf="!response.result" i18n="@@network-page.network-not-found">
          Network not found
        </div>
        <div *ngIf="response.result">
          <p>
            <kpn-situation-on
              [timestamp]="response.situationOn"
            ></kpn-situation-on>
          </p>
          <kpn-changes
            [impact]="impact$ | async"
            (impactChange)="impactChanged($event)"
            [pageIndex]="pageIndex$ | async"
            (pageIndexChange)="pageIndexChanged($event)"
            [itemsPerPage]="itemsPerPage$ | async"
            (itemsPerPageChange)="itemsPerPageChanged($event)"
            [totalCount]="response.result.totalCount"
            [changeCount]="response.result.changes.length"
          >
            <kpn-items>
              <kpn-item
                *ngFor="let networkChangeInfo of response.result.changes"
                [index]="networkChangeInfo.rowIndex"
              >
                <kpn-network-change-set
                  [networkChangeInfo]="networkChangeInfo"
                ></kpn-network-change-set>
              </kpn-item>
            </kpn-items>
          </kpn-changes>
        </div>
      </div>
    </div>
  `,
})
export class NetworkChangesPageComponent implements OnInit {
  readonly response$ = this.store.select(selectNetworkChangesPage);
  readonly impact$ = this.store.select(selectNetworkChangesParametersImpact);
  readonly itemsPerPage$ = this.store.select(
    selectNetworkChangesParametersItemsPerPage
  );
  readonly pageIndex$ = this.store.select(
    selectNetworkChangesParametersPageIndex
  );

  constructor(
    private userService: UserService,
    private store: Store<AppState>
  ) {}

  ngOnInit(): void {
    this.store.dispatch(actionNetworkChangesPageInit());
  }

  isLoggedIn(): boolean {
    return this.userService.isLoggedIn();
  }

  impactChanged(impact: boolean): void {
    this.store.dispatch(actionNetworkChangesImpact({ impact }));
  }

  pageIndexChanged(pageIndex: number): void {
    this.store.dispatch(actionNetworkChangesPageIndex({ pageIndex }));
  }

  itemsPerPageChanged(itemsPerPage: number): void {
    this.store.dispatch(actionNetworkChangesItemsPerPage({ itemsPerPage }));
  }
}
