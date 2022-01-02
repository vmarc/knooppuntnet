import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatSlideToggleChange } from '@angular/material/slide-toggle';
import { MonitorGroupChangesPage } from '@api/common/monitor/monitor-group-changes-page';
import { ApiResponse } from '@api/custom/api-response';
import { select } from '@ngrx/store';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { filter } from 'rxjs/operators';
import { AppState } from '../../../core/core.state';
import { actionPreferencesImpact } from '../../../core/preferences/preferences.actions';
import { selectPreferencesImpact } from '../../../core/preferences/preferences.selectors';
import { actionMonitorGroupChangesPageIndex } from '../../store/monitor.actions';
import { actionMonitorGroupChangesPageInit } from '../../store/monitor.actions';
import { selectMonitorGroupChangesPage } from '../../store/monitor.selectors';
import { selectMonitorGroupDescription } from '../../store/monitor.selectors';
import { selectMonitorGroupName } from '../../store/monitor.selectors';

@Component({
  selector: 'kpn-monitor-group-changes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a [routerLink]="'/'" i18n="@@breadcrumb.home">Home</a></li>
      <li><a [routerLink]="'/monitor'">Monitor</a></li>
      <li>Group changes</li>
    </ul>

    <h1>
      {{ groupDescription$ | async }}
    </h1>

    <kpn-monitor-group-page-menu
      pageName="changes"
      [groupName]="groupName$ | async"
    ></kpn-monitor-group-page-menu>

    <div *ngIf="response$ | async as response">
      <p *ngIf="!response.result">No group changes</p>
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
export class MonitorGroupChangesPageComponent implements OnInit {
  readonly groupName$ = this.store.select(selectMonitorGroupName);
  readonly groupDescription$ = this.store.select(selectMonitorGroupDescription);
  readonly impact$ = this.store.select(selectPreferencesImpact);

  readonly response$: Observable<ApiResponse<MonitorGroupChangesPage>> =
    this.store.pipe(
      select(selectMonitorGroupChangesPage),
      filter((r) => r != null)
    );

  constructor(private store: Store<AppState>) {}

  ngOnInit(): void {
    this.store.dispatch(actionMonitorGroupChangesPageInit());
  }

  impactChanged(event: MatSlideToggleChange) {
    this.store.dispatch(actionPreferencesImpact({ impact: event.checked }));
    this.store.dispatch(actionMonitorGroupChangesPageInit());
  }

  pageChanged(pageIndex: number) {
    window.scroll(0, 0);
    this.store.dispatch(actionMonitorGroupChangesPageIndex({ pageIndex }));
  }
}
