import { Routes } from '@angular/router';

export const appRoutes: Routes = [
  {
    path: 'tryout-filter',
    loadComponent: () =>
      import('./tryout/filter/tryout-filter-page.component').then(
        (m) => m.TryoutFilterPageComponent
      ),
  },
  {
    path: 'tryout-canvas',
    loadComponent: () =>
      import('./tryout/canvas/tryout-canvas-page.component').then(
        (m) => m.TryoutCanvasPageComponent
      ),
  },
  {
    path: 'analysis',
    loadChildren: () => import('./analysis/analysis/analysis-routes').then((m) => m.analysisRoutes),
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
    path: 'monitor',
    loadChildren: () => import('@app/monitor').then((m) => m.monitorRoutes),
  },
  {
    path: 'symbols',
    loadChildren: () => import('@app/symbol').then((m) => m.symbolRoutes),
  },
  {
    path: '',
    loadChildren: () => import('@app/root').then((m) => m.rootRoutes),
  },
];
