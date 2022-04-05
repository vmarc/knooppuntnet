import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { AppState } from '../../../../core/core.state';
import { actionMonitorGroupDeleteInit } from '../../../store/monitor.actions';
import { actionMonitorGroupDelete } from '../../../store/monitor.actions';
import { selectMonitorAdminGroupPage } from '../../../store/monitor.selectors';

@Component({
  selector: 'kpn-monitor-admin-group-delete-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li><a routerLink="/monitor">Monitor</a></li>
      <li>Group</li>
    </ul>

    <h1>Monitor</h1>

    <h2>Delete group</h2>

    <div *ngIf="response$ | async as response" class="kpn-form">
      <div *ngIf="!response.result">
        <p>Group not found</p>
      </div>
      <div *ngIf="response.result" class="kpn-form">
        <p>Name: {{ response.result.groupName }}</p>

        <p>Description: {{ response.result.groupDescription }}</p>

        <div class="kpn-form-buttons">
          <button
            mat-stroked-button
            (click)="delete(response.result.groupName)"
          >
            <span class="delete">Delete group</span>
          </button>
          <a routerLink="/monitor">Cancel</a>
        </div>
      </div>
    </div>
  `,
  styles: [
    `
      .delete {
        color: red;
      }
    `,
  ],
})
export class MonitorAdminGroupDeletePageComponent implements OnInit {
  readonly response$ = this.store.select(selectMonitorAdminGroupPage);

  constructor(private store: Store<AppState>) {}

  ngOnInit(): void {
    this.store.dispatch(actionMonitorGroupDeleteInit());
  }

  delete(groupName: string): void {
    this.store.dispatch(actionMonitorGroupDelete({ groupName }));
  }
}
