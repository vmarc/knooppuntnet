import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { OldPageComponent } from '@app/components/shared/page';
import { RouterService } from '../../shared/services/router.service';
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
import { StatusSidebarComponent } from './status-sidebar.component';

@Component({
  selector: 'kpn-log-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- English only-->
    <!-- eslint-disable @angular-eslint/template/i18n -->
    <kpn-old-page>
      <ul class="breadcrumb">
        <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
        <li><a routerLink="/status" i18n="@@breadcrumb.status">Status</a></li>
        <li>Log analysis</li>
      </ul>

      <h1>Log analysis</h1>

      @if (service.page(); as page) {
        <kpn-status-page-menu [links]="service.statusLinks()" [periodType]="page.periodType" />
        <div>
          <a [routerLink]="'TODO previous'" class="previous">previous</a>
          <a [routerLink]="'TODO next'">next</a>
        </div>
        <div class="chart-group">
          <h2>Analysis</h2>
          <kpn-log-tile-chart [barChart]="page.tile" [xAxisLabel]="service.xAxisLabel" />
          <kpn-log-tile-robot-chart [barChart]="page.tileRobot" [xAxisLabel]="service.xAxisLabel" />
          <kpn-log-api-chart [barChart]="page.api" [xAxisLabel]="service.xAxisLabel" />
          <kpn-log-api-robot-chart [barChart]="page.apiRobot" [xAxisLabel]="service.xAxisLabel" />
          <kpn-log-analysis-chart [barChart]="page.analysis" [xAxisLabel]="service.xAxisLabel" />
          <kpn-log-analysis-robot-chart
            [barChart]="page.analysisRobot"
            [xAxisLabel]="service.xAxisLabel"
          />
          <kpn-log-robot-chart [barChart]="page.robot" [xAxisLabel]="service.xAxisLabel" />
          <kpn-log-non-robot-chart [barChart]="page.nonRobot" [xAxisLabel]="service.xAxisLabel" />
        </div>
      }
      <kpn-status-sidebar sidebar />
    </kpn-old-page>
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
    LogAnalysisChartComponent,
    LogAnalysisRobotChartComponent,
    LogApiChartComponent,
    LogApiRobotChartComponent,
    LogNonRobotChartComponent,
    LogRobotChartComponent,
    LogTileChartComponent,
    LogTileRobotChartComponent,
    OldPageComponent,
    RouterLink,
    StatusPageMenuComponent,
    StatusSidebarComponent,
  ],
})
export class LogPageComponent implements OnInit {
  readonly service = inject(LogPageService);

  ngOnInit(): void {
    this.service.onInit();
  }
}
