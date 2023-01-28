import { Component, OnInit } from '@angular/core';
import { selectUserLoggedIn } from '@app/core/user/user.selectors';
import { Store } from '@ngrx/store';
import { actionSubsetChangesPageSize } from '../store/subset.actions';
import { actionSubsetChangesPageImpact } from '../store/subset.actions';
import { actionSubsetChangesPageIndex } from '../store/subset.actions';
import { actionSubsetChangesPageInit } from '../store/subset.actions';
import { selectSubsetChangesPageImpact } from '../store/subset.selectors';
import { selectSubsetChangesPageSize } from '../store/subset.selectors';
import { selectSubsetChangesPageIndex } from '../store/subset.selectors';
import { selectSubsetChangesPage } from '../store/subset.selectors';

@Component({
  selector: 'kpn-subset-changes-page',
  template: `
    <kpn-subset-page-header-block
      pageName="changes"
      pageTitle="Changes"
      i18n-pageTitle="@@subset-changes.title"
    />

    <kpn-error />

    <div *ngIf="response$ | async as response" class="kpn-spacer-above">
      <p
        *ngIf="(loggedIn$ | async) === false; else loggedIn"
        i18n="@@subset-changes.login-required"
      >
        This details of the changes history are available to registered
        OpenStreetMap contributors only, after
        <kpn-link-login />
        .
      </p>
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
  `,
})
export class SubsetChangesPageComponent implements OnInit {
  readonly impact$ = this.store.select(selectSubsetChangesPageImpact);
  readonly pageSize$ = this.store.select(selectSubsetChangesPageSize);
  readonly pageIndex$ = this.store.select(selectSubsetChangesPageIndex);
  readonly loggedIn$ = this.store.select(selectUserLoggedIn);
  readonly response$ = this.store.select(selectSubsetChangesPage);

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
