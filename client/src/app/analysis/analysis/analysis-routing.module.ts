import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { Util } from '@app/components/shared';
import { AnalysisSidebarComponent } from './analysis-sidebar.component';
import { AnalysisCanoePageComponent } from './pages';
import { AnalysisCyclingPageComponent } from './pages';
import { AnalysisHikingPageComponent } from './pages';
import { AnalysisHorseRidingPageComponent } from './pages';
import { AnalysisInlineSkatingPageComponent } from './pages';
import { AnalysisMotorboatPageComponent } from './pages';
import { AnalysisPageComponent } from './pages';
import { LocationUrlMatcher } from './pages';

const routes: Routes = [
  Util.routePath('', AnalysisPageComponent, AnalysisSidebarComponent),
  {
    path: 'node',
    loadChildren: () => import('../node/node.module').then((m) => m.NodeModule),
  },
  {
    path: 'route',
    loadChildren: () =>
      import('../route/route.module').then((m) => m.RouteModule),
  },
  {
    path: 'network',
    loadChildren: () =>
      import('../network/network.module').then((m) => m.NetworkModule),
  },
  {
    path: 'changeset',
    loadChildren: () =>
      import('../changeset/change-set.module').then((m) => m.ChangeSetModule),
  },
  {
    path: 'changes',
    loadChildren: () =>
      import('../changes/changes.module').then((m) => m.ChangesModule),
  },
  {
    path: 'facts',
    loadChildren: () =>
      import('../facts/facts.module').then((m) => m.FactsModule),
  },
  {
    path: 'overview',
    loadChildren: () =>
      import('../overview/overview.module').then((m) => m.OverviewModule),
  },
  Util.routePath(
    'cycling',
    AnalysisCyclingPageComponent,
    AnalysisSidebarComponent
  ),
  Util.routePath(
    'hiking',
    AnalysisHikingPageComponent,
    AnalysisSidebarComponent
  ),
  Util.routePath(
    'horse-riding',
    AnalysisHorseRidingPageComponent,
    AnalysisSidebarComponent
  ),
  Util.routePath(
    'motorboat',
    AnalysisMotorboatPageComponent,
    AnalysisSidebarComponent
  ),
  Util.routePath('canoe', AnalysisCanoePageComponent, AnalysisSidebarComponent),
  Util.routePath(
    'inline-skating',
    AnalysisInlineSkatingPageComponent,
    AnalysisSidebarComponent
  ),
  {
    matcher: LocationUrlMatcher.match,
    loadChildren: () =>
      import('../location/location.module').then((m) => m.LocationModule),
  },
  {
    matcher: LocationUrlMatcher.subsetUrl,
    loadChildren: () =>
      import('../subset/subset.module').then((m) => m.SubsetModule),
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class AnalysisRoutingModule {}
