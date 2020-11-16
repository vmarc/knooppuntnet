import {CommonModule} from '@angular/common';
import {NgModule} from '@angular/core';
import {NgxChartsModule} from '@swimlane/ngx-charts';
import {SharedModule} from '../components/shared/shared.module';
import {StatusRoutingModule} from './status-routing.module';
import {ActionBarChartStackedComponent} from './status/charts/action-bar-chart-stacked.component';
import {ActionBarChartComponent} from './status/charts/action-bar-chart.component';
import {AnalysisDelayChartComponent} from './status/charts/analysis-delay-chart.component';
import {DelayChartComponent} from './status/charts/delay-chart.component';
import {ReplicationBytesChartComponent} from './status/charts/replication-bytes-chart.component';
import {ReplicationChangesetsChartComponent} from './status/charts/replication-changesets-chart.component';
import {ReplicationDelayChartComponent} from './status/charts/replication-delay-chart.component';
import {ReplicationElementsChartComponent} from './status/charts/replication-elements-chart.component';
import {ServerDiskUsageLegendComponent} from './status/charts/server-disk-usage-legend.component';
import {ServerDiskUsagePieChartComponent} from './status/charts/server-disk-usage-pie-chart.component';
import {ServerDiskUsageComponent} from './status/charts/server-disk-usage.component';
import {DataSizeChartComponent} from './status/charts/system/data-size-chart.component';
import {DiskSizeChartComponent} from './status/charts/system/disk-size-chart.component';
import {DiskSizeExternalChartComponent} from './status/charts/system/disk-size-external-chart.component';
import {DiskSpaceAvailableChartComponent} from './status/charts/system/disk-space-available-chart.component';
import {DiskSpaceOverpassChartComponent} from './status/charts/system/disk-space-overpass-chart.component';
import {DiskSpaceUsedChartComponent} from './status/charts/system/disk-space-used-chart.component';
import {DocsChartComponent} from './status/charts/system/docs-chart.component';
import {UpdateDelayChartComponent} from './status/charts/update-delay-chart.component';
import {ReplicationStatusPageComponent} from './status/replication-status-page.component';
import {StatusLinksComponent} from './status/status-links.component';
import {StatusPageMenuComponent} from './status/status-page-menu.component';
import {StatusPageComponent} from './status/status-page.component';
import {StatusSidebarComponent} from './status/status-sidebar.component';
import {SystemStatusPageComponent} from './status/system-status-page.component';

@NgModule({
  declarations: [
    StatusPageComponent,
    StatusSidebarComponent,
    ActionBarChartComponent,
    ReplicationDelayChartComponent,
    ReplicationBytesChartComponent,
    ReplicationElementsChartComponent,
    ReplicationChangesetsChartComponent,
    UpdateDelayChartComponent,
    AnalysisDelayChartComponent,
    ActionBarChartStackedComponent,
    DelayChartComponent,
    ReplicationStatusPageComponent,
    SystemStatusPageComponent,
    DiskSizeChartComponent,
    DocsChartComponent,
    DiskSizeExternalChartComponent,
    DataSizeChartComponent,
    DiskSpaceUsedChartComponent,
    DiskSpaceAvailableChartComponent,
    DiskSpaceOverpassChartComponent,
    StatusLinksComponent,
    StatusPageMenuComponent,
    ServerDiskUsagePieChartComponent,
    ServerDiskUsageComponent,
    ServerDiskUsageLegendComponent
  ],
  imports: [
    CommonModule,
    StatusRoutingModule,
    SharedModule,
    NgxChartsModule
  ]
})
export class StatusModule {
}
