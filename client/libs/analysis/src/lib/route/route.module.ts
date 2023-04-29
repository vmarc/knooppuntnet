import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { AnalysisComponentsModule } from '@app/analysis/components';
import { FactModule } from '@app/analysis/fact';
import { OlModule } from '@app/components/ol';
import { SharedModule } from '@app/components/shared';
import { EffectsModule } from '@ngrx/effects';
import { StoreModule } from '@ngrx/store';
import { MarkdownModule } from 'ngx-markdown';
import { RouteChangesPageComponent } from './changes/_route-changes-page.component';
import { RouteChangeComponent } from './changes/route-change.component';
import { RouteChangesSidebarComponent } from './changes/route-changes-sidebar.component';
import { RoutePageHeaderComponent } from './components/route-page-header.component';
import { RoutePageComponent } from './details/_route-page.component';
import { LinkImageComponent } from './details/link-image.component';
import { RouteEndNodesComponent } from './details/route-end-nodes.component';
import { RouteFreeNodesComponent } from './details/route-free-nodes.component';
import { RouteLocationComponent } from './details/route-location.component';
import { RouteMembersComponent } from './details/route-members.component';
import { RouteNetworkReferencesComponent } from './details/route-network-references.component';
import { RouteNodeComponent } from './details/route-node.component';
import { RouteRedundantNodesComponent } from './details/route-redundant-nodes.component';
import { RouteStartNodesComponent } from './details/route-start-nodes.component';
import { RouteStructureComponent } from './details/route-structure.component';
import { RouteSummaryComponent } from './details/route-summary.component';
import { RouteMapPageComponent } from './map/_route-map-page.component';
import { RouteMapComponent } from './map/route-map.component';
import { RouteMapService } from './map/route-map.service';
import { RouteRoutingModule } from './route-routing.module';
import { RouteEffects } from './store/route.effects';
import { routeReducer } from './store/route.reducer';
import { routeFeatureKey } from './store/route.state';

@NgModule({
  imports: [
    CommonModule,
    StoreModule.forFeature(routeFeatureKey, routeReducer),
    EffectsModule.forFeature([RouteEffects]),
    MarkdownModule,
    MatIconModule,
    SharedModule,
    FactModule,
    AnalysisComponentsModule,
    RouteRoutingModule,
    OlModule,
    RouteChangeComponent,
    RouteChangesPageComponent,
    RouteMapPageComponent,
    RouteMapComponent,
    RouteMembersComponent,
    RouteNetworkReferencesComponent,
    RouteNodeComponent,
    RoutePageComponent,
    RoutePageHeaderComponent,
    RouteFreeNodesComponent,
    RouteStartNodesComponent,
    RouteEndNodesComponent,
    RouteRedundantNodesComponent,
    RouteStructureComponent,
    RouteSummaryComponent,
    RouteChangesSidebarComponent,
    RouteLocationComponent,
    LinkImageComponent,
  ],
  providers: [RouteMapService],
})
export class RouteModule {}