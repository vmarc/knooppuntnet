import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {PageComponent} from "./page/page.component";
import {KpnMaterialModule} from "../material/kpn-material.module";
import {DataComponent} from "./data/data.component";
import {OsmLinkComponent} from "./link/osm-link.component";
import {OsmLinkNodeComponent} from "./link/osm-link-node.component";
import {JosmLinkComponent} from "./link/josm-link.component";
import {JosmRelationComponent} from "./link/josm-relation.component";
import {JosmNodeComponent} from "./link/josm-node.component";
import {JosmWayComponent} from "./link/josm-way.component";
import {CountryNameComponent} from "./country-name.component";
import {IconNetworkLinkComponent} from "./link/icon-network-link.component";
import {RouterModule} from "@angular/router";
import {IconRouteLinkComponent} from "./link/icon-route-link.component";
import {TimestampComponent} from "./timestamp/timestamp.component";
import {IconLinkComponent} from "./link/icon-link.component";
import {TagsComponent} from "./tags/tags.component";
import {JsonComponent} from "./json/json.component";
import {DayComponent} from "./day/day.component";
import {OsmLinkRelationComponent} from "./link/osm-link-relation.component";
import {LinkChangesComponent} from "./link/link-changes.component";
import {LinkMapComponent} from "./link/link-map.component";
import {LinkNetworkChangesComponent} from "./link/link-network-changes.component";
import {LinkNetworkDetailsComponent} from "./link/link-network-details.component";
import {LinkNetworkFactsComponent} from "./link/link-network-facts.component";
import {LinkNetworkMapComponent} from "./link/link-network-map.component";
import {LinkNetworkNodesComponent} from "./link/link-network-nodes.component";
import {LinkNetworkRoutesComponent} from "./link/link-network-routes.component";
import {LinkOverviewComponent} from "./link/link-overview.component";
import {LinkRouteComponent} from "./link/link-route.component";
import {LinkSubsetNetworksComponent} from "./link/link-subset-networks.component";
import {LinkSubsetOrphanNodesComponent} from "./link/link-subset-orphan-nodes.component";
import {LinkSubsetOrphanRoutesComponent} from "./link/link-subset-orphan-routes.component";
import {LinkAboutComponent} from "./link/link-about.component";
import {LinkAuthenticateComponent} from "./link/link-authenticate.component";
import {LinkGlossaryComponent} from "./link/link-glossary.component";
import {LinkHomeComponent} from "./link/link-home.component";
import {LinkLinksComponent} from "./link/link-links.component";
import {LinkLoginComponent} from "./link/link-login.component";
import {LinkLogoutComponent} from "./link/link-logout.component";
import {LinkNodeComponent} from "./link/link-node.component";
import {LinkFactComponent} from "./link/link-fact.component";
import {LinkSubsetFactsComponent} from "./link/link-subset-facts.component";
import {LinkChangesetComponent} from "./link/link-changeset.component";
import {LinkSubsetChangesComponent} from "./link/link-subset-changes.component";
import {OsmWebsiteComponent} from "./link/osm-website.component";
import {OsmLinkUserComponent} from "./link/osm-link-user.component";
import {OsmLinkUserAothClientsComponent} from "./link/osm-link-user-aoth-clients.component";
import {ToolbarComponent} from './toolbar/toolbar.component';
import {SidenavComponent} from './sidenav/sidenav.component';
import { SidenavFooterComponent } from './sidenav-footer/sidenav-footer.component';

@NgModule({
  imports: [
    CommonModule,
    RouterModule,
    KpnMaterialModule
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
    LinkSubsetFactsComponent,
    LinkSubsetNetworksComponent,
    LinkSubsetOrphanNodesComponent,
    LinkSubsetOrphanRoutesComponent,
    LinkSubsetChangesComponent,
    LinkAboutComponent,
    LinkAuthenticateComponent,
    LinkGlossaryComponent,
    LinkHomeComponent,
    LinkLinksComponent,
    LinkLoginComponent,
    LinkLogoutComponent,
    JosmLinkComponent,
    JosmNodeComponent,
    JosmWayComponent,
    JosmRelationComponent,
    OsmLinkComponent,
    OsmLinkNodeComponent,
    OsmLinkRelationComponent,
    OsmLinkUserComponent,
    OsmLinkUserAothClientsComponent,
    OsmWebsiteComponent,
    IconLinkComponent,
    IconNetworkLinkComponent,
    IconRouteLinkComponent,
    CountryNameComponent,
    DayComponent,
    TimestampComponent,
    JsonComponent,
    TagsComponent,
    DataComponent,
    PageComponent,
    ToolbarComponent,
    SidenavComponent,
    SidenavFooterComponent
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
    LinkSubsetFactsComponent,
    LinkSubsetNetworksComponent,
    LinkSubsetOrphanNodesComponent,
    LinkSubsetOrphanRoutesComponent,
    LinkSubsetChangesComponent,
    LinkAboutComponent,
    LinkAuthenticateComponent,
    LinkGlossaryComponent,
    LinkHomeComponent,
    LinkLinksComponent,
    LinkLoginComponent,
    LinkLogoutComponent,
    JosmNodeComponent,
    JosmWayComponent,
    JosmRelationComponent,
    OsmLinkNodeComponent,
    OsmLinkRelationComponent,
    OsmLinkUserComponent,
    OsmLinkUserAothClientsComponent,
    OsmWebsiteComponent,
    IconLinkComponent,
    IconNetworkLinkComponent,
    IconRouteLinkComponent,
    NetworkTypeIconComponent,
    NetworkTypeNameComponent,
    CountryNameComponent,
    DayComponent,
    TimestampComponent,
    JsonComponent,
    TagsComponent,
    DataComponent,
    PageComponent,
    ToolbarComponent,
    SidenavComponent,
    SidenavFooterComponent
  ]
})
export class SharedModule {
}
