import {OnInit} from '@angular/core';
import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';
import {MonitorGroupChangesPage} from '@api/common/monitor/monitor-group-changes-page';
import {ApiResponse} from '@api/custom/api-response';
import {select} from '@ngrx/store';
import {Store} from '@ngrx/store';
import {Observable} from 'rxjs';
import {filter} from 'rxjs/operators';
import {AppState} from '../../../core/core.state';
import {actionMonitorGroupChangesPageInit} from '../../store/monitor.actions';
import {selectMonitorGroupChangesPage} from '../../store/monitor.selectors';
import {selectMonitorGroupDescription} from '../../store/monitor.selectors';
import {selectMonitorGroupName} from '../../store/monitor.selectors';

@Component({
  selector: 'kpn-monitor-group-changes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `

    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li><a routerLink="/monitor">Monitor</a></li>
      <li>Group changes</li>
    </ul>

    <h1>
      {{groupDescription$ | async}}
    </h1>

    <kpn-monitor-group-page-menu pageName="changes" [groupName]="groupName$ | async"></kpn-monitor-group-page-menu>

    <div *ngIf="response$ | async as response">
      <p *ngIf="!response.result">
        No group changes
      </p>
      <div *ngIf="response.result">
        <kpn-monitor-changes [changes]="response.result.changes"></kpn-monitor-changes>
      </div>
    </div>
  `
})
export class MonitorGroupChangesPageComponent implements OnInit {

  readonly groupName$ = this.store.select(selectMonitorGroupName);
  readonly groupDescription$ = this.store.select(selectMonitorGroupDescription);

  readonly response$: Observable<ApiResponse<MonitorGroupChangesPage>> = this.store.pipe(
    select(selectMonitorGroupChangesPage),
    filter(r => r != null)
  );

  constructor(private store: Store<AppState>) {
  }

  ngOnInit(): void {
    this.store.dispatch(actionMonitorGroupChangesPageInit());
  }
}
