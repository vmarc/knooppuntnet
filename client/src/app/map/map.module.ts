import {CommonModule} from "@angular/common";
import {NgModule} from "@angular/core";
import {MatButtonModule} from "@angular/material/button";
import {MatButtonToggleModule} from "@angular/material/button-toggle";
import {MatCheckboxModule} from "@angular/material/checkbox";
import {MatDialogModule} from "@angular/material/dialog";
import {MatDividerModule} from "@angular/material/divider";
import {MatExpansionModule} from "@angular/material/expansion";
import {MatIconModule} from "@angular/material/icon";
import {MatProgressBarModule} from "@angular/material/progress-bar";
import {MatRadioModule} from "@angular/material/radio";
import {MatStepperModule} from "@angular/material/stepper";
import {OlModule} from "../components/ol/ol.module";
import {SharedModule} from "../components/shared/shared.module";
import {PdfModule} from "../pdf/pdf.module";
import {MapRoutingModule} from "./map-routing.module";
import {MapPageComponent} from "./pages/map/_map-page.component";
import {MapMainPageComponent} from "./pages/map/map-main-page.component";
import {PoiMenuOptionComponent} from "./pages/map/poi-menu-option.component";
import {PoiMenuComponent} from "./pages/map/poi-menu.component";
import {MapPoiConfigComponent} from "./pages/map/poi/map-poi-config.component";
import {PoiConfigComponent} from "./pages/map/poi/poi-config.component";
import {PoiGroupAmenityComponent} from "./pages/map/poi/poi-group-amenity.component";
import {PoiGroupFoodshopsComponent} from "./pages/map/poi/poi-group-foodshops.component";
import {PoiGroupHikingBikingComponent} from "./pages/map/poi/poi-group-hiking-biking.component";
import {PoiGroupLandmarksComponent} from "./pages/map/poi/poi-group-landmarks.component";
import {PoiGroupPlacesToStayComponent} from "./pages/map/poi/poi-group-places-to-stay.component";
import {PoiGroupRestaurantsComponent} from "./pages/map/poi/poi-group-restaurants.component";
import {PoiGroupShopsComponent} from "./pages/map/poi/poi-group-shops.component";
import {PoiGroupSportsComponent} from "./pages/map/poi/poi-group-sports.component";
import {PoiGroupTourismComponent} from "./pages/map/poi/poi-group-tourism.component";
import {PoiGroupComponent} from "./pages/map/poi/poi-group.component";
import {PoiNamesComponent} from "./pages/map/poi/poi-names.component";
import {MapPopupContentsComponent} from "./pages/map/popup/map-popup-contents.component";
import {MapPopupNodeComponent} from "./pages/map/popup/map-popup-node.component";
import {MapPopupPoiComponent} from "./pages/map/popup/map-popup-poi.component";
import {MapPopupRouteComponent} from "./pages/map/popup/map-popup-route.component";
import {MapPopupComponent} from "./pages/map/popup/map-popup.component";
import {PlannerService} from "./planner.service";
import {MapSidebarComponent} from "./sidebar/_map-sidebar.component";
import {ElevationProfileComponent} from "./sidebar/elevation-profile.component";
import {MapSidebarAppearanceComponent} from "./sidebar/map-side-bar-appearance.component";
import {MapSidebarLegendComponent} from "./sidebar/map-side-bar-legend.component";
import {MapSidebarPlannerComponent} from "./sidebar/map-side-bar-planner.component";
import {MapSidebarPoiConfigurationComponent} from "./sidebar/map-side-bar-poi-configuration.component";
import {PlanActionsComponent} from "./sidebar/plan-actions.component";
import {PlanCompactComponent} from "./sidebar/plan-compact.component";
import {PlanDetailedComponent} from "./sidebar/plan-detailed.component";
import {PlanDistanceComponent} from "./sidebar/plan-distance.component";
import {PlanInstructionCommandComponent} from "./sidebar/plan-instruction-command.component";
import {PlanInstructionComponent} from "./sidebar/plan-instruction.component";
import {PlanInstructionsComponent} from "./sidebar/plan-instructions.component";
import {PlanResultMenuComponent} from "./sidebar/plan-result-menu.component";
import {PlanResultComponent} from "./sidebar/plan-result.component";
import {PlanStepOutputComponent} from "./sidebar/plan-step-output.component";
import {PlanStepRouteComponent} from "./sidebar/plan-step-route.component";
import {PlanStepStartComponent} from "./sidebar/plan-step-start.component";
import {PlanStepsComponent} from "./sidebar/plan-steps.component";
import {PlanTranslationsComponent} from "./sidebar/plan-translations.component";
import {PlanComponent} from "./sidebar/plan.component";

@NgModule({
  imports: [
    CommonModule,
    MatRadioModule,
    MatIconModule,
    MatCheckboxModule,
    MatButtonModule,
    MatButtonToggleModule,
    MatDialogModule,
    MatExpansionModule,
    MatDividerModule,
    MapRoutingModule,
    SharedModule,
    OlModule,
    PdfModule,
    MatProgressBarModule,
    MatStepperModule
  ],
  declarations: [
    MapPageComponent,
    MapMainPageComponent,
    MapSidebarComponent,
    MapSidebarPlannerComponent,
    MapSidebarLegendComponent,
    MapSidebarAppearanceComponent,
    MapSidebarPoiConfigurationComponent,
    MapPopupNodeComponent,
    MapPopupRouteComponent,
    MapPoiConfigComponent,
    PoiGroupComponent,
    PoiGroupAmenityComponent,
    PoiGroupFoodshopsComponent,
    PoiGroupPlacesToStayComponent,
    PoiGroupRestaurantsComponent,
    PoiGroupShopsComponent,
    PoiGroupSportsComponent,
    PoiGroupTourismComponent,
    PoiGroupHikingBikingComponent,
    PoiGroupLandmarksComponent,
    PoiConfigComponent,
    PoiNamesComponent,
    MapPopupPoiComponent,
    PlanInstructionComponent,
    PlanInstructionCommandComponent,
    PlanComponent,
    PlanDistanceComponent,
    PlanCompactComponent,
    PlanDetailedComponent,
    PlanInstructionsComponent,
    PlanTranslationsComponent,
    MapPopupComponent,
    ElevationProfileComponent,
    MapPopupContentsComponent,
    PoiMenuComponent,
    PoiMenuOptionComponent,
    PlanStepsComponent,
    PlanStepStartComponent,
    PlanStepRouteComponent,
    PlanStepOutputComponent,
    PlanResultMenuComponent,
    PlanResultComponent,
    PlanActionsComponent
  ],
  exports: [
    MapPageComponent
  ],
  providers: [
    PlannerService
  ]
})
export class MapModule {
}
