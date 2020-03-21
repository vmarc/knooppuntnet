import {CommonModule} from "@angular/common";
import {NgModule} from "@angular/core";
import {NgxChartsModule} from "@swimlane/ngx-charts";
import {SharedModule} from "../components/shared/shared.module";
import {StatusRoutingModule} from "./status-routing.module";
import {ChartExampleComponent} from "./status/chart-example.component";
import {StatusPageComponent} from "./status/status-page.component";
import {StatusSidebarComponent} from "./status/status-sidebar.component";

@NgModule({
  declarations: [
    StatusPageComponent,
    StatusSidebarComponent,
    ChartExampleComponent
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
