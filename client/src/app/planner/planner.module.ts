import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatButtonModule } from '@angular/material/button';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatDialogModule } from '@angular/material/dialog';
import { MatDividerModule } from '@angular/material/divider';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatRadioModule } from '@angular/material/radio';
import { EffectsModule } from '@ngrx/effects';
import { StoreModule } from '@ngrx/store';
import { ClipboardModule } from 'ngx-clipboard';
import { OlModule } from '../components/ol/ol.module';
import { PoiAnalysisModule } from '../components/poi/poi-analysis.module';
import { SharedModule } from '../components/shared/shared.module';
import { PdfModule } from '../pdf/pdf.module';
import { GeolocationControlComponent } from './pages/planner/geolocation/geolocation-control.component';
import { GeolocationNotSupportedDialogComponent } from './pages/planner/geolocation/geolocation-not-supported-dialog.component';
import { GeolocationPermissionDeniedDialogComponent } from './pages/planner/geolocation/geolocation-permission-denied-dialog.component';
import { GeolocationTimeoutDialogComponent } from './pages/planner/geolocation/geolocation-timeout-dialog.component';
import { GeolocationUnavailableDialogComponent } from './pages/planner/geolocation/geolocation-unavailable-dialog.component';
import { PlannerPageComponent } from './pages/planner/planner-page.component';
import { PoiMenuOptionComponent } from './pages/planner/poi-menu-option.component';
import { PoiMenuComponent } from './pages/planner/poi-menu.component';
import { MapPoiConfigComponent } from './pages/planner/poi/map-poi-config.component';
import { PoiConfigComponent } from './pages/planner/poi/poi-config.component';
import { PoiGroupAmenityComponent } from './pages/planner/poi/poi-group-amenity.component';
import { PoiGroupFoodshopsComponent } from './pages/planner/poi/poi-group-foodshops.component';
import { PoiGroupHikingBikingComponent } from './pages/planner/poi/poi-group-hiking-biking.component';
import { PoiGroupLandmarksComponent } from './pages/planner/poi/poi-group-landmarks.component';
import { PoiGroupPlacesToStayComponent } from './pages/planner/poi/poi-group-places-to-stay.component';
import { PoiGroupRestaurantsComponent } from './pages/planner/poi/poi-group-restaurants.component';
import { PoiGroupShopsComponent } from './pages/planner/poi/poi-group-shops.component';
import { PoiGroupSportsComponent } from './pages/planner/poi/poi-group-sports.component';
import { PoiGroupTourismComponent } from './pages/planner/poi/poi-group-tourism.component';
import { PoiGroupComponent } from './pages/planner/poi/poi-group.component';
import { MapPopupRouteComponent } from './pages/planner/popup/map-popup-route.component';
import { PlannerPopupContentsComponent } from './pages/planner/popup/planner-popup-contents.component';
import { PlannerPopupNodeComponent } from './pages/planner/popup/planner-popup-node.component';
import { PlannerPopupPoiComponent } from './pages/planner/popup/planner-popup-poi.component';
import { PlannerPopupComponent } from './pages/planner/popup/planner-popup.component';
import { ElevationProfileComponent } from './pages/planner/sidebar/elevation-profile.component';
import { LegendIconComponent } from './pages/planner/sidebar/legend-icon.component';
import { NetworkTypeSelectorComponent } from './pages/planner/sidebar/network-type-selector.component';
import { PlanActionButtonComponent } from './pages/planner/sidebar/plan-action-button.component';
import { PlanActionsComponent } from './pages/planner/sidebar/plan-actions.component';
import { PlanCompactComponent } from './pages/planner/sidebar/plan-compact.component';
import { PlanDetailedComponent } from './pages/planner/sidebar/plan-detailed.component';
import { PlanDistanceComponent } from './pages/planner/sidebar/plan-distance.component';
import { PlanInstructionCommandComponent } from './pages/planner/sidebar/plan-instruction-command.component';
import { PlanInstructionComponent } from './pages/planner/sidebar/plan-instruction.component';
import { PlanInstructionsComponent } from './pages/planner/sidebar/plan-instructions.component';
import { PlanOutputDialogComponent } from './pages/planner/sidebar/plan-output-dialog.component';
import { PlanResultMenuComponent } from './pages/planner/sidebar/plan-result-menu.component';
import { PlanResultComponent } from './pages/planner/sidebar/plan-result.component';
import { PlanTipComponent } from './pages/planner/sidebar/plan-tip.component';
import { PlanComponent } from './pages/planner/sidebar/plan.component';
import { PlannerSideBarAppearanceComponent } from './pages/planner/sidebar/planner-side-bar-appearance.component';
import { PlannerSideBarLegendComponent } from './pages/planner/sidebar/planner-side-bar-legend.component';
import { PlannerSideBarOptionsComponent } from './pages/planner/sidebar/planner-side-bar-options.component';
import { PlannerSideBarPlannerComponent } from './pages/planner/sidebar/planner-side-bar-planner.component';
import { PlannerSideBarPoiConfigurationComponent } from './pages/planner/sidebar/planner-side-bar-poi-configuration.component';
import { PlannerSidebarComponent } from './pages/planner/sidebar/planner-sidebar.component';
import { PlannerToolbarComponent } from './pages/planner/sidebar/planner-toolbar.component';
import { MapPageComponent } from './pages/selector/_map-page.component';
import { PlannerRoutingModule } from './planner-routing.module';
import { PlannerService } from './services/planner.service';
import { PlannerEffects } from './store/planner-effects';
import { plannerReducer } from './store/planner-reducer';
import { plannerFeatureKey } from './store/planner-state';
import { PlannerMapService } from '@app/planner/pages/planner/planner-map.service';

