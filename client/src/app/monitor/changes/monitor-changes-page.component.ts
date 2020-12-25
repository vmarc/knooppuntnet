import {OnInit} from '@angular/core';
import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';
import {MonitorChangesPage} from '@api/common/monitor/monitor-changes-page';
import {ApiResponse} from '@api/custom/api-response';
import {Store} from '@ngrx/store';
import {select} from '@ngrx/store';
import {Observable} from 'rxjs';
import {filter} from 'rxjs/operators';
import {AppState} from '../../core/core.state';
import {actionMonitorChangesPageInit} from '../store/monitor.actions';
import {selectMonitorChangesPage} from '../store/monitor.selectors';

@Component({
  selector: 'kpn-monitor-changes-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li><a routerLink="/monitor">Monitor</a></li>
      <li>Changes</li>
    </ul>

    <h1>
      Monitor
    </h1>

    <kpn-monitor-page-menu pageName="changes"></kpn-monitor-page-menu>
    <kpn-error></kpn-error>

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
export class MonitorChangesPageComponent implements OnInit {

  readonly response$: Observable<ApiResponse<MonitorChangesPage>> = this.store.pipe(
    select(selectMonitorChangesPage),
    filter(r => r != null)
  );

  constructor(private store: Store<AppState>) {
  }

  ngOnInit(): void {
    this.store.dispatch(actionMonitorChangesPageInit());
  }
}
