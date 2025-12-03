import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { BreadcrumbItem } from '@app/shared/components/breadcrumb/breadcrumb-item';
import { BreadcrumbComponent } from '@app/shared/components/breadcrumb/breadcrumb.component';
import { Breadcrumbs } from '@app/shared/components/breadcrumb/breadcrumbs';
import { PageComponent } from '@app/shared/components/page/page.component';
import { RouterService } from '@app/shared/services/router.service';
import { LogAnalysisChartComponent } from './charts/log/log-analysis-chart.component';
import { LogAnalysisRobotChartComponent } from './charts/log/log-analysis-robot-chart.component';
import { LogApiChartComponent } from './charts/log/log-api-chart.component';
import { LogApiRobotChartComponent } from './charts/log/log-api-robot-chart.component';
import { LogNonRobotChartComponent } from './charts/log/log-non-robot-chart.component';
import { LogRobotChartComponent } from './charts/log/log-robot-chart.component';
import { LogTileChartComponent } from './charts/log/log-tile-chart.component';
import { LogTileRobotChartComponent } from './charts/log/log-tile-robot-chart.component';
import { LogPageService } from './log-page.service';
import { StatusPageMenuComponent } from './status-page-menu.component';

@Component({
  selector: 'ui-log-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- English only-->
    <!-- eslint-disable @angular-eslint/template/i18n -->
    <ui-page>
      <ui-breadcrumb [breadcrumbItems]="breadcrumbItems" />
      <h1>Log analysis</h1>

      @if (service.page(); as page) {
        <ui-status-page-menu [links]="service.statusLinks()" [periodType]="page.periodType" />
        <div>
          <a routerLink="TODO previous" class="previous">previous</a>
          <a routerLink="TODO next">next</a>
        </div>
        <div class="chart-group">
          <h2>Analysis</h2>
          <ui-log-tile-chart [barChart]="page.tile" [xAxisLabel]="service.xAxisLabel" />
          <ui-log-tile-robot-chart [barChart]="page.tileRobot" [xAxisLabel]="service.xAxisLabel" />
          <ui-log-api-chart [barChart]="page.api" [xAxisLabel]="service.xAxisLabel" />
          <ui-log-api-robot-chart [barChart]="page.apiRobot" [xAxisLabel]="service.xAxisLabel" />
          <ui-log-analysis-chart [barChart]="page.analysis" [xAxisLabel]="service.xAxisLabel" />
          <ui-log-analysis-robot-chart
            [barChart]="page.analysisRobot"
            [xAxisLabel]="service.xAxisLabel"
          />
          <ui-log-robot-chart [barChart]="page.robot" [xAxisLabel]="service.xAxisLabel" />
          <ui-log-non-robot-chart [barChart]="page.nonRobot" [xAxisLabel]="service.xAxisLabel" />
        </div>
      }
    </ui-page>
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
  providers: [LogPageService, RouterService],
  imports: [
    BreadcrumbComponent,
    LogAnalysisChartComponent,
    LogAnalysisRobotChartComponent,
    LogApiChartComponent,
    LogApiRobotChartComponent,
    LogNonRobotChartComponent,
    LogRobotChartComponent,
    LogTileChartComponent,
    LogTileRobotChartComponent,
    PageComponent,
    RouterLink,
    StatusPageMenuComponent,
  ],
})
export class LogPageComponent implements OnInit {
  protected readonly service = inject(LogPageService);
  protected readonly breadcrumbItems: BreadcrumbItem[] = [
    Breadcrumbs.home,
    Breadcrumbs.status,
    { label: Breadcrumbs.logAnalysisLabel },
  ];

  ngOnInit(): void {
    this.service.onInit();
  }
}
