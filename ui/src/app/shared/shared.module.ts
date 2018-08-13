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

@NgModule({
  imports: [
    CommonModule,
    RouterModule,
    KpnMaterialModule
  ],
  declarations: [
    JosmLinkComponent,
    JosmNodeComponent,
    JosmWayComponent,
    JosmRelationComponent,
    OsmLinkComponent,
    OsmLinkNodeComponent,
    OsmLinkRelationComponent,
    IconLinkComponent,
    IconNetworkLinkComponent,
    IconRouteLinkComponent,
    CountryNameComponent,
    DayComponent,
    TimestampComponent,
    JsonComponent,
    TagsComponent,
    DataComponent,
    PageComponent
  ],
  exports: [
    JosmNodeComponent,
    JosmWayComponent,
    JosmRelationComponent,
    OsmLinkNodeComponent,
    OsmLinkRelationComponent,
    IconLinkComponent,
    IconNetworkLinkComponent,
    IconRouteLinkComponent,
    CountryNameComponent,
    DayComponent,
    TimestampComponent,
    JsonComponent,
    TagsComponent,
    DataComponent,
    PageComponent
  ]
})
export class SharedModule {
}
