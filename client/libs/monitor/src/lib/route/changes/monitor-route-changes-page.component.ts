import { NgIf } from '@angular/common';
import { AsyncPipe } from '@angular/common';
import { OnDestroy } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatSlideToggleChange } from '@angular/material/slide-toggle';
import { PaginatorComponent } from '@app/components/shared/paginator';
import { actionPreferencesImpact } from '@app/core';
import { selectPreferencesImpact } from '@app/core';
import { Store } from '@ngrx/store';
import { MonitorChangesComponent } from '../../components/monitor-changes.component';
import { actionMonitorRouteChangesPageDestroy } from '../../store/monitor.actions';
import { actionMonitorRouteChangesPageIndex } from '../../store/monitor.actions';
import { actionMonitorRouteChangesPageInit } from '../../store/monitor.actions';
import { selectMonitorRouteChangesPage } from '../../store/monitor.selectors';
import { MonitorRoutePageHeaderComponent } from '../components/monitor-route-page-header.component';

@Component({
  selector: 'kpn-monitor-route-changes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- work-in-progress -->
    <!-- eslint-disable @angular-eslint/template/i18n -->

    <kpn-monitor-route-page-header
      pageName="changes"
      [groupName]="'service.groupName()'"
      [routeName]="'service.routeName()'"
      [routeDescription]="'service.routeDescription()'"
    />

    <div *ngIf="apiResponse() as response" class="kpn-spacer-above">
      <div *ngIf="!response.result">Route not found</div>
      <div *ngIf="response.result as page" class="kpn-spacer-above">
        <mat-slide-toggle [checked]="impact()" (change)="impactChanged($event)"
          >Impact
        </mat-slide-toggle>

        <kpn-paginator
          (pageIndexChange)="pageChanged($event)"
          [pageIndex]="page.pageIndex"
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
    MonitorRoutePageHeaderComponent,
    NgIf,
    MatSlideToggleModule,
    PaginatorComponent,
    MonitorChangesComponent,
    AsyncPipe,
  ],
})
export class MonitorRouteChangesPageComponent implements OnInit, OnDestroy {
  readonly apiResponse = this.store.selectSignal(selectMonitorRouteChangesPage);
  readonly impact = this.store.selectSignal(selectPreferencesImpact);

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
