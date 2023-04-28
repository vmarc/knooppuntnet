import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatNativeDateModule } from '@angular/material/core';
import { DateAdapter } from '@angular/material/core';
import { MAT_DATE_LOCALE } from '@angular/material/core';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatDialogModule } from '@angular/material/dialog';
import { MatDividerModule } from '@angular/material/divider';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatListModule } from '@angular/material/list';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterModule } from '@angular/router';
import { SpinnerModule } from '@app/spinner';
import { MarkdownModule } from 'ngx-markdown';
import { CountryNameComponent } from './country-name.component';
import { DataComponent } from './data';
import { DayComponent } from './day';
import { KpnDateAdapter } from './day';
import { DialogComponent } from './dialog';
import { WarningDialogComponent } from './dialog';
import { ErrorComponent } from './error';
import { DayInputComponent } from './format';
import { DayPipe } from './format';
import { DistancePipe } from './format';
import { IntegerFormatPipe } from './format';
import { TimestampDayPipe } from './format';
import { TimestampPipe } from './format';
import { IconButtonComponent } from './icon';
import { IconButtonsComponent } from './icon';
import { IconHappyComponent } from './icon';
import { IconInvestigateComponent } from './icon';
import { IndicatorDialogComponent } from './indicator';
import { IndicatorIconComponent } from './indicator';
import { IndicatorComponent } from './indicator';
import { IntegrityIndicatorDialogComponent } from './indicator';
import { IntegrityIndicatorComponent } from './indicator';
import { ItemComponent } from './items';
import { ItemsComponent } from './items';
import { JsonComponent } from './json';
import { BracketsComponent } from './link';
import { DocLinkComponent } from './link';
import { IconLinkComponent } from './link';
import { IconNetworkLinkComponent } from './link';
import { IconRouteLinkComponent } from './link';
import { JosmLinkComponent } from './link';
import { JosmNodeComponent } from './link';
import { JosmRelationComponent } from './link';
import { JosmWayComponent } from './link';
import { LinkChangesetComponent } from './link';
import { LinkFactComponent } from './link';
import { LinkLoginComponent } from './link';
import { LinkLogoutComponent } from './link';
import { LinkNetworkDetailsComponent } from './link';
import { LinkNodeRefHeaderComponent } from './link';
import { LinkNodeRefComponent } from './link';
import { LinkNodeComponent } from './link';
import { LinkRouteRefHeaderComponent } from './link';
import { LinkRouteRefComponent } from './link';
import { LinkRouteComponent } from './link';
import { NodeListComponent } from './link';
import { OsmLinkChangeSetComponent } from './link';
import { OsmLinkNodeComponent } from './link';
import { OsmLinkRelationComponent } from './link';
import { OsmLinkUserAothClientsComponent } from './link';
import { OsmLinkUserComponent } from './link';
import { OsmLinkWayComponent } from './link';
import { OsmLinkComponent } from './link';
import { OsmWebsiteComponent } from './link';
import { TimeoutComponent } from './link';
import { PageMenuOptionComponent } from './menu';
import { PageMenuComponent } from './menu';
import { MetaDataComponent } from './meta-data.component';
import { NetworkScopeNameComponent } from './network-scope-name.component';
import { NetworkTypeIconComponent } from './network-type-icon.component';
import { NetworkTypeNameComponent } from './network-type-name.component';
import { NetworkTypeComponent } from './network-type.component';
import { PageExperimentalComponent } from './page';
import { PageFooterComponent } from './page';
import { PageHeaderComponent } from './page';
import { PaginatorComponent } from './paginator';
import { AnalysisSidebarComponent } from './sidebar';
import { SidebarBackComponent } from './sidebar';
import { SidebarFooterComponent } from './sidebar';
import { SidebarComponent } from './sidebar';
import { TagsTableComponent } from './tags';
import { TagsTextComponent } from './tags';
import { SituationOnComponent } from './timestamp';
import { TimestampComponent } from './timestamp';
import { ToolbarComponent } from './toolbar';

