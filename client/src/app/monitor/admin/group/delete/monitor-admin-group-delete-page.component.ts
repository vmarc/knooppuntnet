import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';
import {Store} from '@ngrx/store';
import {AppState} from '../../../../core/core.state';
import {actionMonitorDeleteRouteGroup} from '../../../../core/monitor/monitor.actions';
import {selectMonitorAdminRouteGroupPage} from '../../../../core/monitor/monitor.selectors';

@Component({
  selector: 'kpn-monitor-admin-group-delete-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li><a routerLink="/monitor">Monitor</a></li>
      <li>Group</li>
    </ul>

    <h1>
      Monitor
    </h1>

    <kpn-page-menu>
      <span>
        Delete group
      </span>
    </kpn-page-menu>

    <div *ngIf="response$ | async as response">
      <div *ngIf="!response.result">
        <p>
          Group not found
        </p>
      </div>
      <div *ngIf="response.result">

        <p>
          Name: {{response.result.groupName}}
        </p>

        <p>
          Description: {{response.result.groupDescription}}
        </p>

        <div class="kpn-button-group">
          <button mat-stroked-button (click)="delete(response.result.groupName)">
            <span class="delete">Delete group</span>
          </button>
          <a routerLink="/monitor">Cancel</a>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .delete {
      color: red;
    }
  `]
})
export class MonitorAdminGroupDeletePageComponent {

  readonly response$ = this.store.select(selectMonitorAdminRouteGroupPage);

  constructor(private store: Store<AppState>) {
  }

  delete(groupName: string): void {
    this.store.dispatch(actionMonitorDeleteRouteGroup({groupName: groupName}));
  }
}