@NgModule({
  imports: [
    CommonModule,
    StoreModule.forFeature(plannerFeatureKey, plannerReducer),
    EffectsModule.forFeature([PlannerEffects]),
    MatRadioModule,
    MatIconModule,
    MatCheckboxModule,
    MatButtonModule,
    MatButtonToggleModule,
    MatDialogModule,
    MatExpansionModule,
    MatDividerModule,
    MatInputModule,
    PlannerRoutingModule,
    MatProgressBarModule,
    MatAutocompleteModule,
    SharedModule,
    OlModule,
    PdfModule,
    ClipboardModule,
    PoiAnalysisModule,
  ],
  declarations: [
    MapPageComponent,
    PlannerPageComponent,
    PlannerSidebarComponent,
    PlannerSideBarPlannerComponent,
    PlannerSideBarLegendComponent,
    PlannerSideBarOptionsComponent,
    PlannerSideBarAppearanceComponent,
    PlannerSideBarPoiConfigurationComponent,
    PlannerPopupNodeComponent,
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
    PlannerPopupPoiComponent,
    PlanInstructionComponent,
    PlanInstructionCommandComponent,
    PlanComponent,
    PlanDistanceComponent,
    PlanCompactComponent,
    PlanDetailedComponent,
    PlanInstructionsComponent,
    PlannerPopupComponent,
    ElevationProfileComponent,
    PlannerPopupContentsComponent,
    PoiMenuComponent,
    PoiMenuOptionComponent,
    PlanResultMenuComponent,
    PlanResultComponent,
    PlanActionsComponent,
    LegendIconComponent,
    NetworkTypeSelectorComponent,
    PlannerToolbarComponent,
    PlanActionButtonComponent,
    PlanOutputDialogComponent,
    PlanTipComponent,
    GeolocationUnavailableDialogComponent,
    GeolocationTimeoutDialogComponent,
    GeolocationPermissionDeniedDialogComponent,
    GeolocationNotSupportedDialogComponent,
    GeolocationControlComponent,
  ],
  exports: [MapPageComponent],
  providers: [PlannerService, PlannerMapService],
})
export class PlannerModule {}
