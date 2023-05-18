import { NgIf } from '@angular/common';
import { OnDestroy } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatSlideToggleChange } from '@angular/material/slide-toggle';
import { RouterLink } from '@angular/router';
import { ErrorComponent } from '@app/components/shared/error';
import { PaginatorComponent } from '@app/components/shared/paginator';
import { actionPreferencesPageSize } from '@app/core';
import { actionPreferencesImpact } from '@app/core';
import { selectPreferencesPageSize } from '@app/core';
import { selectPreferencesImpact } from '@app/core';
import { Store } from '@ngrx/store';
import { MonitorChangesComponent } from '../components/monitor-changes.component';
import { MonitorPageMenuComponent } from '../components/monitor-page-menu.component';
import { actionMonitorChangesPageDestroy } from '../store/monitor.actions';
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

    <kpn-monitor-page-menu pageName="changes" />
    <kpn-error />

    <div *ngIf="apiResponse() as response">
      <p *ngIf="!response.result" i18n="@@monitor.changes.no-changes">
        No group changes
      </p>
      <div *ngIf="response.result as page" class="kpn-spacer-above">
        <mat-slide-toggle
          [checked]="impact()"
          (change)="impactChanged($event)"
          i18n="@@monitor.changes.impact"
          >Impact
        </mat-slide-toggle>

        <kpn-paginator
          [pageSize]="pageSize()"
          (pageSizeChange)="onPageSizeChange($event)"
          [pageIndex]="page.pageIndex"
          (pageIndexChange)="onPageIndexChange($event)"
          [length]="page.totalChangeCount"
          [showPageSizeSelection]="true"
        />

        <kpn-monitor-changes
          [pageSize]="page.pageSize"
          [pageIndex]="page.pageIndex"
          [changes]="page.changes"
        />
      </div>
    </div>
  `,
  standalone: true,
  imports: [
    ErrorComponent,
    MatSlideToggleModule,
    MatSlideToggleModule,
    MonitorChangesComponent,
    MonitorPageMenuComponent,
    NgIf,
    PaginatorComponent,
    RouterLink,
  ],
})
export class MonitorChangesPageComponent implements OnInit, OnDestroy {
  readonly impact = this.store.selectSignal(selectPreferencesImpact);
  readonly pageSize = this.store.selectSignal(selectPreferencesPageSize);
  readonly apiResponse = this.store.selectSignal(selectMonitorChangesPage);

  constructor(private store: Store) {}

  ngOnInit(): void {
    this.store.dispatch(actionMonitorChangesPageInit());
  }

  ngOnDestroy(): void {
    this.store.dispatch(actionMonitorChangesPageDestroy());
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
