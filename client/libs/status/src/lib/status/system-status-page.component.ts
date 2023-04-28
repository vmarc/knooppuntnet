import { NgIf } from '@angular/common';
import { AsyncPipe } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { Params, RouterLink } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { PeriodParameters } from '@api/common/status';
import { SystemStatusPage } from '@api/common/status';
import { ApiService } from '@app/services';
import { Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';
import { tap } from 'rxjs/operators';
import { map } from 'rxjs/operators';
import { DataSizeChartComponent } from './charts/system/data-size-chart.component';
import { DiskSizeChartComponent } from './charts/system/disk-size-chart.component';
import { DiskSizeExternalChartComponent } from './charts/system/disk-size-external-chart.component';
import { DiskSpaceAvailableChartComponent } from './charts/system/disk-space-available-chart.component';
import { DiskSpaceOverpassChartComponent } from './charts/system/disk-space-overpass-chart.component';
import { DiskSpaceUsedChartComponent } from './charts/system/disk-space-used-chart.component';
import { DocsChartComponent } from './charts/system/docs-chart.component';
import { StatusLinks } from './status-links';
import { StatusPageMenuComponent } from './status-page-menu.component';

@Component({
  selector: 'kpn-system-status-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- English only-->
    <!-- eslint-disable @angular-eslint/template/i18n -->
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li><a routerLink="/status" i18n="@@breadcrumb.status">Status</a></li>
      <li i18n="@@breadcrumb.system">System</li>
    </ul>

    <h1>System</h1>

    <div *ngIf="page$ | async as page">
      <kpn-status-page-menu
        [links]="statusLinks"
        [periodType]="page.periodType"
      />

      <div>
        <a [routerLink]="'TODO previous'" class="previous">previous</a>
        <a [routerLink]="'TODO next'">next</a>
      </div>

      <div class="chart-group">
        <h2>Backend disk space</h2>
        <kpn-disk-space-used-chart
          [barChart]="page.backendDiskSpaceUsed"
          [xAxisLabel]="xAxisLabel"
        />
        <kpn-disk-space-available-chart
          [barChart]="page.backendDiskSpaceAvailable"
          [xAxisLabel]="xAxisLabel"
        />
        <kpn-disk-space-overpass-chart
          [barChart]="page.backendDiskSpaceOverpass"
          [xAxisLabel]="xAxisLabel"
        />
      </div>

      <div class="chart-group">
        <h2>Analysis database</h2>
        <kpn-docs-chart
          [barChart]="page.analysisDocCount"
          [xAxisLabel]="xAxisLabel"
        />
        <kpn-disk-size-chart
          [barChart]="page.analysisDiskSize"
          [xAxisLabel]="xAxisLabel"
        />
        <kpn-disk-size-external-chart
          [barChart]="page.analysisDiskSizeExternal"
          [xAxisLabel]="xAxisLabel"
        />
        <kpn-data-size-chart
          [barChart]="page.analysisDataSize"
          [xAxisLabel]="xAxisLabel"
        />
      </div>

      <div class="chart-group">
        <h2>Changes database</h2>
        <kpn-docs-chart
          [barChart]="page.changesDocCount"
          [xAxisLabel]="xAxisLabel"
        />
        <kpn-disk-size-chart
          [barChart]="page.changesDiskSize"
          [xAxisLabel]="xAxisLabel"
        />
        <kpn-disk-size-external-chart
          [barChart]="page.changesDiskSizeExternal"
          [xAxisLabel]="xAxisLabel"
        />
        <kpn-data-size-chart
          [barChart]="page.changesDataSize"
          [xAxisLabel]="xAxisLabel"
        />
      </div>
    </div>
  `,
  styles: [
    `
      .chart-group {
        padding-bottom: 40px;
        margin-bottom: 40px;
        border-bottom: 1px solid lightgray;
      }

      .previous:after {
        content: ' | ';
        padding-left: 5px;
        padding-right: 5px;
      }
    `,
  ],
  standalone: true,
  imports: [
    RouterLink,
    NgIf,
    StatusPageMenuComponent,
    DiskSpaceUsedChartComponent,
    DiskSpaceAvailableChartComponent,
    DiskSpaceOverpassChartComponent,
    DocsChartComponent,
    DiskSizeChartComponent,
    DiskSizeExternalChartComponent,
    DataSizeChartComponent,
    AsyncPipe,
  ],
})
export class SystemStatusPageComponent implements OnInit {
  page$: Observable<SystemStatusPage>;
  statusLinks: StatusLinks;
  xAxisLabel: string;

  constructor(
    private readonly activatedRoute: ActivatedRoute,
    private readonly apiService: ApiService
  ) {}

  ngOnInit(): void {
    this.page$ = this.activatedRoute.params.pipe(
      map((params) => this.toPeriodParameters(params)),
      tap((parameters) => {
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
      mergeMap((parameters) =>
        this.apiService.systemStatus(parameters).pipe(
          map((r) => r.result),
          tap(
            (page) =>
              (this.statusLinks = new StatusLinks(
                page.timestamp,
                '/status/system'
              ))
          )
        )
      )
    );
  }

  private toPeriodParameters(params: Params): PeriodParameters {
    const period = params['period'];
    if ('year' === period) {
      return {
        period: 'year',
        year: +params['year'],
        month: null,
        week: null,
        day: null,
        hour: null,
      };
    }
    if ('month' === period) {
      return {
        period: 'month',
        year: +params['year'],
        month: +params['monthOrWeek'],
        week: null,
        day: null,
        hour: null,
      };
    }
    if ('week' === period) {
      return {
        period: 'week',
        year: +params['year'],
        month: null,
        week: +params['monthOrWeek'],
        day: null,
        hour: null,
      };
    }
    if ('day' === period) {
      return {
        period: 'day',
        year: +params['year'],
        month: +params['month'],
        week: null,
        day: +params['day'],
        hour: null,
      };
    }
    if ('hour' === period) {
      return {
        period: 'hour',
        year: +params['year'],
        month: +params['month'],
        week: null,
        day: +params['day'],
        hour: +params['hour'],
      };
    }

    const now = new Date();
    return {
      period: 'hour',
      year: now.getFullYear(),
      month: now.getMonth(),
      week: null,
      day: now.getDate(),
      hour: now.getHours(),
    };
  }
}
