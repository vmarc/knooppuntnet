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
  selector: 'ui-replication-status-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- English only-->
    <!-- eslint-disable @angular-eslint/template/i18n -->
    <ui-page>
      <ui-breadcrumb [breadcrumbItems]="breadcrumbItems" />
      <h1>Replication</h1>

      @if (service.page(); as page) {
        <ui-status-page-menu [links]="service.statusLinks()" [periodType]="page.periodType" />
        <div>
          <a [routerLink]="'TODO previous'" class="previous">previous</a>
          <a [routerLink]="'TODO next'">next</a>
        </div>
        <div class="chart-group">
          <ui-delay-chart [barChart]="page.delay" [xAxisLabel]="service.xAxisLabel" />
          <ui-analysis-delay-chart
            [barChart]="page.analysisDelay"
            [xAxisLabel]="service.xAxisLabel"
          />
          <ui-update-delay-chart [barChart]="page.updateDelay" [xAxisLabel]="service.xAxisLabel" />
          <ui-replication-delay-chart
            [barChart]="page.replicationDelay"
            [xAxisLabel]="service.xAxisLabel"
          />
        </div>
        <div class="chart-group">
          <ui-replication-bytes-chart
            [barChart]="page.replicationBytes"
            [xAxisLabel]="service.xAxisLabel"
          />
          <ui-replication-elements-chart
            [barChart]="page.replicationElements"
            [xAxisLabel]="service.xAxisLabel"
          />
          <ui-replication-changesets-chart
            [barChart]="page.replicationChangeSets"
            [xAxisLabel]="service.xAxisLabel"
          />
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
  providers: [ReplicationStatusPageService, RouterService],
  imports: [
    AnalysisDelayChartComponent,
    BreadcrumbComponent,
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
  protected readonly service = inject(ReplicationStatusPageService);
  protected readonly breadcrumbItems: BreadcrumbItem[] = [
    Breadcrumbs.home,
    Breadcrumbs.status,
    { label: Breadcrumbs.replicationLabel },
  ];

  ngOnInit(): void {
    this.service.onInit();
  }
}
