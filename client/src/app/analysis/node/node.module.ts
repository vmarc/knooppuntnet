import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { MatPaginatorModule } from '@angular/material/paginator';
import { EffectsModule } from '@ngrx/effects';
import { StoreModule } from '@ngrx/store';
import { MarkdownModule } from 'ngx-markdown';
import { OlModule } from '../../components/ol/ol.module';
import { SharedModule } from '../../components/shared/shared.module';
import { AnalysisComponentsModule } from '../components/analysis-components.module';
import { FactModule } from '../fact/fact.module';
import { NodeChangesPageComponent } from './changes/_node-changes-page.component';
import { NodeChangeComponent } from './changes/node-change.component';
import { NodeChangesSidebarComponent } from './changes/node-changes-sidebar.component';
import { NodeChangesService } from './changes/node-changes.service';
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

@NgModule({
  imports: [
    NodeRoutingModule,
    CommonModule,
    StoreModule.forFeature(nodeFeatureKey, nodeReducer),
    EffectsModule.forFeature([NodeEffects]),
    MarkdownModule,
    OlModule,
    SharedModule,
    AnalysisComponentsModule,
    FactModule,
    MatPaginatorModule,
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
    NodePageHeaderComponent,
    NodeChangesSidebarComponent,
    NodeLocationComponent,
    NodeIntegrityComponent,
    NodeDetailsSidebarComponent,
  ],
  providers: [NodeChangesService],
})
export class NodeModule {}
