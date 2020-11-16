import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AnalysisSidebarComponent} from '../../components/shared/sidebar/analysis-sidebar.component';
import {Util} from '../../components/shared/util';
import {AnalysisCanoePageComponent} from './pages/analysis-canoe-page.component';
import {AnalysisCyclingPageComponent} from './pages/analysis-cycling-page.component';
import {AnalysisHikingPageComponent} from './pages/analysis-hiking-page.component';
import {AnalysisHorseRidingPageComponent} from './pages/analysis-horse-riding-page.component';
import {AnalysisInlineSkatingPageComponent} from './pages/analysis-inline-skating-page.component';
import {AnalysisMotorboatPageComponent} from './pages/analysis-motorboat-page.component';
import {AnalysisPageComponent} from './pages/analysis-page.component';
import {LocationUrlMatcher} from './pages/location-url-matcher';

const routes: Routes = [
  Util.routePath('', AnalysisPageComponent, AnalysisSidebarComponent),
  {
    path: 'node',
    loadChildren: () => import('../node/node.module').then(m => m.NodeModule)
  },
  {
    path: 'route',
    loadChildren: () => import('../route/route.module').then(m => m.RouteModule)
  },
  {
    path: 'network',
    loadChildren: () => import('../network/network.module').then(m => m.NetworkModule)
  },
  {
    path: 'changeset',
    loadChildren: () => import('../changeset/change-set.module').then(m => m.ChangeSetModule)
  },
  {
    path: 'changes',
    loadChildren: () => import('../changes/changes.module').then(m => m.ChangesModule)
  },
  {
    path: 'facts',
    loadChildren: () => import('../facts/facts.module').then(m => m.FactsModule)
  },
  {
    path: 'overview',
    loadChildren: () => import('../overview/overview.module').then(m => m.OverviewModule)
  },
  Util.routePath('cycling', AnalysisCyclingPageComponent, AnalysisSidebarComponent),
  Util.routePath('hiking', AnalysisHikingPageComponent, AnalysisSidebarComponent),
  Util.routePath('horse-riding', AnalysisHorseRidingPageComponent, AnalysisSidebarComponent),
  Util.routePath('motorboat', AnalysisMotorboatPageComponent, AnalysisSidebarComponent),
  Util.routePath('canoe', AnalysisCanoePageComponent, AnalysisSidebarComponent),
  Util.routePath('inline-skating', AnalysisInlineSkatingPageComponent, AnalysisSidebarComponent),
  {
    matcher: LocationUrlMatcher.match,
    loadChildren: () => import('../location/location.module').then(m => m.LocationModule)
  },
  {
    matcher: LocationUrlMatcher.subsetUrl,
    loadChildren: () => import('../subset/subset.module').then(m => m.SubsetModule)
  }
];

@NgModule({
  imports: [
    RouterModule.forChild(routes)
  ],
  exports: [
    RouterModule
  ]
})
export class AnalysisRoutingModule {
}
