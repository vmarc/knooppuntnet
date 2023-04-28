import { NgIf } from '@angular/common';
import { NgFor } from '@angular/common';
import { AsyncPipe } from '@angular/common';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ChangeLocationAnalysisSummaryComponent } from '@app/analysis/components/change-set';
import { ChangeNetworkAnalysisSummaryComponent } from '@app/analysis/components/change-set';
import { ChangesComponent } from '@app/analysis/components/changes';
import { ErrorComponent } from '@app/components/shared/error';
import { ItemComponent } from '@app/components/shared/items';
import { ItemsComponent } from '@app/components/shared/items';
import { LinkLoginComponent } from '@app/components/shared/link';
import { PageHeaderComponent } from '@app/components/shared/page';
import { SituationOnComponent } from '@app/components/shared/timestamp';
import { selectUserLoggedIn } from '@app/core';
import { Store } from '@ngrx/store';
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

    <div *ngIf="response$ | async as response" class="kpn-spacer-above">
      <p
        *ngIf="(loggedIn$ | async) === false; else loggedIn"
        i18n="@@changes-page.login-required"
        class="kpn-spacer-above"
      >
        The details of the changes history are available to registered
        OpenStreetMap contributors only, after
        <kpn-link-login />
        .
      </p>
      <ng-template #loggedIn>
        <div *ngIf="response.result as page">
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
            [totalCount]="page.changeCount"
            [changeCount]="page.changes.length"
          >
            <kpn-items>
              <kpn-item
                *ngFor="let changeSet of page.changes"
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
        </div>
      </ng-template>
    </div>
  `,
  standalone: true,
  imports: [
    RouterLink,
    PageHeaderComponent,
    ErrorComponent,
    NgIf,
    LinkLoginComponent,
    SituationOnComponent,
    ChangesComponent,
    ItemsComponent,
    NgFor,
    ItemComponent,
    ChangeNetworkAnalysisSummaryComponent,
    ChangeLocationAnalysisSummaryComponent,
    AsyncPipe,
  ],
})
export class ChangesPageComponent implements OnInit {
  readonly response$ = this.store.select(selectChangesPage);
  readonly impact$ = this.store.select(selectChangesImpact);
  readonly pageSize$ = this.store.select(selectChangesPageSize);
  readonly pageIndex$ = this.store.select(selectChangesPageIndex);
  readonly loggedIn$ = this.store.select(selectUserLoggedIn);

  constructor(private store: Store) {}

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
