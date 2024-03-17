import { inject } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangesComponent } from '@app/analysis/components/changes';
import { ItemComponent } from '@app/components/shared/items';
import { ItemsComponent } from '@app/components/shared/items';
import { PageComponent } from '@app/components/shared/page';
import { SituationOnComponent } from '@app/components/shared/timestamp';
import { Store } from '@ngrx/store';
import { UserLinkLoginComponent } from '../../../shared/user';
import { UserStore } from '../../../shared/user/user.store';
import { NetworkPageHeaderComponent } from '../components/network-page-header.component';
import { actionNetworkChangesImpact } from '../store/network.actions';
import { actionNetworkChangesPageDestroy } from '../store/network.actions';
import { actionNetworkChangesPageSize } from '../store/network.actions';
import { actionNetworkChangesPageIndex } from '../store/network.actions';
import { actionNetworkChangesPageInit } from '../store/network.actions';
import { selectNetworkChangesPageSize } from '../store/network.selectors';
import { selectNetworkChangesImpact } from '../store/network.selectors';
import { selectNetworkChangesPageIndex } from '../store/network.selectors';
import { selectNetworkChangesPage } from '../store/network.selectors';
import { NetworkChangeSetComponent } from './components/network-change-set.component';
import { NetworkChangesSidebarComponent } from './components/network-changes-sidebar.component';

@Component({
  selector: 'kpn-network-changes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      <kpn-network-page-header
        pageName="changes"
        pageTitle="Changes"
        i18n-pageTitle="@@network-changes.title"
      />

      @if (apiResponse(); as response) {
        <div class="kpn-spacer-above">
          @if (!response.result) {
            <p i18n="@@network-page.network-not-found">Network not found</p>
          } @else {
            @if (loggedIn() === false) {
              <p i18n="@@network-changes.login-required">
                The details of network history are available to logged in OpenStreetMap contributors
                only.
              </p>
              <p>
                <kpn-user-link-login />
              </p>
            } @else {
              <p>
                <kpn-situation-on [timestamp]="response.situationOn" />
              </p>
              <kpn-changes
                [impact]="impact()"
                [pageSize]="pageSize()"
                [pageIndex]="pageIndex()"
                (impactChange)="onImpactChange($event)"
                (pageSizeChange)="onPageSizeChange($event)"
                (pageIndexChange)="onPageIndexChange($event)"
                [totalCount]="response.result.totalCount"
                [changeCount]="response.result.changes.length"
              >
                <kpn-items>
                  @for (
                    networkChangeInfo of response.result.changes;
                    track networkChangeInfo.rowIndex
                  ) {
                    <kpn-item [index]="networkChangeInfo.rowIndex">
                      <kpn-network-change-set [networkChangeInfo]="networkChangeInfo" />
                    </kpn-item>
                  }
                </kpn-items>
              </kpn-changes>
            }
          }
        </div>
      }
      <kpn-network-changes-sidebar sidebar />
    </kpn-page>
  `,
  standalone: true,
  imports: [
    ChangesComponent,
    ItemComponent,
    ItemsComponent,
    NetworkChangeSetComponent,
    NetworkChangesSidebarComponent,
    NetworkPageHeaderComponent,
    PageComponent,
    SituationOnComponent,
    UserLinkLoginComponent,
  ],
})
export class NetworkChangesPageComponent implements OnInit, OnDestroy {
  private readonly userStore = inject(UserStore);
  readonly loggedIn = this.userStore.loggedIn;

  private readonly store = inject(Store);
  readonly apiResponse = this.store.selectSignal(selectNetworkChangesPage);
  readonly impact = this.store.selectSignal(selectNetworkChangesImpact);
  readonly pageSize = this.store.selectSignal(selectNetworkChangesPageSize);
  readonly pageIndex = this.store.selectSignal(selectNetworkChangesPageIndex);

  ngOnInit(): void {
    this.store.dispatch(actionNetworkChangesPageInit());
  }

  ngOnDestroy(): void {
    this.store.dispatch(actionNetworkChangesPageDestroy());
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
