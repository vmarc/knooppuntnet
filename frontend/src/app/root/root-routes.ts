import { Routes } from '@angular/router';
import { NotFoundPageComponent } from '../shared/base/pages/not-found/not-found-page.component';
import { MenuComponent } from './menu.component';
import { AnalysisComponent } from './tryout/analysis.component';
import { ConfigurationComponent } from './tryout/configuration.component';
import { MonitorComponent } from './tryout/monitor.component';
import { PlannerComponent } from './tryout/planner.component';
import { SearchComponent } from './tryout/search.component';

export const rootRoutes: Routes = [
  {
    path: '',
    component: MenuComponent,
  },
  {
    path: 'search',
    component: SearchComponent,
  },
  {
    path: 'configuration',
    component: ConfigurationComponent,
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
