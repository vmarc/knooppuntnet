import { OnDestroy } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { map } from 'rxjs/operators';
import { selectDefined } from '../../../core/core.state';
import { AppState } from '../../../core/core.state';
import { actionMonitorGroupDeleteDestroy } from '../../store/monitor.actions';
import { actionMonitorGroupDeleteInit } from '../../store/monitor.actions';
import { actionMonitorGroupDelete } from '../../store/monitor.actions';
import { selectMonitorGroupPage } from '../../store/monitor.selectors';

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
            <mat-icon svgIcon="warning"/>
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
