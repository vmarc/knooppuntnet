import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatRadioModule } from '@angular/material/radio';
import { MarkdownModule } from 'ngx-markdown';
import { SharedModule } from '../../components/shared/shared.module';
import { OverviewRoutingModule } from './overview-routing.module';
import { OverviewService } from './overview.service';
import { OverviewPageComponent } from './overview/_overview-page.component';
import { OverviewConfigurationService } from './overview/overview-configuration.service';
import { OverviewListStatRowComponent } from './overview/overview-list-stat-row.component';
import { OverviewListStatTableComponent } from './overview/overview-list-stat-table.component';
import { OverviewListStatComponent } from './overview/overview-list-stat.component';
import { OverviewListComponent } from './overview/overview-list.component';
import { OverviewSidebarComponent } from './overview/overview-sidebar.component';
import { OverviewTableHeaderComponent } from './overview/overview-table-header.component';
import { OverviewTableRowComponent } from './overview/overview-table-row.component';
import { OverviewTableComponent } from './overview/overview-table.component';
import { OverviewValueComponent } from './overview/overview-value.component';

@NgModule({
  imports: [
    CommonModule,
    MarkdownModule,
    MatIconModule,
    SharedModule,
    OverviewRoutingModule,
    MatListModule,
    MatRadioModule,
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
    OverviewSidebarComponent,
  ],
  providers: [OverviewService, OverviewConfigurationService],
})
export class OverviewModule {}
