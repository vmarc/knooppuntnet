import { AsyncPipe, NgFor, NgIf } from '@angular/common';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeLocationAnalysisSummaryComponent } from '@app/analysis/components/change-set';
import { ChangeNetworkAnalysisSummaryComponent } from '@app/analysis/components/change-set';
import { ChangesComponent } from '@app/analysis/components/changes';
import { ErrorComponent } from '@app/components/shared/error';
import { ItemComponent } from '@app/components/shared/items';
import { ItemsComponent } from '@app/components/shared/items';
import { LinkLoginComponent } from '@app/components/shared/link';
import { PageComponent } from '@app/components/shared/page';
import { SituationOnComponent } from '@app/components/shared/timestamp';
import { selectUserLoggedIn } from '@app/core';
import { Store } from '@ngrx/store';
import { SubsetPageHeaderBlockComponent } from '../components/subset-page-header-block.component';
import { actionSubsetChangesPageSize } from '../store/subset.actions';
import { actionSubsetChangesPageImpact } from '../store/subset.actions';
import { actionSubsetChangesPageIndex } from '../store/subset.actions';
import { actionSubsetChangesPageInit } from '../store/subset.actions';
import { selectSubsetChangesPageImpact } from '../store/subset.selectors';
import { selectSubsetChangesPageSize } from '../store/subset.selectors';
import { selectSubsetChangesPageIndex } from '../store/subset.selectors';
import { selectSubsetChangesPage } from '../store/subset.selectors';
import { SubsetChangesSidebarComponent } from './subset-changes-sidebar.component';

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

      <div *ngIf="apiResponse() as response" class="kpn-spacer-above">
        <p *ngIf="loggedIn() === false; else changes" i18n="@@subset-changes.login-required">
          This details of the changes history are available to registered OpenStreetMap contributors
          only, after
          <kpn-link-login />
          .
        </p>
        <ng-template #changes>
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
              <kpn-item
                *ngFor="let changeSet of response.result.changes"
                [index]="changeSet.rowIndex"
              >
                <kpn-change-network-analysis-summary
                  *ngIf="changeSet.network"
                  [changeSet]="changeSet"
                />
                <kpn-change-location-analysis-summary
                  *ngIf="changeSet.location"
                  [changeSet]="changeSet"
                />
              </kpn-item>
            </kpn-items>
          </kpn-changes>
        </ng-template>
      </div>
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
    LinkLoginComponent,
    NgFor,
    NgIf,
    PageComponent,
    SituationOnComponent,
    SubsetChangesSidebarComponent,
    SubsetPageHeaderBlockComponent,
  ],
})
export class SubsetChangesPageComponent implements OnInit {
  readonly impact = this.store.selectSignal(selectSubsetChangesPageImpact);
  readonly pageSize = this.store.selectSignal(selectSubsetChangesPageSize);
  readonly pageIndex = this.store.selectSignal(selectSubsetChangesPageIndex);
  readonly loggedIn = this.store.selectSignal(selectUserLoggedIn);
  readonly apiResponse = this.store.selectSignal(selectSubsetChangesPage);

  constructor(private store: Store) {}

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
