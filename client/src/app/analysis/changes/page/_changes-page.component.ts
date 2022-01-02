import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { PageService } from '../../../components/shared/page.service';
import { AppState } from '../../../core/core.state';
import { UserService } from '../../../services/user.service';
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

    <div
      *ngIf="!isLoggedIn()"
      i18n="@@changes-page.login-required"
      class="kpn-spacer-above"
    >
      The details of the changes history are available to registered
      OpenStreetMap contributors only, after
      <kpn-link-login></kpn-link-login>
      .
    </div>

    <kpn-error></kpn-error>

    <div *ngIf="response$ | async as response" class="kpn-spacer-above">
      <div *ngIf="response.result as page">
        <p>
          <kpn-situation-on
            [timestamp]="response.situationOn"
          ></kpn-situation-on>
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
              ></kpn-change-network-analysis-summary>
              <kpn-change-location-analysis-summary
                *ngIf="changeSet.location"
                [changeSet]="changeSet"
              ></kpn-change-location-analysis-summary>
            </kpn-item>
          </kpn-items>
        </kpn-changes>
      </div>
    </div>
  `,
})
export class ChangesPageComponent implements OnInit {
  readonly response$ = this.store.select(selectChangesPage);
  readonly impact$ = this.store.select(selectChangesImpact);
  readonly pageSize$ = this.store.select(selectChangesPageSize);
  readonly pageIndex$ = this.store.select(selectChangesPageIndex);

  constructor(
    private pageService: PageService,
    private userService: UserService,
    private store: Store<AppState>
  ) {}

  ngOnInit(): void {
    this.pageService.defaultMenu();
    if (this.isLoggedIn()) {
      this.store.dispatch(actionChangesPageInit());
    }
  }

  isLoggedIn(): boolean {
    return this.userService.isLoggedIn();
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
