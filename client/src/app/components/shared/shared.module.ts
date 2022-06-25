import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule } from '@angular/material/dialog';
import { MatDividerModule } from '@angular/material/divider';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterModule } from '@angular/router';
import { MarkdownModule } from 'ngx-markdown';
import { SpinnerModule } from '../../spinner/spinner.module';
import { CountryNameComponent } from './country-name.component';
import { DataComponent } from './data/data.component';
import { DayComponent } from './day/day.component';
import { DialogComponent } from './dialog/dialog.component';
import { WarningDialogComponent } from './dialog/warning-dialog.component';
import { ErrorComponent } from './error/error.component';
import { DayPipe } from './format/day.pipe';
import { DistancePipe } from './format/distance.pipe';
import { IntegerFormatPipe } from './format/integer-format.pipe';
import { TimestampDayPipe } from './format/timestamp-day.pipe';
import { TimestampPipe } from './format/timestamp-pipe';
import { IconButtonComponent } from './icon/icon-button.component';
import { IconButtonsComponent } from './icon/icon-buttons.component';
import { IconHappyComponent } from './icon/icon-happy.component';
import { IconInvestigateComponent } from './icon/icon-investigate.component';
import { IndicatorDialogComponent } from './indicator/indicator-dialog.component';
import { IndicatorIconComponent } from './indicator/indicator-icon.component';
import { IndicatorComponent } from './indicator/indicator.component';
import { IntegrityIndicatorDialogComponent } from './indicator/integrity-indicator-dialog.component';
import { IntegrityIndicatorComponent } from './indicator/integrity-indicator.component';
import { ItemComponent } from './items/item.component';
import { ItemsComponent } from './items/items.component';
import { JsonComponent } from './json/json.component';
import { BracketsComponent } from './link/brackets.component';
import { DocLinkComponent } from './link/doc-link.component';
import { IconLinkComponent } from './link/icon-link.component';
import { IconNetworkLinkComponent } from './link/icon-network-link.component';
import { IconRouteLinkComponent } from './link/icon-route-link.component';
import { JosmLinkComponent } from './link/josm-link.component';
import { JosmNodeComponent } from './link/josm-node.component';
import { JosmRelationComponent } from './link/josm-relation.component';
import { JosmWayComponent } from './link/josm-way.component';
import { LinkChangesetComponent } from './link/link-changeset.component';
import { LinkFactComponent } from './link/link-fact.component';
import { LinkLoginComponent } from './link/link-login.component';
import { LinkLogoutComponent } from './link/link-logout.component';
import { LinkNetworkDetailsComponent } from './link/link-network-details.component';
import { LinkNodeRefHeaderComponent } from './link/link-node-ref-header';
import { LinkNodeRefComponent } from './link/link-node-ref.component';
import { LinkNodeComponent } from './link/link-node.component';
import { LinkRouteRefHeaderComponent } from './link/link-route-ref-header';
import { LinkRouteRefComponent } from './link/link-route-ref.component';
import { LinkRouteComponent } from './link/link-route.component';
import { NodeListComponent } from './link/node-list.component';
import { OsmLinkChangeSetComponent } from './link/osm-link-change-set.component';
import { OsmLinkNodeComponent } from './link/osm-link-node.component';
import { OsmLinkRelationComponent } from './link/osm-link-relation.component';
import { OsmLinkUserAothClientsComponent } from './link/osm-link-user-aoth-clients.component';
import { OsmLinkUserComponent } from './link/osm-link-user.component';
import { OsmLinkWayComponent } from './link/osm-link-way.component';
import { OsmLinkComponent } from './link/osm-link.component';
import { OsmWebsiteComponent } from './link/osm-website.component';
import { TimeoutComponent } from './link/timeout.component';
import { PageMenuOptionComponent } from './menu/page-menu-option.component';
import { PageMenuComponent } from './menu/page-menu.component';
import { MetaDataComponent } from './meta-data.component';
import { NetworkScopeNameComponent } from './network-scope-name.component';
import { NetworkTypeIconComponent } from './network-type-icon.component';
import { NetworkTypeNameComponent } from './network-type-name.component';
import { NetworkTypeComponent } from './network-type.component';
import { PageExperimentalComponent } from './page/page-experimental.component';
import { PageFooterComponent } from './page/page-footer.component';
import { PageHeaderComponent } from './page/page-header.component';
import { PaginatorComponent } from './paginator/paginator.component';
import { AnalysisSidebarComponent } from './sidebar/analysis-sidebar.component';
import { SidebarBackComponent } from './sidebar/sidebar-back.component';
import { SidebarFooterComponent } from './sidebar/sidebar-footer.component';
import { SidebarComponent } from './sidebar/sidebar.component';
import { TagsTableComponent } from './tags/tags-table.component';
import { TagsTextComponent } from './tags/tags-text.component';
import { SituationOnComponent } from './timestamp/situation-on.component';
import { TimestampComponent } from './timestamp/timestamp.component';
import { ToolbarComponent } from './toolbar/toolbar.component';

@NgModule({
  imports: [
    CommonModule,
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
  ],
})
export class SharedModule {}
