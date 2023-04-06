import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MatPaginatorModule } from '@angular/material/paginator';
import { EffectsModule } from '@ngrx/effects';
import { StoreModule } from '@ngrx/store';
import { MarkdownModule } from 'ngx-markdown';
import { OlModule } from '@app/components/ol/ol.module';
import { SharedModule } from '@app/components/shared/shared.module';
import { AnalysisComponentsModule } from '../components/analysis-components.module';
import { FactModule } from '../fact/fact.module';
import { NodeChangesPageComponent } from './changes/_node-changes-page.component';
import { NodeChangeComponent } from './changes/node-change.component';
import { NodeChangesSidebarComponent } from './changes/node-changes-sidebar.component';
import { NodePageHeaderComponent } from './components/node-page-header.component';
import { NodeDetailsPageComponent } from './details/_node-details-page.component';
import { NodeDetailsSidebarComponent } from './details/node-details-sidebar.component';
import { NodeIntegrityComponent } from './details/node-integrity.component';
import { NodeLocationComponent } from './details/node-location.component';
import { NodeNetworkReferenceComponent } from './details/node-network-reference.component';
import { NodeNetworkReferencesComponent } from './details/node-network-references.component';
import { NodeRouteReferencesComponent } from './details/node-route-references.component';
import { NodeSummaryComponent } from './details/node-summary.component';
import { NodeMapPageComponent } from './map/_node-map-page.component';
import { NodeRoutingModule } from './node-routing.module';
import { NodeEffects } from './store/node.effects';
import { nodeReducer } from './store/node.reducer';
import { nodeFeatureKey } from './store/node.state';
import { NodeMapLayerService } from '@app/analysis/node/map/node-map-layer.service';
import { NodeMapComponent } from '@app/analysis/node/map/node-map.component';

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    NodeRoutingModule,
    StoreModule.forFeature(nodeFeatureKey, nodeReducer),
    EffectsModule.forFeature([NodeEffects]),
    MarkdownModule,
    OlModule,
    AnalysisComponentsModule,
    FactModule,
    MatPaginatorModule,
    MatIconModule,
  ],
  declarations: [
    NodeDetailsPageComponent,
    NodeChangeComponent,
    NodeSummaryComponent,
    NodeNetworkReferencesComponent,
    NodeNetworkReferenceComponent,
    NodeRouteReferencesComponent,
    NodeChangesPageComponent,
    NodeMapPageComponent,
    NodeMapComponent,
    NodePageHeaderComponent,
    NodeChangesSidebarComponent,
    NodeLocationComponent,
    NodeIntegrityComponent,
    NodeDetailsSidebarComponent,
  ],
  providers: [NodeMapLayerService],
})
export class NodeModule {}
