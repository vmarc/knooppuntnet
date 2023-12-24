import { inject } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ChangeLocationAnalysisSummaryComponent } from '@app/analysis/components/change-set';
import { ChangeNetworkAnalysisSummaryComponent } from '@app/analysis/components/change-set';
import { ChangesComponent } from '@app/analysis/components/changes';
import { ErrorComponent } from '@app/components/shared/error';
import { ItemComponent } from '@app/components/shared/items';
import { ItemsComponent } from '@app/components/shared/items';
import { PageComponent } from '@app/components/shared/page';
import { PageHeaderComponent } from '@app/components/shared/page';
import { SituationOnComponent } from '@app/components/shared/timestamp';
import { Store } from '@ngrx/store';
import { UserLinkLoginComponent } from '../../../shared/user';
import { UserStore } from '../../../shared/user/user.store';
import { ChangesSidebarComponent } from '../sidebar/changes-sidebar.component';
import { actionChangesPageSize } from '../store/changes.actions';
import { actionChangesImpact } from '../store/changes.actions';
import { actionChangesPageIndex } from '../store/changes.actions';
import { actionChangesPageInit } from '../store/changes.actions';
import { selectChangesImpact } from '../store/changes.selectors';
import { selectChangesPageSize } from '../store/changes.selectors';
import { selectChangesPageIndex } from '../store/changes.selectors';
import { selectChangesPage } from '../store/changes.selectors';

@Component({
  selector: 'kpn-changes-page',
  template: `
    <kpn-page>
      <ul class="breadcrumb">
        <li><a [routerLink]="'/'" i18n="@@breadcrumb.home">Home</a></li>
        <li>
          <a [routerLink]="'/analysis'" i18n="@@breadcrumb.analysis">Analysis</a>
        </li>
        <li i18n="@@breadcrumb.changes">Changes</li>
      </ul>

      <kpn-page-header subject="changes-page" i18n="@@changes-page.title">
        Changes
      </kpn-page-header>

      <kpn-error />

      @if (apiResponse(); as response) {
        <div class="kpn-spacer-above">
          @if (!loggedIn()) {
            <p i18n="@@changes-page.login-required" class="kpn-spacer-above">
              The details of the changes history are available to registered OpenStreetMap
              contributors only, after
              <kpn-user-link-login />
              .
            </p>
          } @else {
            @if (response.result; as page) {
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
                [totalCount]="page.changeCount"
                [changeCount]="page.changes.length"
              >
                <kpn-items>
                  @for (changeSet of page.changes; track $index) {
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
          }
        </div>
      }
      <kpn-changes-sidebar sidebar />
    </kpn-page>
  `,
  standalone: true,
  imports: [
    ChangeLocationAnalysisSummaryComponent,
    ChangeNetworkAnalysisSummaryComponent,
    ChangesComponent,
    ChangesSidebarComponent,
    ErrorComponent,
    ItemComponent,
    ItemsComponent,
    PageComponent,
    PageHeaderComponent,
    RouterLink,
    SituationOnComponent,
    UserLinkLoginComponent,
  ],
})
export class ChangesPageComponent implements OnInit {
  private readonly userStore = inject(UserStore);
  readonly loggedIn = this.userStore.loggedIn;

  private readonly store = inject(Store);
  readonly apiResponse = this.store.selectSignal(selectChangesPage);
  readonly impact = this.store.selectSignal(selectChangesImpact);
  readonly pageSize = this.store.selectSignal(selectChangesPageSize);
  readonly pageIndex = this.store.selectSignal(selectChangesPageIndex);

  ngOnInit(): void {
    this.store.dispatch(actionChangesPageInit());
  }

  onImpactChange(impact: boolean): void {
    this.store.dispatch(actionChangesImpact({ impact }));
  }

  onPageSizeChange(pageSize: number): void {
    this.store.dispatch(actionChangesPageSize({ pageSize }));
  }

  onPageIndexChange(pageIndex: number): void {
    this.store.dispatch(actionChangesPageIndex({ pageIndex }));
  }
}
