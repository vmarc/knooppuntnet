import { ChangeDetectionStrategy } from '@angular/core';
import { OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { Params } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { LogPage } from '@api/common/status/log-page';
import { PeriodParameters } from '@api/common/status/period-parameters';
import { Observable } from 'rxjs';
import { flatMap } from 'rxjs/operators';
import { tap } from 'rxjs/operators';
import { map } from 'rxjs/operators';
import { AppService } from '../../app.service';
import { StatusLinks } from './status-links';

/* tslint:disable:template-i18n English only */
@Component({
  selector: 'kpn-log-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li><a routerLink="/status" i18n="@@breadcrumb.status">Status</a></li>
      <li i18n="@@breadcrumb.system">Log analysis</li>
    </ul>

    <h1>Log analysis</h1>

    <div *ngIf="page$ | async as page">
      <kpn-status-page-menu
        [links]="statusLinks"
        [periodType]="page.periodType"
      ></kpn-status-page-menu>

      <div>
        <a [routerLink]="'TODO previous'" class="previous">previous</a>
        <a [routerLink]="'TODO next'">next</a>
      </div>

      <div class="chart-group">
        <h2>Analysis</h2>
        <kpn-log-tile-chart
          [barChart]="page.tile"
          [xAxisLabel]="xAxisLabel"
        ></kpn-log-tile-chart>
        <kpn-log-tile-robot-chart
          [barChart]="page.tileRobot"
          [xAxisLabel]="xAxisLabel"
        ></kpn-log-tile-robot-chart>
        <kpn-log-api-chart
          [barChart]="page.api"
          [xAxisLabel]="xAxisLabel"
        ></kpn-log-api-chart>
        <kpn-log-api-robot-chart
          [barChart]="page.apiRobot"
          [xAxisLabel]="xAxisLabel"
        ></kpn-log-api-robot-chart>
        <kpn-log-analysis-chart
          [barChart]="page.analysis"
          [xAxisLabel]="xAxisLabel"
        ></kpn-log-analysis-chart>
        <kpn-log-analysis-robot-chart
          [barChart]="page.analysisRobot"
          [xAxisLabel]="xAxisLabel"
        ></kpn-log-analysis-robot-chart>
        <kpn-log-robot-chart
          [barChart]="page.robot"
          [xAxisLabel]="xAxisLabel"
        ></kpn-log-robot-chart>
        <kpn-log-non-robot-chart
          [barChart]="page.nonRobot"
          [xAxisLabel]="xAxisLabel"
        ></kpn-log-non-robot-chart>
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
})
export class LogPageComponent implements OnInit {
  page$: Observable<LogPage>;
  statusLinks: StatusLinks;
  xAxisLabel: string;

  constructor(
    private readonly activatedRoute: ActivatedRoute,
    private readonly appService: AppService
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
      flatMap((parameters) =>
        this.appService.logStatus(parameters).pipe(
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
      return new PeriodParameters(
        'year',
        +params['year'],
        null,
        null,
        null,
        null
      );
    }
    if ('month' === period) {
      return new PeriodParameters(
        'month',
        +params['year'],
        +params['monthOrWeek'],
        null,
        null,
        null
      );
    }
    if ('week' === period) {
      return new PeriodParameters(
        'week',
        +params['year'],
        null,
        +params['monthOrWeek'],
        null,
        null
      );
    }
    if ('day' === period) {
      return new PeriodParameters(
        'day',
        +params['year'],
        +params['month'],
        null,
        +params['day'],
        null
      );
    }
    if ('hour' === period) {
      return new PeriodParameters(
        'hour',
        +params['year'],
        +params['month'],
        null,
        +params['day'],
        +params['hour']
      );
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
