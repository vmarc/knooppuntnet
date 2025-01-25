import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { PageComponent } from '../../shared/components/shared/page/page.component';
import { RouterService } from '../../shared/services/router.service';
import { AnalysisDelayChartComponent } from './charts/analysis-delay-chart.component';
import { DelayChartComponent } from './charts/delay-chart.component';
import { ReplicationBytesChartComponent } from './charts/replication-bytes-chart.component';
import { ReplicationChangesetsChartComponent } from './charts/replication-changesets-chart.component';
import { ReplicationDelayChartComponent } from './charts/replication-delay-chart.component';
import { ReplicationElementsChartComponent } from './charts/replication-elements-chart.component';
import { UpdateDelayChartComponent } from './charts/update-delay-chart.component';
import { ReplicationStatusPageService } from './replication-status-page.service';
import { StatusPageMenuComponent } from './status-page-menu.component';

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

      @if (service.page(); as page) {
        <kpn-status-page-menu [links]="service.statusLinks()" [periodType]="page.periodType" />
        <div>
          <a [routerLink]="'TODO previous'" class="previous">previous</a>
          <a [routerLink]="'TODO next'">next</a>
        </div>
        <div class="chart-group">
          <kpn-delay-chart [barChart]="page.delay" [xAxisLabel]="service.xAxisLabel" />
          <kpn-analysis-delay-chart
            [barChart]="page.analysisDelay"
            [xAxisLabel]="service.xAxisLabel"
          />
          <kpn-update-delay-chart [barChart]="page.updateDelay" [xAxisLabel]="service.xAxisLabel" />
          <kpn-replication-delay-chart
            [barChart]="page.replicationDelay"
            [xAxisLabel]="service.xAxisLabel"
          />
        </div>
        <div class="chart-group">
          <kpn-replication-bytes-chart
            [barChart]="page.replicationBytes"
            [xAxisLabel]="service.xAxisLabel"
          />
          <kpn-replication-elements-chart
            [barChart]="page.replicationElements"
            [xAxisLabel]="service.xAxisLabel"
          />
          <kpn-replication-changesets-chart
            [barChart]="page.replicationChangeSets"
            [xAxisLabel]="service.xAxisLabel"
          />
        </div>
      }
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
  providers: [ReplicationStatusPageService, RouterService],
  imports: [
    AnalysisDelayChartComponent,
    DelayChartComponent,
    PageComponent,
    ReplicationBytesChartComponent,
    ReplicationChangesetsChartComponent,
    ReplicationDelayChartComponent,
    ReplicationElementsChartComponent,
    RouterLink,
    StatusPageMenuComponent,
    UpdateDelayChartComponent,
  ],
})
export class ReplicationStatusPageComponent implements OnInit {
  readonly service = inject(ReplicationStatusPageService);

  ngOnInit(): void {
    this.service.onInit();
  }
}
