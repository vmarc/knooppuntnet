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

@NgModule({
  imports: [
    CommonModule,
    KpnMaterialModule
  ],
  declarations: [
    JosmLinkComponent,
    JosmNodeComponent,
    JosmWayComponent,
    JosmRelationComponent,
    OsmLinkComponent,
    OsmLinkNodeComponent,
    CountryNameComponent,
    DataComponent,
    PageComponent
  ],
  exports: [
    JosmNodeComponent,
    JosmWayComponent,
    JosmRelationComponent,
    OsmLinkNodeComponent,
    CountryNameComponent,
    DataComponent,
    PageComponent
  ]
})
export class SharedModule {
}
