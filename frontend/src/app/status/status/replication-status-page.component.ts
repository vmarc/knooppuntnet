import { NgIf } from '@angular/common';
import { AsyncPipe } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { Params, RouterLink } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { PeriodParameters } from '@api/common/status';
import { ReplicationStatusPage } from '@api/common/status';
import { PageComponent } from '@app/components/shared/page';
import { ApiService } from '@app/services';
import { Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';
import { tap } from 'rxjs/operators';
import { map } from 'rxjs/operators';
import { AnalysisDelayChartComponent } from './charts/analysis-delay-chart.component';
import { DelayChartComponent } from './charts/delay-chart.component';
import { ReplicationBytesChartComponent } from './charts/replication-bytes-chart.component';
import { ReplicationChangesetsChartComponent } from './charts/replication-changesets-chart.component';
import { ReplicationDelayChartComponent } from './charts/replication-delay-chart.component';
import { ReplicationElementsChartComponent } from './charts/replication-elements-chart.component';
import { UpdateDelayChartComponent } from './charts/update-delay-chart.component';
import { StatusLinks } from './status-links';
import { StatusPageMenuComponent } from './status-page-menu.component';
import { StatusSidebarComponent } from './status-sidebar.component';

@Component({
  selector: 'kpn-replication-status-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- English only-->
    <!-- eslint-disable @angular-eslint/template/i18n -->
    <kpn-page>
      <ul class="breadcrumb">
        <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
        <li><a routerLink="/status" i18n="@@breadcrumb.status">Status</a></li>
        <li i18n="@@breadcrumb.replication">Replication</li>
      </ul>

      <h1>Replication</h1>

      <div *ngIf="page$ | async as page">
        <kpn-status-page-menu [links]="statusLinks" [periodType]="page.periodType" />

        <div>
          <a [routerLink]="'TODO previous'" class="previous">previous</a>
          <a [routerLink]="'TODO next'">next</a>
        </div>

        <div class="chart-group">
          <kpn-delay-chart [barChart]="page.delay" [xAxisLabel]="xAxisLabel" />
          <kpn-analysis-delay-chart [barChart]="page.analysisDelay" [xAxisLabel]="xAxisLabel" />
          <kpn-update-delay-chart [barChart]="page.updateDelay" [xAxisLabel]="xAxisLabel" />
          <kpn-replication-delay-chart
            [barChart]="page.replicationDelay"
            [xAxisLabel]="xAxisLabel"
          />
        </div>

        <div class="chart-group">
          <kpn-replication-bytes-chart
            [barChart]="page.replicationBytes"
            [xAxisLabel]="xAxisLabel"
          />
          <kpn-replication-elements-chart
            [barChart]="page.replicationElements"
            [xAxisLabel]="xAxisLabel"
          />
          <kpn-replication-changesets-chart
            [barChart]="page.replicationChangeSets"
            [xAxisLabel]="xAxisLabel"
          />
        </div>
      </div>
      <kpn-status-sidebar sidebar />
    </kpn-page>
  `,
  styles: `
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
  standalone: true,
  imports: [
    AnalysisDelayChartComponent,
    AsyncPipe,
    DelayChartComponent,
    NgIf,
    PageComponent,
    ReplicationBytesChartComponent,
    ReplicationChangesetsChartComponent,
    ReplicationDelayChartComponent,
    ReplicationElementsChartComponent,
    RouterLink,
    StatusPageMenuComponent,
    StatusSidebarComponent,
    UpdateDelayChartComponent,
  ],
})
export class ReplicationStatusPageComponent implements OnInit {
  page$: Observable<ReplicationStatusPage>;

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
        this.apiService.replicationStatus(parameters).pipe(
          map((r) => r.result),
          tap((page) => (this.statusLinks = new StatusLinks(page.timestamp, '/status/replication')))
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
