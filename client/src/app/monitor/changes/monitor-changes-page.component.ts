import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { MatSlideToggleChange } from '@angular/material/slide-toggle';
import { MonitorChangesPage } from '@api/common/monitor/monitor-changes-page';
import { ApiResponse } from '@api/custom/api-response';
import { Store } from '@ngrx/store';
import { select } from '@ngrx/store';
import { Observable } from 'rxjs';
import { filter } from 'rxjs/operators';
import { AppState } from '../../core/core.state';
import { actionPreferencesImpact } from '../../core/preferences/preferences.actions';
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
      <li><a routerLink="/monitor">Monitor</a></li>
      <li>Changes</li>
    </ul>

    <h1>Monitor</h1>

    <kpn-monitor-page-menu pageName="changes"></kpn-monitor-page-menu>
    <kpn-error></kpn-error>

    <div *ngIf="response$ | async as response">
      <p *ngIf="!response.result">No group changes</p>
      <div *ngIf="response.result" class="kpn-spacer-above">
        <mat-slide-toggle
          [checked]="impact$ | async"
          (change)="impactChanged($event)"
          >Impact</mat-slide-toggle
        >

        <kpn-paginator
          (page)="pageChanged($event)"
          [pageIndex]="response.result.pageIndex"
          [length]="response.result.totalChangeCount"
          [showPageSizeSelection]="true"
        >
        </kpn-paginator>

        <kpn-monitor-changes
          [pageIndex]="response.result.pageIndex"
          [itemsPerPage]="response.result.itemsPerPage"
          [changes]="response.result.changes"
        >
        </kpn-monitor-changes>
      </div>
    </div>
  `,
})
export class MonitorChangesPageComponent implements OnInit {
  readonly impact$ = this.store.select(selectPreferencesImpact);

  readonly response$: Observable<ApiResponse<MonitorChangesPage>> =
    this.store.pipe(
      select(selectMonitorChangesPage),
      filter((r) => r != null)
    );

  constructor(private store: Store<AppState>) {}

  ngOnInit(): void {
    this.store.dispatch(actionMonitorChangesPageInit());
  }

  impactChanged(event: MatSlideToggleChange) {
    this.store.dispatch(actionPreferencesImpact({ impact: event.checked }));
    this.store.dispatch(actionMonitorChangesPageInit());
  }

  pageChanged(event: PageEvent) {
    window.scroll(0, 0);
    this.store.dispatch(
      actionMonitorChangesPageIndex({ pageIndex: event.pageIndex })
    );
  }
}
