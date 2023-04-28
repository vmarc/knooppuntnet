import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatNativeDateModule } from '@angular/material/core';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatListModule } from '@angular/material/list';
import { MatMenuModule } from '@angular/material/menu';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatRadioModule } from '@angular/material/radio';
import { MatSelectModule } from '@angular/material/select';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatStepperModule } from '@angular/material/stepper';
import { MatTableModule } from '@angular/material/table';
import { OlModule } from '@app/components/ol';
import { SharedModule } from '@app/components/shared';
import { EffectsModule } from '@ngrx/effects';
import { StoreModule } from '@ngrx/store';
import { MarkdownModule } from 'ngx-markdown';
import { MonitorAboutPageComponent } from './about/monitor-about-page.component';
import { MonitorChangesPageComponent } from './changes/monitor-changes-page.component';
import { MonitorAdminToggleComponent } from './components/monitor-admin-toggle.component';
import { MonitorChangeHeaderComponent } from './components/monitor-change-header.component';
import { MonitorChangesComponent } from './components/monitor-changes.component';
import { MonitorPageMenuComponent } from './components/monitor-page-menu.component';
import { MonitorGroupAddPageComponent } from './group/add/monitor-group-add-page.component';
import { MonitorGroupChangesPageComponent } from './group/changes/monitor-group-changes-page.component';
import { MonitorGroupBreadcrumbComponent } from './group/components/monitor-group-breadcrumb.component';
import { MonitorGroupDescriptionComponent } from './group/components/monitor-group-description.component';
import { MonitorGroupNameComponent } from './group/components/monitor-group-name.component';
import { MonitorGroupPageMenuComponent } from './group/components/monitor-group-page-menu.component';
import { MonitorGroupDeletePageComponent } from './group/delete/monitor-group-delete-page.component';
import { MonitorGroupPageComponent } from './group/details/monitor-group-page.component';
import { MonitorGroupRouteTableComponent } from './group/details/monitor-group-route-table.component';
import { MonitorGroupUpdatePageComponent } from './group/update/monitor-group-update-page.component';
import { MonitorGroupTableComponent } from './groups/monitor-group-table.component';
import { MonitorGroupsPageComponent } from './groups/monitor-groups-page.component';
import { MonitorRoutingModule } from './monitor-routing.module';
import { MonitorService } from './monitor.service';
import { MonitorRouteAddPageComponent } from './route/add/monitor-route-add-page.component';
import { MonitorRouteInfoComponent } from './route/add/monitor-route-info.component';
import { MonitorRouteChangeMapComponent } from './route/changes/monitor-route-change-map.component';
import { MonitorRouteChangePageComponent } from './route/changes/monitor-route-change-page.component';
import { MonitorRouteChangesPageComponent } from './route/changes/monitor-route-changes-page.component';
import { MonitorRouteDescriptionComponent } from './route/components/monitor-route-description.component';
import { MonitorRouteNameComponent } from './route/components/monitor-route-name.component';
import { MonitorRoutePageHeaderComponent } from './route/components/monitor-route-page-header.component';
import { MonitorRoutePropertiesStep1GroupComponent } from './route/components/monitor-route-properties-step-1-group.component';
import { MonitorRoutePropertiesStep2NameComponent } from './route/components/monitor-route-properties-step-2-name.component';
import { MonitorRoutePropertiesStep3RelationComponent } from './route/components/monitor-route-properties-step-3-relation.component';
import { MonitorRoutePropertiesStep4ReferenceTypeComponent } from './route/components/monitor-route-properties-step-4-reference-type.component';
import { MonitorRoutePropertiesStep5ReferenceDetailsComponent } from './route/components/monitor-route-properties-step-5-reference-details.component';
import { MonitorRoutePropertiesStep6CommentComponent } from './route/components/monitor-route-properties-step-6-comment.component';
import { MonitorRoutePropertiesComponent } from './route/components/monitor-route-properties.component';
import { MonitorRouteSaveDialogComponent } from './route/components/monitor-route-save-dialog.component';
import { MonitorRouteSaveStepComponent } from './route/components/monitor-route-save-step.component';
import { MonitorRouteSubRelationMenuOptionComponent } from './route/components/monitor-route-sub-relation-menu-option.component';
import { MonitorRouteDeletePageComponent } from './route/delete/monitor-route-delete-page.component';
import { MonitorRouteDetailsAnalysisComponent } from './route/details/monitor-route-details-analysis.component';
import { MonitorRouteDetailsPageComponent } from './route/details/monitor-route-details-page.component';
import { MonitorRouteDetailsReferenceComponent } from './route/details/monitor-route-details-reference.component';
import { MonitorRouteDetailsStructureComponent } from './route/details/monitor-route-details-structure.component';
import { MonitorRouteDetailsSummaryComponent } from './route/details/monitor-route-details-summary.component';
import { LegendLineComponent } from './route/map/legend-line';
import { MonitorRouteMapControlJosmComponent } from './route/map/monitor-route-map-control-josm.component';
import { MonitorRouteMapControlModeComponent } from './route/map/monitor-route-map-control-mode.component';
import { MonitorRouteMapControlComponent } from './route/map/monitor-route-map-control.component';
import { MonitorRouteMapDeviationsComponent } from './route/map/monitor-route-map-deviations.component';
import { MonitorRouteMapLayersComponent } from './route/map/monitor-route-map-layers.component';
import { MonitorRouteMapOsmSegmentsComponent } from './route/map/monitor-route-map-osm-segments.component';
import { MonitorRouteMapPageComponent } from './route/map/monitor-route-map-page.component';
import { MonitorRouteMapSidebarComponent } from './route/map/monitor-route-map-sidebar.component';
import { MonitorRouteMapComponent } from './route/map/monitor-route-map.component';
import { MonitorRouteMapService } from './route/map/monitor-route-map.service';
import { MonitorRouteMapEffects } from './route/map/store/monitor-route-map.effects';
import { monitorRouteMapReducer } from './route/map/store/monitor-route-map.reducer';
import { monitorRouteMapFeatureKey } from './route/map/store/monitor-route-map.state';
import { MonitorRouteUpdatePageComponent } from './route/update/monitor-route-update-page.component';
import { MonitorEffects } from './store/monitor.effects';
import { monitorReducer } from './store/monitor.reducer';
import { monitorFeatureKey } from './store/monitor.state';

