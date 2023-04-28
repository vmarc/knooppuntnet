import { NgIf } from '@angular/common';
import { AsyncPipe } from '@angular/common';
import { OnDestroy } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';
import { selectDefined } from '@app/core';
import { Store } from '@ngrx/store';
import { map } from 'rxjs/operators';
import { actionMonitorGroupDeleteDestroy } from '../../store/monitor.actions';
import { actionMonitorGroupDeleteInit } from '../../store/monitor.actions';
import { actionMonitorGroupDelete } from '../../store/monitor.actions';
import { selectMonitorGroupPage } from '../../store/monitor.selectors';
import { MonitorGroupBreadcrumbComponent } from '../components/monitor-group-breadcrumb.component';

@Component({
  selector: 'kpn-monitor-group-delete-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-monitor-group-breadcrumb></kpn-monitor-group-breadcrumb>

    <h1 i18n="@@monitor.group.delete.title">Monitor - delete group</h1>

    <div *ngIf="response$ | async as response" class="kpn-form">
      <div *ngIf="!response.result">
        <p i18n="@@monitor.group.delete.group-not-found">Group not found</p>
      </div>
      <div *ngIf="response.result" class="kpn-form">
        <p>
          <span class="kpn-label" i18n="@@monitor.group.delete.name">
            Name
          </span>
          {{ response.result.groupName }}
        </p>

        <p>
          <span class="kpn-label" i18n="@@monitor.group.delete.description">
            Description
          </span>
          {{ response.result.groupDescription }}
        </p>

        <div *ngIf="routeCount$ | async as routeCount">
          <div *ngIf="routeCount > 0" class="kpn-line">
            <mat-icon svgIcon="warning" />
            <span i18n="@@monitor.group.delete.warning">
              The information of all routes ({{ routeCount }} route(s)) in the
              group will also be deleted!
            </span>
          </div>
        </div>

        <div class="kpn-form-buttons">
          <button mat-stroked-button (click)="delete(response.result.groupId)">
            <span class="kpn-warning" i18n="@@monitor.group.delete.action">
              Delete group
            </span>
          </button>
          <a routerLink="/monitor" i18n="@@action.cancel">Cancel</a>
        </div>
      </div>
    </div>
  `,
  standalone: true,
  imports: [
    MonitorGroupBreadcrumbComponent,
    NgIf,
    MatIconModule,
    MatButtonModule,
    RouterLink,
    AsyncPipe,
  ],
})
export class MonitorGroupDeletePageComponent implements OnInit, OnDestroy {
  readonly response$ = selectDefined(this.store, selectMonitorGroupPage);
  readonly routeCount$ = this.response$.pipe(
    map((response) => response.result.routes.length)
  );

  constructor(private store: Store) {}

  ngOnInit(): void {
    this.store.dispatch(actionMonitorGroupDeleteInit());
  }

  ngOnDestroy(): void {
    this.store.dispatch(actionMonitorGroupDeleteDestroy());
  }

  delete(groupId: string): void {
    this.store.dispatch(actionMonitorGroupDelete({ groupId }));
  }
}
