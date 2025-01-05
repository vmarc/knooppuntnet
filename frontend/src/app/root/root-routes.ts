import { Routes } from '@angular/router';
import { ExploreComponent } from '../explore/explore.component';
import { NotFoundPageComponent } from '../shared/base/pages/not-found/not-found-page.component';
import { MenuComponent } from './menu/menu.component';
import { AnalysisComponent } from './tryout/analysis.component';
import { MonitorComponent } from './tryout/monitor.component';
import { PlannerComponent } from './tryout/planner.component';

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
    component: PlannerComponent,
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
