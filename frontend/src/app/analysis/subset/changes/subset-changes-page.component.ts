import { AsyncPipe } from '@angular/common';
import { inject } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeLocationAnalysisSummaryComponent } from '@app/analysis/components/change-set';
import { ChangeNetworkAnalysisSummaryComponent } from '@app/analysis/components/change-set';
import { ChangesComponent } from '@app/analysis/components/changes';
import { ErrorComponent } from '@app/components/shared/error';
import { ItemComponent } from '@app/components/shared/items';
import { ItemsComponent } from '@app/components/shared/items';
import { PageComponent } from '@app/components/shared/page';
import { SituationOnComponent } from '@app/components/shared/timestamp';
import { Store } from '@ngrx/store';
import { UserLinkLoginComponent } from '../../../shared/user';
import { UserStore } from '../../../shared/user/user.store';
import { SubsetPageHeaderBlockComponent } from '../components/subset-page-header-block.component';
import { actionSubsetChangesPageSize } from '../store/subset.actions';
import { actionSubsetChangesPageImpact } from '../store/subset.actions';
import { actionSubsetChangesPageIndex } from '../store/subset.actions';
import { actionSubsetChangesPageInit } from '../store/subset.actions';
import { selectSubsetChangesPageImpact } from '../store/subset.selectors';
import { selectSubsetChangesPageSize } from '../store/subset.selectors';
import { selectSubsetChangesPageIndex } from '../store/subset.selectors';
import { selectSubsetChangesPage } from '../store/subset.selectors';
import { SubsetChangesSidebarComponent } from './components/subset-changes-sidebar.component';

@Component({
  selector: 'kpn-subset-changes-page',
  template: `
    <kpn-page>
      <kpn-subset-page-header-block
        pageName="changes"
        pageTitle="Changes"
        i18n-pageTitle="@@subset-changes.title"
      />

      <kpn-error />

      @if (apiResponse(); as response) {
        <div class="kpn-spacer-above">
          @if (loggedIn() === false) {
            <p i18n="@@subset-changes.login-required">
              This details of the changes history are available to logged in OpenStreetMap
              contributors only.
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
              [totalCount]="response.result.changeCount"
              [changeCount]="response.result.changes.length"
            >
              <kpn-items>
                @for (changeSet of response.result.changes; track changeSet.rowIndex) {
                  <kpn-item [index]="changeSet.rowIndex">
                    @if (changeSet.network) {
                      <kpn-change-network-analysis-summary [changeSet]="changeSet" />
                    }
                    @if (changeSet.location) {
                      <kpn-change-location-analysis-summary [changeSet]="changeSet" />
                    }
                  </kpn-item>
                }
              </kpn-items>
            </kpn-changes>
          }
        </div>
      }
      <kpn-subset-changes-sidebar sidebar />
    </kpn-page>
  `,
  standalone: true,
  imports: [
    AsyncPipe,
    ChangeLocationAnalysisSummaryComponent,
    ChangeNetworkAnalysisSummaryComponent,
    ChangesComponent,
    ErrorComponent,
    ItemComponent,
    ItemsComponent,
    PageComponent,
    SituationOnComponent,
    SubsetChangesSidebarComponent,
    SubsetPageHeaderBlockComponent,
    UserLinkLoginComponent,
  ],
})
export class SubsetChangesPageComponent implements OnInit {
  private readonly userStore = inject(UserStore);
  readonly loggedIn = this.userStore.loggedIn;

  private readonly store = inject(Store);
  readonly impact = this.store.selectSignal(selectSubsetChangesPageImpact);
  readonly pageSize = this.store.selectSignal(selectSubsetChangesPageSize);
  readonly pageIndex = this.store.selectSignal(selectSubsetChangesPageIndex);
  readonly apiResponse = this.store.selectSignal(selectSubsetChangesPage);

  ngOnInit(): void {
    this.store.dispatch(actionSubsetChangesPageInit());
  }

  onImpactChange(impact: boolean): void {
    this.store.dispatch(actionSubsetChangesPageImpact({ impact }));
  }

  onPageSizeChange(pageSize: number): void {
    this.store.dispatch(actionSubsetChangesPageSize({ pageSize }));
  }

  onPageIndexChange(pageIndex: number): void {
    this.store.dispatch(actionSubsetChangesPageIndex({ pageIndex }));
  }
}
