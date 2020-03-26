import {CommonModule} from "@angular/common";
import {NgModule} from "@angular/core";
import {NgxChartsModule} from "@swimlane/ngx-charts";
import {SharedModule} from "../components/shared/shared.module";
import {StatusRoutingModule} from "./status-routing.module";
import {ActionBarChartStackedComponent} from "./status/charts/action-bar-chart-stacked.component";
import {ActionBarChartComponent} from "./status/charts/action-bar-chart.component";
import {AnalysisDelayComponent} from "./status/charts/analysis-delay.component";
import {ChartExampleComponent} from "./status/charts/chart-example.component";
import {DelayComponent} from "./status/charts/delay.component";
import {ReplicationBytesComponent} from "./status/charts/replication-bytes.component";
import {ReplicationChangesetsComponent} from "./status/charts/replication-changesets.component";
import {ReplicationDelayComponent} from "./status/charts/replication-delay.component";
import {ReplicationElementsComponent} from "./status/charts/replication-elements.component";
import {DataSizeChartComponent} from "./status/charts/system/data-size-chart.component";
import {DiskSizeChartComponent} from "./status/charts/system/disk-size-chart.component";
import {DiskSizeExternalChartComponent} from "./status/charts/system/disk-size-external-chart.component";
import {DiskSpaceAvailableChartComponent} from "./status/charts/system/disk-space-available-chart.component";
import {DiskSpaceOverpassChartComponent} from "./status/charts/system/disk-space-overpass-chart.component";
import {DiskSpaceUsedChartComponent} from "./status/charts/system/disk-space-used-chart.component";
import {DocsChartComponent} from "./status/charts/system/docs-chart.component";
import {UpdateDelayComponent} from "./status/charts/update-delay.component";
import {ReplicationStatusPageComponent} from "./status/replication-status-page.component";
import {StatusPageComponent} from "./status/status-page.component";
import {StatusSidebarComponent} from "./status/status-sidebar.component";
import {SystemStatusPageComponent} from "./status/system-status-page.component";

@NgModule({
  declarations: [
    StatusPageComponent,
    StatusSidebarComponent,
    ChartExampleComponent,
    ActionBarChartComponent,
    ReplicationDelayComponent,
    ReplicationBytesComponent,
    ReplicationElementsComponent,
    ReplicationChangesetsComponent,
    UpdateDelayComponent,
    AnalysisDelayComponent,
    ActionBarChartStackedComponent,
    DelayComponent,
    ReplicationStatusPageComponent,
    SystemStatusPageComponent,
    DiskSizeChartComponent,
    DocsChartComponent,
    DiskSizeExternalChartComponent,
    DataSizeChartComponent,
    DiskSpaceUsedChartComponent,
    DiskSpaceAvailableChartComponent,
    DiskSpaceOverpassChartComponent
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
