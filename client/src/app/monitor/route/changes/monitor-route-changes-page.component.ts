import { OnDestroy } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatSlideToggleChange } from '@angular/material/slide-toggle';
import { actionPreferencesImpact } from '@app/core';
import { selectPreferencesImpact } from '@app/core';
import { Store } from '@ngrx/store';
import { actionMonitorRouteChangesPageDestroy } from '../../store/monitor.actions';
import { actionMonitorRouteChangesPageIndex } from '../../store/monitor.actions';
import { actionMonitorRouteChangesPageInit } from '../../store/monitor.actions';
import { selectMonitorRouteChangesPage } from '../../store/monitor.selectors';

@Component({
  selector: 'kpn-monitor-route-changes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- work-in-progress -->
    <!-- eslint-disable @angular-eslint/template/i18n -->

    <kpn-monitor-route-page-header pageName="changes" />

    <div *ngIf="response$ | async as response" class="kpn-spacer-above">
      <div *ngIf="!response.result">Route not found</div>
      <div *ngIf="response.result" class="kpn-spacer-above">
        <mat-slide-toggle
          [checked]="impact$ | async"
          (change)="impactChanged($event)"
          >Impact
        </mat-slide-toggle>

        <kpn-paginator
          (pageIndexChange)="pageChanged($event)"
          [pageIndex]="response.result.pageIndex"
          [length]="response.result.totalChangeCount"
          [showPageSizeSelection]="true"
        />

        <kpn-monitor-changes
          [pageSize]="response.result.pageSize"
          [pageIndex]="response.result.pageIndex"
          [changes]="response.result.changes"
        />
      </div>
    </div>
  `,
})
export class MonitorRouteChangesPageComponent implements OnInit, OnDestroy {
  readonly response$ = this.store.select(selectMonitorRouteChangesPage);
  readonly impact$ = this.store.select(selectPreferencesImpact);

  constructor(private store: Store) {}

  ngOnInit(): void {
    this.store.dispatch(actionMonitorRouteChangesPageInit());
  }

  ngOnDestroy(): void {
    this.store.dispatch(actionMonitorRouteChangesPageDestroy());
  }

  impactChanged(event: MatSlideToggleChange) {
    this.store.dispatch(actionPreferencesImpact({ impact: event.checked }));
    this.store.dispatch(actionMonitorRouteChangesPageInit());
  }

  pageChanged(pageIndex: number) {
    window.scroll(0, 0);
    this.store.dispatch(actionMonitorRouteChangesPageIndex({ pageIndex }));
  }
}
