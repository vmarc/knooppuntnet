import {CommonModule} from "@angular/common";
import {NgModule} from "@angular/core";
import {ReactiveFormsModule} from "@angular/forms";
import {MatAutocompleteModule} from "@angular/material/autocomplete";
import {MatButtonModule} from "@angular/material/button";
import {MatDividerModule} from "@angular/material/divider";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatIconModule} from "@angular/material/icon";
import {MatInputModule} from "@angular/material/input";
import {MatProgressBarModule} from "@angular/material/progress-bar";
import {MatRadioModule} from "@angular/material/radio";
import {MatSortModule} from "@angular/material/sort";
import {MatTableModule} from "@angular/material/table";
import {MatTreeModule} from "@angular/material/tree";
import {OlModule} from "../../components/ol/ol.module";
import {SharedModule} from "../../components/shared/shared.module";
import {FactModule} from "../fact/fact.module";
import {LocationChangesPageComponent} from "./changes/location-changes-page.component";
import {LocationChangesComponent} from "./changes/location-changes.component";
import {LocationPageBreadcrumbComponent} from "./components/location-page-breadcrumb.component";
import {LocationPageHeaderComponent} from "./components/location-page-header.component";
import {LocationResponseComponent} from "./components/location-response.component";
import {LocationEditPageComponent} from "./edit/location-edit-page.component";
import {LocationEditComponent} from "./edit/location-edit.component";
import {LocationFactsPageComponent} from "./facts/location-facts-page.component";
import {LocationFactsComponent} from "./facts/location-facts.component";
import {LocationRoutingModule} from "./location-routing.module";
import {LocationService} from "./location.service";
import {LocationMapPageComponent} from "./map/location-map-page.component";
import {LocationNodeRoutesComponent} from "./nodes/location-node-routes.component";
import {LocationNodeTableComponent} from "./nodes/location-node-table.component";
import {LocationNodesPageComponent} from "./nodes/location-nodes-page.component";
import {LocationNodesComponent} from "./nodes/location-nodes.component";
import {LocationRouteTableComponent} from "./routes/location-route-table.component";
import {LocationRoutesPageComponent} from "./routes/location-routes-page.component";
import {LocationRoutesComponent} from "./routes/location-routes.component";
import {LocationModeComponent} from "./selection/location-mode.component";
import {LocationModeService} from "./selection/location-mode.service";
import {LocationSelectionPageComponent} from "./selection/location-selection-page.component";
import {LocationSelectionService} from "./selection/location-selection.service";
import {LocationSelectorComponent} from "./selection/location-selector.component";
import {LocationTreeComponent} from "./selection/location-tree.component";

@NgModule({
  imports: [
    LocationRoutingModule,
    CommonModule,
    SharedModule,
    MatDividerModule,
    MatTableModule,
    MatSortModule,
    MatFormFieldModule,
    MatAutocompleteModule,
    MatInputModule,
    ReactiveFormsModule,
    MatIconModule,
    MatTreeModule,
    MatRadioModule,
    MatButtonModule,
    FactModule,
    MatProgressBarModule,
    OlModule
  ],
  declarations: [
    LocationPageHeaderComponent,
    LocationPageBreadcrumbComponent,
    LocationNodesPageComponent,
    LocationFactsPageComponent,
    LocationRoutesPageComponent,
    LocationMapPageComponent,
    LocationChangesPageComponent,
    LocationNodeTableComponent,
    LocationNodeRoutesComponent,
    LocationRouteTableComponent,
    LocationRoutesComponent,
    LocationNodesComponent,
    LocationResponseComponent,
    LocationChangesComponent,
    LocationFactsComponent,
    LocationModeComponent,
    LocationSelectionPageComponent,
    LocationSelectorComponent,
    LocationTreeComponent,
    LocationEditPageComponent,
    LocationEditComponent
  ],
  providers: [
    LocationService,
    LocationModeService,
    LocationSelectionService
  ]
})
export class LocationModule {
}
