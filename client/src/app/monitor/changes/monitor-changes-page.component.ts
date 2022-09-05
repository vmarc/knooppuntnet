import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatSlideToggleChange } from '@angular/material/slide-toggle';
import { Store } from '@ngrx/store';
import { AppState } from '../../core/core.state';
import { actionPreferencesPageSize } from '../../core/preferences/preferences.actions';
import { actionPreferencesImpact } from '../../core/preferences/preferences.actions';
import { selectPreferencesPageSize } from '../../core/preferences/preferences.selectors';
import { selectPreferencesImpact } from '../../core/preferences/preferences.selectors';
import { actionMonitorChangesPageIndex } from '../store/monitor.actions';
import { actionMonitorChangesPageInit } from '../store/monitor.actions';
import { selectMonitorChangesPage } from '../store/monitor.selectors';

@Component({
  selector: 'kpn-monitor-changes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li>
        <a routerLink="/monitor" i18n="@@breadcrumb.monitor">Monitor</a>
      </li>
      <li i18n="@@breadcrumb.monitor.changes">Changes</li>
    </ul>

    <h1 i18n="@@monitor.changes.title">Monitor</h1>

    <kpn-monitor-page-menu pageName="changes"></kpn-monitor-page-menu>
    <kpn-error></kpn-error>

    <div *ngIf="response$ | async as response">
      <p *ngIf="!response.result" i18n="@@monitor.changes.no-changes">
        No group changes
      </p>
      <div *ngIf="response.result" class="kpn-spacer-above">
        <mat-slide-toggle
          [checked]="impact$ | async"
          (change)="impactChanged($event)"
          i18n="@@monitor.changes.impact"
          >Impact
        </mat-slide-toggle>

        <kpn-paginator
          [pageSize]="pageSize$ | async"
          (pageSizeChange)="onPageSizeChange($event)"
          [pageIndex]="response.result.pageIndex"
          (pageIndexChange)="onPageIndexChange($event)"
          [length]="response.result.totalChangeCount"
          [showPageSizeSelection]="true"
        >
        </kpn-paginator>

        <kpn-monitor-changes
          [pageSize]="response.result.pageSize"
          [pageIndex]="response.result.pageIndex"
          [changes]="response.result.changes"
        >
        </kpn-monitor-changes>
      </div>
    </div>
  `,
})
export class MonitorChangesPageComponent implements OnInit {
  readonly impact$ = this.store.select(selectPreferencesImpact);
  readonly pageSize$ = this.store.select(selectPreferencesPageSize);
  readonly response$ = this.store.select(selectMonitorChangesPage);

  constructor(private store: Store<AppState>) {}

  ngOnInit(): void {
    this.store.dispatch(actionMonitorChangesPageInit());
  }

  impactChanged(event: MatSlideToggleChange) {
    this.store.dispatch(actionPreferencesImpact({ impact: event.checked }));
    this.store.dispatch(actionMonitorChangesPageInit());
  }

  onPageSizeChange(pageSize: number) {
    this.store.dispatch(actionPreferencesPageSize({ pageSize }));
  }

  onPageIndexChange(pageIndex: number) {
    window.scroll(0, 0);
    this.store.dispatch(actionMonitorChangesPageIndex({ pageIndex }));
  }
}
