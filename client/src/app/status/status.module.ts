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
import {UpdateDelayComponent} from "./status/charts/update-delay.component";
import {StatusPageComponent} from "./status/status-page.component";
import {StatusSidebarComponent} from "./status/status-sidebar.component";

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
    DelayComponent
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