@NgModule({
  imports: [
    CommonModule,
    MonitorRoutingModule,
    StoreModule.forFeature(monitorFeatureKey, monitorReducer),
    StoreModule.forFeature(monitorRouteMapFeatureKey, monitorRouteMapReducer),
    EffectsModule.forFeature([MonitorEffects, MonitorRouteMapEffects]),
    SharedModule,
    OlModule,
    MatTableModule,
    MatIconModule,
    MatButtonModule,
    MatListModule,
    MatRadioModule,
    MatCheckboxModule,
    MatSlideToggleModule,
    MatFormFieldModule,
    MatInputModule,
    MatDatepickerModule,
    MatNativeDateModule,
    ReactiveFormsModule,
    MatMenuModule,
    MatStepperModule,
    MatProgressSpinnerModule,
    MatSelectModule,
    MatDialogModule,
    MarkdownModule,
    LegendLineComponent,
    MonitorAboutPageComponent,
    MonitorRouteDeletePageComponent,
    MonitorRouteUpdatePageComponent,
    MonitorAdminToggleComponent,
    MonitorChangeHeaderComponent,
    MonitorChangesComponent,
    MonitorChangesPageComponent,
    MonitorGroupAddPageComponent,
    MonitorGroupBreadcrumbComponent,
    MonitorGroupChangesPageComponent,
    MonitorGroupDeletePageComponent,
    MonitorGroupDescriptionComponent,
    MonitorGroupNameComponent,
    MonitorGroupPageComponent,
    MonitorGroupPageMenuComponent,
    MonitorGroupRouteTableComponent,
    MonitorGroupsPageComponent,
    MonitorGroupTableComponent,
    MonitorGroupUpdatePageComponent,
    MonitorPageMenuComponent,
    MonitorRouteAddPageComponent,
    MonitorRouteNameComponent,
    MonitorRouteDescriptionComponent,
    MonitorRouteChangeMapComponent,
    MonitorRouteChangePageComponent,
    MonitorRouteChangesPageComponent,
    MonitorRouteDetailsPageComponent,
    MonitorRouteInfoComponent,
    MonitorRouteMapControlComponent,
    MonitorRouteMapControlModeComponent,
    MonitorRouteMapControlJosmComponent,
    MonitorRouteMapLayersComponent,
    MonitorRouteMapDeviationsComponent,
    MonitorRouteMapOsmSegmentsComponent,
    MonitorRouteMapPageComponent,
    MonitorRouteMapComponent,
    MonitorRouteMapSidebarComponent,
    MonitorRoutePageHeaderComponent,
    MonitorRoutePropertiesComponent,
    MonitorRoutePropertiesStep1GroupComponent,
    MonitorRoutePropertiesStep2NameComponent,
    MonitorRoutePropertiesStep3RelationComponent,
    MonitorRoutePropertiesStep4ReferenceTypeComponent,
    MonitorRoutePropertiesStep5ReferenceDetailsComponent,
    MonitorRoutePropertiesStep6CommentComponent,
    MonitorRouteSaveDialogComponent,
    MonitorRouteSaveStepComponent,
    MonitorRouteDetailsStructureComponent,
    MonitorRouteDetailsSummaryComponent,
    MonitorRouteDetailsReferenceComponent,
    MonitorRouteDetailsAnalysisComponent,
    MonitorRouteSubRelationMenuOptionComponent,
  ],
  exports: [],
  providers: [MonitorService, MonitorRouteMapService],
})
export class MonitorModule {}
