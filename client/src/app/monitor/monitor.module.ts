import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MAT_DATE_LOCALE } from '@angular/material/core';
import { MatNativeDateModule } from '@angular/material/core';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatListModule } from '@angular/material/list';
import { MatMenuModule } from '@angular/material/menu';
import { MatRadioModule } from '@angular/material/radio';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatStepperModule } from '@angular/material/stepper';
import { MatTableModule } from '@angular/material/table';
import { EffectsModule } from '@ngrx/effects';
import { StoreModule } from '@ngrx/store';
import { OlModule } from '../components/ol/ol.module';
import { SharedModule } from '../components/shared/shared.module';
import { MonitorAboutPageComponent } from './about/monitor-about-page.component';
import { MonitorAdminGroupAddPageComponent } from './admin/group/add/monitor-admin-group-add-page.component';
import { MonitorAdminGroupBreadcrumbComponent } from './admin/group/components/monitor-admin-group-breadcrumb.component';
import { MonitorAdminGroupDescriptionComponent } from './admin/group/components/monitor-admin-group-description.component';
import { MonitorAdminGroupNameComponent } from './admin/group/components/monitor-admin-group-name.component';
import { MonitorAdminGroupDeletePageComponent } from './admin/group/delete/monitor-admin-group-delete-page.component';
import { MonitorAdminGroupUpdatePageComponent } from './admin/group/update/monitor-admin-group-update-page.component';
import { MonitorAdminRouteAddPageComponent } from './admin/route/add/monitor-admin-route-add-page.component';
import { MonitorAdminRouteInfoComponent } from './admin/route/add/monitor-admin-route-info.component';
import { MonitorRouteStep1NameComponent } from './admin/route/add/steps/monitor-route-step1-name.component';
import { MonitorRouteStep2RelationComponent } from './admin/route/add/steps/monitor-route-step2-relation.component';
import { MonitorRouteStep3ReferenceTypeComponent } from './admin/route/add/steps/monitor-route-step3-reference-type.component';
import { MonitorRouteStep4ReferenceDetailsComponent } from './admin/route/add/steps/monitor-route-step4-reference-details.component';
import { MonitorRouteStep5SaveComponent } from './admin/route/add/steps/monitor-route-step5-save.component';
import { MonitorAdminRouteReferenceComponent } from './admin/route/components/monitor-admin-route-reference.component';
import { MonitorAdminRouteDeletePageComponent } from './admin/route/delete/monitor-admin-route-delete-page.component';
import { MonitorAdminRouteUpdatePageComponent } from './admin/route/update/monitor-admin-route-update-page.component';
import { MonitorChangesPageComponent } from './changes/monitor-changes-page.component';
import { MonitorAdminToggleComponent } from './components/monitor-admin-toggle.component';
import { MonitorChangeHeaderComponent } from './components/monitor-change-header.component';
import { MonitorChangesComponent } from './components/monitor-changes.component';
import { MonitorPageMenuComponent } from './components/monitor-page-menu.component';
import { MonitorGroupChangesPageComponent } from './group/changes/monitor-group-changes-page.component';
import { MonitorGroupPageMenuComponent } from './group/components/monitor-group-page-menu.component';
import { MonitorGroupPageComponent } from './group/details/monitor-group-page.component';
import { MonitorGroupRouteTableComponent } from './group/details/monitor-group-route-table.component';
import { MonitorGroupTableComponent } from './groups/monitor-group-table.component';
import { MonitorGroupsPageComponent } from './groups/monitor-groups-page.component';
import { MonitorRoutingModule } from './monitor-routing.module';
import { MonitorService } from './monitor.service';
import { MonitorRouteChangeMapComponent } from './route/changes/monitor-route-change-map.component';
import { MonitorRouteChangePageComponent } from './route/changes/monitor-route-change-page.component';
import { MonitorRouteChangesPageComponent } from './route/changes/monitor-route-changes-page.component';
import { MonitorRoutePageHeaderComponent } from './route/components/monitor-route-page-header.component';
import { MonitorRouteDetailsPageComponent } from './route/details/monitor-route-details-page.component';
import { LegendLineComponent } from './route/map/legend-line';
import { MonitorRouteMapControlComponent } from './route/map/monitor-route-map-control.component';
import { MonitorRouteMapLayersComponent } from './route/map/monitor-route-map-layers.component';
import { MonitorRouteMapNokSegmentsComponent } from './route/map/monitor-route-map-nok-segments.component';
import { MonitorRouteMapOsmSegmentsComponent } from './route/map/monitor-route-map-osm-segments.component';
import { MonitorRouteMapPageComponent } from './route/map/monitor-route-map-page.component';
import { MonitorRouteMapSidebarComponent } from './route/map/monitor-route-map-sidebar.component';
import { MonitorRouteReferencePageComponent } from './route/reference/monitor-route-reference-page.component';
import { MonitorEffects } from './store/monitor.effects';
import { monitorReducer } from './store/monitor.reducer';
import { monitorFeatureKey } from './store/monitor.state';

@NgModule({
  imports: [
    CommonModule,
    MonitorRoutingModule,
    StoreModule.forFeature(monitorFeatureKey, monitorReducer),
    EffectsModule.forFeature([MonitorEffects]),
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
  ],
  declarations: [
    MonitorRouteDetailsPageComponent,
    MonitorRouteMapPageComponent,
    MonitorRouteChangesPageComponent,
    MonitorRoutePageHeaderComponent,
    MonitorRouteMapSidebarComponent,
    MonitorRouteMapOsmSegmentsComponent,
    MonitorRouteMapNokSegmentsComponent,
    LegendLineComponent,
    MonitorRouteMapControlComponent,
    MonitorRouteMapLayersComponent,
    MonitorRouteChangePageComponent,
    MonitorChangeHeaderComponent,
    MonitorRouteChangeMapComponent,
    MonitorAboutPageComponent,
    MonitorChangesPageComponent,
    MonitorGroupChangesPageComponent,
    MonitorGroupPageComponent,
    MonitorGroupsPageComponent,
    MonitorGroupTableComponent,
    MonitorPageMenuComponent,
    MonitorGroupPageMenuComponent,
    MonitorAdminToggleComponent,
    MonitorAdminGroupBreadcrumbComponent,
    MonitorAdminGroupNameComponent,
    MonitorAdminGroupDescriptionComponent,
    MonitorAdminGroupAddPageComponent,
    MonitorAdminGroupUpdatePageComponent,
    MonitorAdminGroupDeletePageComponent,
    MonitorAdminRouteAddPageComponent,
    MonitorAdminRouteUpdatePageComponent,
    MonitorAdminRouteDeletePageComponent,
    MonitorGroupRouteTableComponent,
    MonitorAdminRouteInfoComponent,
    MonitorAdminRouteReferenceComponent,
    MonitorChangesComponent,
    MonitorRouteReferencePageComponent,
    MonitorRouteStep1NameComponent,
    MonitorRouteStep2RelationComponent,
    MonitorRouteStep3ReferenceTypeComponent,
    MonitorRouteStep4ReferenceDetailsComponent,
    MonitorRouteStep5SaveComponent,
  ],
  exports: [],
  providers: [{ provide: MAT_DATE_LOCALE, useValue: 'nl-BE' }, MonitorService],
})
export class MonitorModule {}
