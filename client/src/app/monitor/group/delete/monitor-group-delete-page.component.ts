import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { map } from 'rxjs/operators';
import { AppState } from '../../../core/core.state';
import { actionMonitorGroupDeleteInit } from '../../store/monitor.actions';
import { actionMonitorGroupDelete } from '../../store/monitor.actions';
import { selectMonitorGroupPage } from '../../store/monitor.selectors';

@Component({
  selector: 'kpn-monitor-group-delete-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-monitor-group-breadcrumb></kpn-monitor-group-breadcrumb>

    <h1>Monitor - delete group</h1>

    <div *ngIf="response$ | async as response" class="kpn-form">
      <div *ngIf="!response.result">
        <p>Group not found</p>
      </div>
      <div *ngIf="response.result" class="kpn-form">
        <p>Name: {{ response.result.groupName }}</p>

        <p>Description: {{ response.result.groupDescription }}</p>

        <div *ngIf="routeCount$ | async as routeCount">
          <div *ngIf="routeCount > 0" class="kpn-line">
            <mat-icon svgIcon="warning"></mat-icon>
            <span
              >The information of all routes ({{ routeCount }} route(s)) in the
              group will also be deleted!</span
            >
          </div>
        </div>

        <div class="kpn-form-buttons">
          <button mat-stroked-button (click)="delete(response.result.groupId)">
            <span class="warning">Delete group</span>
          </button>
          <a routerLink="/monitor">Cancel</a>
        </div>
      </div>
    </div>
  `,
})
export class MonitorGroupDeletePageComponent implements OnInit {
  readonly response$ = this.store.select(selectMonitorGroupPage);
  readonly routeCount$ = this.response$.pipe(
    map((response) => response.result.routes.length)
  );

  constructor(private store: Store<AppState>) {}

  ngOnInit(): void {
    this.store.dispatch(actionMonitorGroupDeleteInit());
  }

  delete(groupId: string): void {
    this.store.dispatch(actionMonitorGroupDelete({ groupId }));
  }
}
