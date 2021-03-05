import {ChangeDetectionStrategy} from '@angular/core';
import {OnInit} from '@angular/core';
import {Component} from '@angular/core';
import {Params} from '@angular/router';
import {ActivatedRoute} from '@angular/router';
import {PeriodParameters} from '@api/common/status/period-parameters';
import {SystemStatusPage} from '@api/common/status/system-status-page';
import {Observable} from 'rxjs';
import {mergeMap} from 'rxjs/operators';
import {tap} from 'rxjs/operators';
import {map} from 'rxjs/operators';
import {AppService} from '../../app.service';
import {StatusLinks} from './status-links';

/* tslint:disable:template-i18n English only */
@Component({
  selector: 'kpn-system-status-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li><a routerLink="/status" i18n="@@breadcrumb.status">Status</a></li>
      <li i18n="@@breadcrumb.system">System</li>
    </ul>

    <h1>System</h1>

    <div *ngIf="page$ | async as page">
      <kpn-status-page-menu [links]="statusLinks" [periodType]="page.periodType"></kpn-status-page-menu>

      <div>
        <a [routerLink]="'TODO previous'" class="previous">previous</a>
        <a [routerLink]="'TODO next'">next</a>
      </div>

      <div class="chart-group">
        <h2>Backend disk space</h2>
        <kpn-disk-space-used-chart [barChart]="page.backendDiskSpaceUsed" [xAxisLabel]="xAxisLabel"></kpn-disk-space-used-chart>
        <kpn-disk-space-available-chart [barChart]="page.backendDiskSpaceAvailable" [xAxisLabel]="xAxisLabel"></kpn-disk-space-available-chart>
        <kpn-disk-space-overpass-chart [barChart]="page.backendDiskSpaceOverpass" [xAxisLabel]="xAxisLabel"></kpn-disk-space-overpass-chart>
      </div>

      <div class="chart-group">
        <h2>Analysis database</h2>
        <kpn-docs-chart [barChart]="page.analysisDocCount" [xAxisLabel]="xAxisLabel"></kpn-docs-chart>
        <kpn-disk-size-chart [barChart]="page.analysisDiskSize" [xAxisLabel]="xAxisLabel"></kpn-disk-size-chart>
        <kpn-disk-size-external-chart [barChart]="page.analysisDiskSizeExternal" [xAxisLabel]="xAxisLabel"></kpn-disk-size-external-chart>
        <kpn-data-size-chart [barChart]="page.analysisDataSize" [xAxisLabel]="xAxisLabel"></kpn-data-size-chart>
      </div>

      <div class="chart-group">
        <h2>Changes database</h2>
        <kpn-docs-chart [barChart]="page.changesDocCount" [xAxisLabel]="xAxisLabel"></kpn-docs-chart>
        <kpn-disk-size-chart [barChart]="page.changesDiskSize" [xAxisLabel]="xAxisLabel"></kpn-disk-size-chart>
        <kpn-disk-size-external-chart [barChart]="page.changesDiskSizeExternal" [xAxisLabel]="xAxisLabel"></kpn-disk-size-external-chart>
        <kpn-data-size-chart [barChart]="page.changesDataSize" [xAxisLabel]="xAxisLabel"></kpn-data-size-chart>
      </div>

    </div>
  `,
  styles: [`
    .chart-group {
      padding-bottom: 40px;
      margin-bottom: 40px;
      border-bottom: 1px solid lightgray;
    }

    .previous:after {
      content: " | ";
      padding-left: 5px;
      padding-right: 5px;
    }

  `]
})
export class SystemStatusPageComponent implements OnInit {

  page$: Observable<SystemStatusPage>;
  statusLinks: StatusLinks;
  xAxisLabel: string;

  constructor(private readonly activatedRoute: ActivatedRoute,
              private readonly appService: AppService) {
  }

  ngOnInit(): void {
    this.page$ = this.activatedRoute.params.pipe(
      map(params => this.toPeriodParameters(params)),
      tap(parameters => {
        if (parameters.period === 'year') {
          this.xAxisLabel = 'weeks';
        } else if (parameters.period === 'month') {
          this.xAxisLabel = 'days';
        } else if (parameters.period === 'week') {
          this.xAxisLabel = 'days';
        } else if (parameters.period === 'day') {
          this.xAxisLabel = 'hours';
        } else if (parameters.period === 'hour') {
          this.xAxisLabel = 'minutes';
        }
      }),
      mergeMap(parameters => this.appService.systemStatus(parameters).pipe(
        map(r => r.result),
        tap(page => this.statusLinks = new StatusLinks(page.timestamp, '/status/system'))
      ))
    );
  }

  private toPeriodParameters(params: Params): PeriodParameters {

    const period = params['period'];
    if ('year' === period) {
      return new PeriodParameters('year', +params['year'], null, null, null, null);
    }
    if ('month' === period) {
      return new PeriodParameters('month', +params['year'], +params['monthOrWeek'], null, null, null);
    }
    if ('week' === period) {
      return new PeriodParameters('week', +params['year'], null, +params['monthOrWeek'], null, null);
    }
    if ('day' === period) {
      return new PeriodParameters('day', +params['year'], +params['month'], null, +params['day'], null);
    }
    if ('hour' === period) {
      return new PeriodParameters('hour', +params['year'], +params['month'], null, +params['day'], +params['hour']);
    }

    const now = new Date();
    return new PeriodParameters(
      'hour',
      now.getFullYear(),
      now.getMonth(),
      null,
      now.getDate(),
      now.getHours()
    );
  }

}
