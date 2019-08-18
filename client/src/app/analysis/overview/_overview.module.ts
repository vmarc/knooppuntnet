import {CommonModule} from "@angular/common";
import {NgModule} from "@angular/core";
import {MatIconModule} from "@angular/material";
import {MarkdownModule} from "ngx-markdown";
import {SharedModule} from "../../components/shared/shared.module";
import {OverviewPageComponent} from "./_overview-page.component";
import {OverviewRoutingModule} from "./_overview-routing.module";
import {OverviewListStatRowComponent} from "./overview-list-stat-row.component";
import {OverviewListStatTableComponent} from "./overview-list-stat-table.component";
import {OverviewListStatComponent} from "./overview-list-stat.component";
import {OverviewListComponent} from "./overview-list.component";
import {OverviewValueComponent} from "./overview-value.component";
import {OverviewTableHeaderComponent} from "./overview-table-header.component";
import {OverviewTableRowComponent} from "./overview-table-row.component";
import {OverviewTableComponent} from "./overview-table.component";
import {OverviewService} from "./overview.service";
import {StatisticConfigurationComponent} from "./statistic-configuration.component";
import {StatisticConfigurationsComponent} from "./statistic-configurations.component";

@NgModule({
  imports: [
    CommonModule,
    MarkdownModule,
    MatIconModule,
    SharedModule,
    OverviewRoutingModule
  ],
  declarations: [
    OverviewListComponent,
    OverviewListStatComponent,
    OverviewListStatRowComponent,
    OverviewListStatTableComponent,
    OverviewPageComponent,
    OverviewValueComponent,
    OverviewTableComponent,
    OverviewTableHeaderComponent,
    OverviewTableRowComponent,
    StatisticConfigurationComponent,
    StatisticConfigurationsComponent
  ],
  providers: [
    OverviewService
  ]
})
export class OverviewModule {
}
