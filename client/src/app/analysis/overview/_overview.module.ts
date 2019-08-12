import {CommonModule} from "@angular/common";
import {NgModule} from "@angular/core";
import {MatIconModule} from "@angular/material";
import {MarkdownModule} from "ngx-markdown";
import {OverviewPageComponent} from "./_overview-page.component";
import {OverviewTableCellComponent} from "./overview-table-cell.component";
import {OverviewTableHeaderComponent} from "./overview-table-header.component";
import {OverviewTableRowComponent} from "./overview-table-row.component";
import {OverviewTableComponent} from "./overview-table.component";
import {OverviewService} from "./overview.service";
import {StatisticConfigurationComponent} from "./statistic-configuration.component";
import {StatisticConfigurationsComponent} from "./statistic-configurations.component";
import {SharedModule} from "../../components/shared/shared.module";
import {OverviewRoutingModule} from "./_overview-routing.module";

@NgModule({
  imports: [
    CommonModule,
    MarkdownModule,
    MatIconModule,
    SharedModule,
    OverviewRoutingModule
  ],
  declarations: [
    OverviewPageComponent,
    OverviewTableComponent,
    OverviewTableHeaderComponent,
    OverviewTableRowComponent,
    OverviewTableCellComponent,
    StatisticConfigurationsComponent,
    StatisticConfigurationComponent,
  ],
  providers: [
    OverviewService,
  ]
})
export class OverviewModule {
}
