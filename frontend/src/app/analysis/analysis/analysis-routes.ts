import { Routes } from '@angular/router';
import { AnalysisStrategyService } from '@app/analysis/strategy';
import { AnalysisCanoePageComponent } from './pages/analysis-canoe-page.component';
import { AnalysisCyclingPageComponent } from './pages/analysis-cycling-page.component';
import { AnalysisHikingPageComponent } from './pages/analysis-hiking-page.component';
import { AnalysisHorseRidingPageComponent } from './pages/analysis-horse-riding-page.component';
import { AnalysisInlineSkatingPageComponent } from './pages/analysis-inline-skating-page.component';
import { AnalysisMotorboatPageComponent } from './pages/analysis-motorboat-page.component';
import { AnalysisPageComponent } from './pages/analysis-page.component';
import { LocationUrlMatcher } from './pages/location-url-matcher';

export const analysisRoutes: Routes = [
  {
    path: '',
    providers: [AnalysisStrategyService],
    children: [
      { path: '', component: AnalysisPageComponent },
      {
        path: 'node',
        loadChildren: () => import('../node').then((m) => m.nodeRoutes),
      },
      {
        path: 'route',
        loadChildren: () => import('../route').then((m) => m.routeRoutes),
      },
      {
        path: 'network',
        loadChildren: () => import('../network').then((m) => m.networkRoutes),
      },
      {
        path: 'changeset',
        loadChildren: () =>
          import('../changeset').then((m) => m.changeSetRoutes),
      },
      {
        path: 'changes',
        loadChildren: () => import('../changes').then((m) => m.changesRoutes),
      },
      {
        path: 'facts',
        loadChildren: () => import('../facts').then((m) => m.factsRoutes),
      },
      {
        path: 'overview',
        loadChildren: () => import('../overview').then((m) => m.overviewRoutes),
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
        loadChildren: () => import('../location').then((m) => m.locationRoutes),
      },
      {
        matcher: LocationUrlMatcher.subsetUrl,
        loadChildren: () => import('../subset').then((m) => m.subsetRoutes),
      },
    ],
  },
];
