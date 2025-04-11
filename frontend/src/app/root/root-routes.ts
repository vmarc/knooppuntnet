import { Routes } from '@angular/router';
import { ExploreComponent } from '../explore/explore.component';
import { NotFoundPageComponent } from '../shared/base/pages/not-found/not-found-page.component';
import { MenuComponent } from './internal/menu/menu.component';
import { AnalysisComponent } from './internal/tryout/analysis.component';
import { MonitorComponent } from './internal/tryout/monitor.component';

export const rootRoutes: Routes = [
  {
    path: '',
    component: MenuComponent,
  },
  {
    path: 'explore',
    component: ExploreComponent,
  },
  {
    path: 'planner',
    loadComponent: () =>
      import('./internal/tryout/planner.component').then((m) => m.PlannerComponent),
  },
  {
    path: 'analysis',
    component: AnalysisComponent,
  },
  {
    path: 'monitor',
    component: MonitorComponent,
  },
  {
    path: 'not-found',
    component: NotFoundPageComponent,
  },
  {
    path: '**',
    redirectTo: '',
    pathMatch: 'full',
  },
];
