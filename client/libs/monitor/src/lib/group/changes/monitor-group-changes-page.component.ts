import { NgIf } from '@angular/common';
import { AsyncPipe } from '@angular/common';
import { OnDestroy } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatSlideToggleChange } from '@angular/material/slide-toggle';
import { RouterLink } from '@angular/router';
import { PaginatorComponent } from '@app/components/shared/paginator';
import { actionPreferencesImpact } from '@app/core';
import { selectPreferencesImpact } from '@app/core';
import { Store } from '@ngrx/store';
import { MonitorChangesComponent } from '../../components/monitor-changes.component';
import { actionMonitorGroupChangesPageDestroy } from '../../store/monitor.actions';
import { actionMonitorGroupChangesPageIndex } from '../../store/monitor.actions';
import { actionMonitorGroupChangesPageInit } from '../../store/monitor.actions';
import { selectMonitorGroupChangesPage } from '../../store/monitor.selectors';
import { selectMonitorGroupDescription } from '../../store/monitor.selectors';
import { selectMonitorGroupName } from '../../store/monitor.selectors';
import { MonitorGroupPageMenuComponent } from '../components/monitor-group-page-menu.component';

@Component({
  selector: 'kpn-monitor-group-changes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- work-in-progress -->
    <!-- eslint-disable @angular-eslint/template/i18n -->

    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li>
        <a routerLink="/monitor" i18n="@@breadcrumb.monitor">Monitor</a>
      </li>
      <li>Group changes</li>
    </ul>

    <h1>
      {{ groupDescription$ | async }}
    </h1>

    <kpn-monitor-group-page-menu
      pageName="changes"
      [groupName]="groupName$ | async"
    />

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
        />

        <kpn-monitor-changes
          [pageSize]="response.result.pageSize"
          [pageIndex]="response.result.pageIndex"
          [changes]="response.result.changes"
        />
      </div>
    </div>
  `,
  standalone: true,
  imports: [
    RouterLink,
    MonitorGroupPageMenuComponent,
    NgIf,
    MatSlideToggleModule,
    PaginatorComponent,
    MonitorChangesComponent,
    AsyncPipe,
  ],
})
export class MonitorGroupChangesPageComponent implements OnInit, OnDestroy {
  readonly groupName$ = this.store.select(selectMonitorGroupName);
  readonly groupDescription$ = this.store.select(selectMonitorGroupDescription);
  readonly impact$ = this.store.select(selectPreferencesImpact);
  readonly response$ = this.store.select(selectMonitorGroupChangesPage);

  constructor(private store: Store) {}

  ngOnInit(): void {
    this.store.dispatch(actionMonitorGroupChangesPageInit());
  }

  ngOnDestroy(): void {
    this.store.dispatch(actionMonitorGroupChangesPageDestroy());
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