@NgModule({
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatIconModule,
    MatToolbarModule,
    MatListModule,
    MatDividerModule,
    MatButtonModule,
    MatDialogModule,
    RouterModule,
    SpinnerModule,
    MatPaginatorModule,
    MarkdownModule,
    MatFormFieldModule,
    MatInputModule,
    MatDatepickerModule,
    MatNativeDateModule,
  ],
  declarations: [
    LinkChangesetComponent,
    LinkNetworkDetailsComponent,
    LinkNodeComponent,
    LinkRouteComponent,
    LinkFactComponent,
    LinkLoginComponent,
    LinkLogoutComponent,
    JosmLinkComponent,
    JosmNodeComponent,
    JosmWayComponent,
    JosmRelationComponent,
    OsmLinkComponent,
    OsmLinkNodeComponent,
    OsmLinkWayComponent,
    OsmLinkChangeSetComponent,
    OsmLinkRelationComponent,
    OsmLinkUserComponent,
    OsmLinkUserAothClientsComponent,
    OsmWebsiteComponent,
    IconLinkComponent,
    IconNetworkLinkComponent,
    IconRouteLinkComponent,
    NetworkTypeComponent,
    NetworkTypeIconComponent,
    NetworkTypeNameComponent,
    NetworkScopeNameComponent,
    MetaDataComponent,
    DayComponent,
    TimestampComponent,
    CountryNameComponent,
    SituationOnComponent,
    JsonComponent,
    TagsTableComponent,
    TagsTextComponent,
    DataComponent,
    PageHeaderComponent,
    PageFooterComponent,
    PageExperimentalComponent,
    ToolbarComponent,
    SidebarComponent,
    SidebarBackComponent,
    SidebarFooterComponent,
    ItemsComponent,
    ItemComponent,
    IndicatorIconComponent,
    IndicatorComponent,
    IndicatorDialogComponent,
    IconButtonComponent,
    IconButtonsComponent,
    PageMenuComponent,
    PageMenuOptionComponent,
    DocLinkComponent,
    AnalysisSidebarComponent,
    IconHappyComponent,
    IconInvestigateComponent,
    LinkNodeRefComponent,
    LinkNodeRefHeaderComponent,
    LinkRouteRefComponent,
    LinkRouteRefHeaderComponent,
    NodeListComponent,
    PaginatorComponent,
    IntegerFormatPipe,
    BracketsComponent,
    DayPipe,
    DistancePipe,
    TimestampPipe,
    TimeoutComponent,
    TimestampDayPipe,
    DialogComponent,
    WarningDialogComponent,
    ErrorComponent,
    IntegrityIndicatorComponent,
    IntegrityIndicatorDialogComponent,
    DayInputComponent,
  ],
  exports: [
    LinkChangesetComponent,
    LinkNetworkDetailsComponent,
    LinkNodeComponent,
    LinkRouteComponent,
    LinkFactComponent,
    LinkLoginComponent,
    LinkLogoutComponent,
    JosmNodeComponent,
    JosmWayComponent,
    JosmRelationComponent,
    JosmLinkComponent,
    OsmLinkComponent,
    OsmLinkNodeComponent,
    OsmLinkWayComponent,
    OsmLinkChangeSetComponent,
    OsmLinkRelationComponent,
    OsmLinkUserComponent,
    OsmLinkUserAothClientsComponent,
    OsmWebsiteComponent,
    IconLinkComponent,
    IconNetworkLinkComponent,
    IconRouteLinkComponent,
    NetworkTypeComponent,
    NetworkTypeIconComponent,
    NetworkTypeNameComponent,
    NetworkScopeNameComponent,
    MetaDataComponent,
    DayComponent,
    TimestampComponent,
    CountryNameComponent,
    SituationOnComponent,
    JsonComponent,
    TagsTableComponent,
    DataComponent,
    PageHeaderComponent,
    PageFooterComponent,
    PageFooterComponent,
    PageExperimentalComponent,
    ToolbarComponent,
    SidebarComponent,
    SidebarBackComponent,
    SidebarFooterComponent,
    ItemsComponent,
    ItemComponent,
    IndicatorComponent,
    IndicatorIconComponent,
    IndicatorDialogComponent,
    IconButtonComponent,
    IconButtonsComponent,
    PageMenuComponent,
    PageMenuOptionComponent,
    DocLinkComponent,
    TagsTextComponent,
    AnalysisSidebarComponent,
    IconHappyComponent,
    IconInvestigateComponent,
    LinkNodeRefComponent,
    LinkNodeRefHeaderComponent,
    LinkRouteRefComponent,
    LinkRouteRefHeaderComponent,
    NodeListComponent,
    PaginatorComponent,
    IntegerFormatPipe,
    BracketsComponent,
    DayPipe,
    DistancePipe,
    DialogComponent,
    TimestampPipe,
    TimestampDayPipe,
    WarningDialogComponent,
    ErrorComponent,
    IntegrityIndicatorComponent,
    DayInputComponent,
  ],
  providers: [
    {
      provide: DateAdapter,
      useClass: KpnDateAdapter,
      deps: [MAT_DATE_LOCALE],
    },
  ],
})
export class SharedModule {}
