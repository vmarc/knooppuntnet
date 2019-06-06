import {CommonModule} from "@angular/common";
import {NgModule} from "@angular/core";
import {MatButtonModule, MatDialogModule, MatDividerModule, MatIconModule, MatListModule, MatToolbarModule} from "@angular/material";
import {RouterModule} from "@angular/router";
import {SpinnerModule} from "../../spinner/spinner.module";
import {CountryNameComponent} from "./country-name.component";
import {DataComponent} from "./data/data.component";
import {DayComponent} from "./day/day.component";
import {IconButtonComponent} from "./icon/icon-button.component";
import {IndicatorDialogComponent} from "./indicator/indicator-dialog.component";
import {IndicatorIconComponent} from "./indicator/indicator-icon.component";
import {IndicatorComponent} from "./indicator/indicator.component";
import {ItemComponent} from "./items/item.component";
import {ItemsComponent} from "./items/items.component";
import {JsonComponent} from "./json/json.component";
import {IconLinkComponent} from "./link/icon-link.component";
import {IconNetworkLinkComponent} from "./link/icon-network-link.component";
import {IconRouteLinkComponent} from "./link/icon-route-link.component";
import {JosmLinkComponent} from "./link/josm-link.component";
import {JosmNodeComponent} from "./link/josm-node.component";
import {JosmRelationComponent} from "./link/josm-relation.component";
import {JosmWayComponent} from "./link/josm-way.component";
import {LinkAboutComponent} from "./link/link-about.component";
import {LinkAuthenticateComponent} from "./link/link-authenticate.component";
import {LinkChangesComponent} from "./link/link-changes.component";
import {LinkChangesetComponent} from "./link/link-changeset.component";
import {LinkFactComponent} from "./link/link-fact.component";
import {LinkGlossaryComponent} from "./link/link-glossary.component";
import {LinkLinksComponent} from "./link/link-links.component";
import {LinkLoginComponent} from "./link/link-login.component";
import {LinkLogoutComponent} from "./link/link-logout.component";
import {LinkMapComponent} from "./link/link-map.component";
import {LinkNetworkChangesComponent} from "./link/link-network-changes.component";
import {LinkNetworkDetailsComponent} from "./link/link-network-details.component";
import {LinkNetworkFactsComponent} from "./link/link-network-facts.component";
import {LinkNetworkMapComponent} from "./link/link-network-map.component";
import {LinkNetworkNodesComponent} from "./link/link-network-nodes.component";
import {LinkNetworkRoutesComponent} from "./link/link-network-routes.component";
import {LinkNodeComponent} from "./link/link-node.component";
import {LinkOverviewComponent} from "./link/link-overview.component";
import {LinkRouteComponent} from "./link/link-route.component";
import {OsmLinkChangeSetComponent} from "./link/osm-link-change-set.component";
import {OsmLinkNodeComponent} from "./link/osm-link-node.component";
import {OsmLinkRelationComponent} from "./link/osm-link-relation.component";
import {OsmLinkUserAothClientsComponent} from "./link/osm-link-user-aoth-clients.component";
import {OsmLinkUserComponent} from "./link/osm-link-user.component";
import {OsmLinkComponent} from "./link/osm-link.component";
import {OsmWebsiteComponent} from "./link/osm-website.component";
import {PageMenuOptionComponent} from "./menu/page-menu-option.component";
import {PageMenuComponent} from "./menu/page-menu.component";
import {MetaDataComponent} from "./meta-data.component";
import {NetworkTypeIconComponent} from "./network-type-icon.component";
import {NetworkTypeNameComponent} from "./network-type-name.component";
import {NetworkTypeComponent} from "./network-type.component";
import {PageFooterComponent} from "./page/page-footer.component";
import {SidebarBackComponent} from "./sidebar/sidebar-back.component";
import {SidebarFooterComponent} from "./sidebar/sidebar-footer.component";
import {SidebarMenuComponent} from "./sidebar/sidebar-menu.component";
import {SidebarNetworkComponent} from "./sidebar/sidebar-network.component";
import {SidebarSubItemComponent} from "./sidebar/sidebar-sub-item.component";
import {SidebarVersionWarningComponent} from "./sidebar/sidebar-version-warning.component";
import {SidebarComponent} from "./sidebar/sidebar.component";
import {SubsetNameComponent} from "./subset-name.component";
import {TagsTableComponent} from "./tags/tags-table.component";
import {TagsTextComponent} from "./tags/tags-text.component";
import {SituationOnComponent} from "./timestamp/situation-on.component";
import {TimestampComponent} from "./timestamp/timestamp.component";
import {ToolbarComponent} from "./toolbar/toolbar.component";

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
    SpinnerModule
  ],
  declarations: [
    LinkChangesetComponent,
    LinkChangesComponent,
    LinkMapComponent,
    LinkNetworkChangesComponent,
    LinkNetworkDetailsComponent,
    LinkNetworkFactsComponent,
    LinkNetworkMapComponent,
    LinkNetworkNodesComponent,
    LinkNetworkRoutesComponent,
    LinkNodeComponent,
    LinkOverviewComponent,
    LinkRouteComponent,
    LinkChangesComponent,
    LinkFactComponent,
    LinkAboutComponent,
    LinkAuthenticateComponent,
    LinkGlossaryComponent,
    LinkLinksComponent,
    LinkLoginComponent,
    LinkLogoutComponent,
    JosmLinkComponent,
    JosmNodeComponent,
    JosmWayComponent,
    JosmRelationComponent,
    OsmLinkComponent,
    OsmLinkNodeComponent,
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
    CountryNameComponent,
    MetaDataComponent,
    DayComponent,
    TimestampComponent,
    SituationOnComponent,
    JsonComponent,
    TagsTableComponent,
    TagsTextComponent,
    DataComponent,
    PageFooterComponent,
    ToolbarComponent,
    SidebarComponent,
    SidebarBackComponent,
    SidebarFooterComponent,
    SidebarVersionWarningComponent,
    SidebarNetworkComponent,
    SidebarMenuComponent,
    SidebarSubItemComponent,
    SubsetNameComponent,
    ItemsComponent,
    ItemComponent,
    IndicatorIconComponent,
    IndicatorComponent,
    IndicatorDialogComponent,
    IconButtonComponent,
    PageMenuComponent,
    PageMenuOptionComponent,
  ],
  exports: [
    LinkChangesetComponent,
    LinkChangesComponent,
    LinkMapComponent,
    LinkNetworkChangesComponent,
    LinkNetworkDetailsComponent,
    LinkNetworkFactsComponent,
    LinkNetworkMapComponent,
    LinkNetworkNodesComponent,
    LinkNetworkRoutesComponent,
    LinkNodeComponent,
    LinkOverviewComponent,
    LinkRouteComponent,
    LinkChangesComponent,
    LinkFactComponent,
    LinkAboutComponent,
    LinkAuthenticateComponent,
    LinkGlossaryComponent,
    LinkLinksComponent,
    LinkLoginComponent,
    LinkLogoutComponent,
    JosmNodeComponent,
    JosmWayComponent,
    JosmRelationComponent,
    JosmLinkComponent,
    OsmLinkComponent,
    OsmLinkNodeComponent,
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
    CountryNameComponent,
    MetaDataComponent,
    DayComponent,
    TimestampComponent,
    SituationOnComponent,
    JsonComponent,
    TagsTableComponent,
    DataComponent,
    PageFooterComponent,
    ToolbarComponent,
    SidebarComponent,
    SidebarBackComponent,
    SidebarFooterComponent,
    SidebarVersionWarningComponent,
    SidebarNetworkComponent,
    SidebarSubItemComponent,
    SubsetNameComponent,
    ItemsComponent,
    ItemComponent,
    IndicatorComponent,
    IndicatorIconComponent,
    IndicatorDialogComponent,
    IconButtonComponent,
    PageMenuComponent,
    PageMenuOptionComponent,
  ]
})
export class SharedModule {
}
