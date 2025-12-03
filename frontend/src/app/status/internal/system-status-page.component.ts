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
import { DataSizeChartComponent } from './charts/system/data-size-chart.component';
import { DiskSizeChartComponent } from './charts/system/disk-size-chart.component';
import { DiskSizeExternalChartComponent } from './charts/system/disk-size-external-chart.component';
import { DiskSpaceAvailableChartComponent } from './charts/system/disk-space-available-chart.component';
import { DiskSpaceOverpassChartComponent } from './charts/system/disk-space-overpass-chart.component';
import { DiskSpaceUsedChartComponent } from './charts/system/disk-space-used-chart.component';
import { DocsChartComponent } from './charts/system/docs-chart.component';
import { StatusPageMenuComponent } from './status-page-menu.component';
import { SystemStatusPageService } from './system-status-page.service';

@Component({
  selector: 'ui-system-status-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- English only-->
    <!-- eslint-disable @angular-eslint/template/i18n -->
    <ui-page>
      <ui-breadcrumb [breadcrumbItems]="breadcrumbItems" />
      <h1>System</h1>

      @if (service.page(); as page) {
        <ui-status-page-menu [links]="service.statusLinks()" [periodType]="page.periodType" />
        <div>
          <a routerLink="TODO previous" class="previous">previous</a>
          <a routerLink="TODO next">next</a>
        </div>
        <div class="chart-group">
          <h2>Backend disk space</h2>
          <ui-disk-space-used-chart
            [barChart]="page.backendDiskSpaceUsed"
            [xAxisLabel]="service.xAxisLabel"
          />
          <ui-disk-space-available-chart
            [barChart]="page.backendDiskSpaceAvailable"
            [xAxisLabel]="service.xAxisLabel"
          />
          <ui-disk-space-overpass-chart
            [barChart]="page.backendDiskSpaceOverpass"
            [xAxisLabel]="service.xAxisLabel"
          />
        </div>
        <div class="chart-group">
          <h2>Analysis database</h2>
          <ui-docs-chart [barChart]="page.analysisDocCount" [xAxisLabel]="service.xAxisLabel" />
          <ui-disk-size-chart
            [barChart]="page.analysisDiskSize"
            [xAxisLabel]="service.xAxisLabel"
          />
          <ui-disk-size-external-chart
            [barChart]="page.analysisDiskSizeExternal"
            [xAxisLabel]="service.xAxisLabel"
          />
          <ui-data-size-chart
            [barChart]="page.analysisDataSize"
            [xAxisLabel]="service.xAxisLabel"
          />
        </div>
        <div class="chart-group">
          <h2>Changes database</h2>
          <ui-docs-chart [barChart]="page.changesDocCount" [xAxisLabel]="service.xAxisLabel" />
          <ui-disk-size-chart [barChart]="page.changesDiskSize" [xAxisLabel]="service.xAxisLabel" />
          <ui-disk-size-external-chart
            [barChart]="page.changesDiskSizeExternal"
            [xAxisLabel]="service.xAxisLabel"
          />
          <ui-data-size-chart [barChart]="page.changesDataSize" [xAxisLabel]="service.xAxisLabel" />
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
  providers: [SystemStatusPageComponent, RouterService],
  imports: [
    BreadcrumbComponent,
    DataSizeChartComponent,
    DiskSizeChartComponent,
    DiskSizeExternalChartComponent,
    DiskSpaceAvailableChartComponent,
    DiskSpaceOverpassChartComponent,
    DiskSpaceUsedChartComponent,
    DocsChartComponent,
    PageComponent,
    RouterLink,
    StatusPageMenuComponent,
  ],
})
export class SystemStatusPageComponent implements OnInit {
  protected readonly service = inject(SystemStatusPageService);
  protected readonly breadcrumbItems: BreadcrumbItem[] = [
    Breadcrumbs.home,
    Breadcrumbs.status,
    { label: Breadcrumbs.systemLabel },
  ];

  ngOnInit(): void {
    this.service.onInit();
  }
}
