import { Routes } from '@angular/router';
import { AnalysisCanoePageComponent } from './internal/analysis-canoe-page.component';
import { AnalysisCyclingPageComponent } from './internal/analysis-cycling-page.component';
import { AnalysisHikingPageComponent } from './internal/analysis-hiking-page.component';
import { AnalysisHorseRidingPageComponent } from './internal/analysis-horse-riding-page.component';
import { AnalysisInlineSkatingPageComponent } from './internal/analysis-inline-skating-page.component';
import { AnalysisMotorboatPageComponent } from './internal/analysis-motorboat-page.component';
import { AnalysisPageComponent } from './internal/analysis-page.component';
import { LocationUrlMatcher } from './internal/location-url-matcher';

export const analysisRoutes: Routes = [
  {
    path: '',
    children: [
      { path: '', component: AnalysisPageComponent },
      {
        path: 'node',
        loadChildren: () => import('../node/node-routes').then((m) => m.nodeRoutes),
      },
      {
        path: 'route',
        loadChildren: () => import('../route/route-routes').then((m) => m.routeRoutes),
      },
      {
        path: 'network',
        loadChildren: () => import('../network/network-routes').then((m) => m.networkRoutes),
      },
      {
        path: 'changeset',
        loadChildren: () => import('../changeset/change-set-routes').then((m) => m.changeSetRoutes),
      },
      {
        path: 'changes',
        loadChildren: () => import('../changes/changes-routes').then((m) => m.changesRoutes),
      },
      {
        path: 'facts',
        loadChildren: () => import('../facts/facts-routes').then((m) => m.factsRoutes),
      },
      {
        path: 'overview',
        loadChildren: () => import('../overview/overview-routes').then((m) => m.overviewRoutes),
      },
      {
        path: 'cycling',
        component: AnalysisCyclingPageComponent,
      },
      {
        path: 'hiking',
        component: AnalysisHikingPageComponent,
      },
      {
        path: 'horse-riding',
        component: AnalysisHorseRidingPageComponent,
      },
      {
        path: 'motorboat',
        component: AnalysisMotorboatPageComponent,
      },
      {
        path: 'canoe',
        component: AnalysisCanoePageComponent,
      },
      {
        path: 'inline-skating',
        component: AnalysisInlineSkatingPageComponent,
      },
      {
        matcher: LocationUrlMatcher.match,
        loadChildren: () => import('../location/location-routes').then((m) => m.locationRoutes),
      },
      {
        matcher: LocationUrlMatcher.subsetUrl,
        loadChildren: () => import('../subset/subset-routes').then((m) => m.subsetRoutes),
      },
    ],
  },
];
