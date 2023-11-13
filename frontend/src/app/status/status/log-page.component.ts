import { NgIf } from '@angular/common';
import { AsyncPipe } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { Params, RouterLink } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { LogPage } from '@api/common/status';
import { PeriodParameters } from '@api/common/status';
import { PageComponent } from '@app/components/shared/page';
import { ApiService } from '@app/services';
import { Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';
import { tap } from 'rxjs/operators';
import { map } from 'rxjs/operators';
import { LogAnalysisChartComponent } from './charts/log/log-analysis-chart.component';
import { LogAnalysisRobotChartComponent } from './charts/log/log-analysis-robot-chart.component';
import { LogApiChartComponent } from './charts/log/log-api-chart.component';
import { LogApiRobotChartComponent } from './charts/log/log-api-robot-chart.component';
import { LogNonRobotChartComponent } from './charts/log/log-non-robot-chart.component';
import { LogRobotChartComponent } from './charts/log/log-robot-chart.component';
import { LogTileChartComponent } from './charts/log/log-tile-chart.component';
import { LogTileRobotChartComponent } from './charts/log/log-tile-robot-chart.component';
import { StatusLinks } from './status-links';
import { StatusPageMenuComponent } from './status-page-menu.component';
import { StatusSidebarComponent } from './status-sidebar.component';

@Component({
  selector: 'kpn-log-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- English only-->
    <!-- eslint-disable @angular-eslint/template/i18n -->
    <kpn-page>
      <ul class="breadcrumb">
        <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
        <li><a routerLink="/status" i18n="@@breadcrumb.status">Status</a></li>
        <li>Log analysis</li>
      </ul>

      <h1>Log analysis</h1>

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
          <h2>Analysis</h2>
          <kpn-log-tile-chart
            [barChart]="page.tile"
            [xAxisLabel]="xAxisLabel"
          />
          <kpn-log-tile-robot-chart
            [barChart]="page.tileRobot"
            [xAxisLabel]="xAxisLabel"
          />
          <kpn-log-api-chart [barChart]="page.api" [xAxisLabel]="xAxisLabel" />
          <kpn-log-api-robot-chart
            [barChart]="page.apiRobot"
            [xAxisLabel]="xAxisLabel"
          />
          <kpn-log-analysis-chart
            [barChart]="page.analysis"
            [xAxisLabel]="xAxisLabel"
          />
          <kpn-log-analysis-robot-chart
            [barChart]="page.analysisRobot"
            [xAxisLabel]="xAxisLabel"
          />
          <kpn-log-robot-chart
            [barChart]="page.robot"
            [xAxisLabel]="xAxisLabel"
          />
          <kpn-log-non-robot-chart
            [barChart]="page.nonRobot"
            [xAxisLabel]="xAxisLabel"
          />
        </div>
      </div>
      <kpn-status-sidebar sidebar />
    </kpn-page>
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
    AsyncPipe,
    LogAnalysisChartComponent,
    LogAnalysisRobotChartComponent,
    LogApiChartComponent,
    LogApiRobotChartComponent,
    LogNonRobotChartComponent,
    LogRobotChartComponent,
    LogTileChartComponent,
    LogTileRobotChartComponent,
    NgIf,
    PageComponent,
    RouterLink,
    StatusPageMenuComponent,
    StatusSidebarComponent,
  ],
})
export class LogPageComponent implements OnInit {
  page$: Observable<LogPage>;
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
        this.apiService.logStatus(parameters).pipe(
          map((r) => r.result),
          tap(
            (page) =>
              (this.statusLinks = new StatusLinks(
                page.timestamp,
                '/status/log'
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
