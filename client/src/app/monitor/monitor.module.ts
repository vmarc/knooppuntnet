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
import { MonitorRouteStep1NameComponent } from './route/add/steps/monitor-route-step1-name.component';
import { MonitorRouteStep2RelationComponent } from './route/add/steps/monitor-route-step2-relation.component';
import { MonitorRouteStep3ReferenceTypeComponent } from './route/add/steps/monitor-route-step3-reference-type.component';
import { MonitorRouteStep4ReferenceDetailsComponent } from './route/add/steps/monitor-route-step4-reference-details.component';
import { MonitorRouteStep5SaveComponent } from './route/add/steps/monitor-route-step5-save.component';
import { MonitorRouteChangeMapComponent } from './route/changes/monitor-route-change-map.component';
import { MonitorRouteChangePageComponent } from './route/changes/monitor-route-change-page.component';
import { MonitorRouteChangesPageComponent } from './route/changes/monitor-route-changes-page.component';
import { MonitorRoutePageHeaderComponent } from './route/components/monitor-route-page-header.component';
import { MonitorRouteReferenceComponent } from './route/components/monitor-route-reference.component';
import { MonitorRouteDeletePageComponent } from './route/delete/monitor-route-delete-page.component';
import { MonitorRouteDetailsPageComponent } from './route/details/monitor-route-details-page.component';
import { LegendLineComponent } from './route/map/legend-line';
import { MonitorRouteMapControlComponent } from './route/map/monitor-route-map-control.component';
import { MonitorRouteMapLayersComponent } from './route/map/monitor-route-map-layers.component';
import { MonitorRouteMapNokSegmentsComponent } from './route/map/monitor-route-map-nok-segments.component';
import { MonitorRouteMapOsmSegmentsComponent } from './route/map/monitor-route-map-osm-segments.component';
import { MonitorRouteMapPageComponent } from './route/map/monitor-route-map-page.component';
import { MonitorRouteMapSidebarComponent } from './route/map/monitor-route-map-sidebar.component';
import { MonitorRouteReferencePageComponent } from './route/reference/monitor-route-reference-page.component';
import { MonitorRouteUpdatePageComponent } from './route/update/monitor-route-update-page.component';
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
    LegendLineComponent,
    MonitorAboutPageComponent,
    MonitorRouteDeletePageComponent,
    MonitorRouteReferenceComponent,
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
    MonitorRouteChangeMapComponent,
    MonitorRouteChangePageComponent,
    MonitorRouteChangesPageComponent,
    MonitorRouteDetailsPageComponent,
    MonitorRouteInfoComponent,
    MonitorRouteMapControlComponent,
    MonitorRouteMapLayersComponent,
    MonitorRouteMapNokSegmentsComponent,
    MonitorRouteMapOsmSegmentsComponent,
    MonitorRouteMapPageComponent,
    MonitorRouteMapSidebarComponent,
    MonitorRoutePageHeaderComponent,
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
