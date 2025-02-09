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
    loadChildren: () => import('./status/status-routes').then((m) => m.statusRoutes),
  },
  {
    path: 'settings',
    loadChildren: () => import('./settings/settings-routes').then((m) => m.settingsRoutes),
  },
  {
    path: 'poi',
    loadChildren: () => import('./poi/poi-routes').then((m) => m.poiRoutes),
  },
  {
    path: 'monitor',
    loadChildren: () => import('./monitor/monitor-routes').then((m) => m.monitorRoutes),
  },
  {
    path: 'symbols',
    loadChildren: () => import('./symbol/symbol-routes').then((m) => m.symbolRoutes),
  },
  {
    path: '',
    loadChildren: () => import('./root/root-routes').then((m) => m.rootRoutes),
  },
];
