import { Routes } from '@angular/router';
import { ExplorePageComponent } from '../explore/explore-page.component';
import { NotFoundPageComponent } from '../shared/base/pages/not-found/not-found-page.component';
import { MenuComponent } from './internal/menu/menu.component';

export const rootRoutes: Routes = [
  {
    path: '',
    component: MenuComponent,
  },
  {
    path: 'explore',
    component: ExplorePageComponent,
  },
  {
    path: 'planner',
    loadComponent: () =>
      import('./internal/tryout/planner.component').then((m) => m.PlannerComponent),
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
