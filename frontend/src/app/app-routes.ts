import { Routes } from '@angular/router';
import { JosmComponent } from './josm.component';
import { TryoutFilterPageComponent } from './tryout/filter/tryout-filter-page.component';
import { TryoutTabsPageComponent } from './tryout/tabs/tryout-tabs-page.component';

export const appRoutes: Routes = [
  {
    path: 'josm',
    component: JosmComponent,
  },
  {
    path: 'tryout-filter',
    component: TryoutFilterPageComponent,
  },
  {
    path: 'tryout-tabs',
    component: TryoutTabsPageComponent,
  },
  {
    path: 'analysis',
    loadChildren: () => import('@app/analysis/analysis').then((m) => m.analysisRoutes),
  },
  {
    path: 'map',
    loadChildren: () => import('@app/planner').then((m) => m.plannerRoutes),
  },
  {
    path: 'status',
    loadChildren: () => import('@app/status').then((m) => m.statusRoutes),
  },
  {
    path: 'settings',
    loadChildren: () => import('@app/settings').then((m) => m.settingsRoutes),
  },
  {
    path: 'poi',
    loadChildren: () => import('@app/poi').then((m) => m.poiRoutes),
  },
  {
    path: 'demo',
    loadChildren: () => import('@app/demo').then((m) => m.demoRoutes),
  },
  {
    path: 'monitor',
    loadChildren: () => import('@app/monitor').then((m) => m.monitorRoutes),
  },
  {
    path: 'symbols',
    loadChildren: () => import('@app/symbol').then((m) => m.symbolRoutes),
  },
  {
    path: '',
    loadChildren: () => import('@app/shared/base').then((m) => m.baseRoutes),
  },
];
