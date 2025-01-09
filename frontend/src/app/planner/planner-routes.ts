import { Routes } from '@angular/router';
import { PlannerPageComponent } from './pages/planner/planner-page.component';
import { MapPageComponent } from './pages/selector/map-page.component';

export const plannerRoutes: Routes = [
  {
    path: '',
    children: [
      {
        path: '',
        pathMatch: 'full',
        component: MapPageComponent,
      },
      {
        path: ':routeType',
        children: [
          {
            path: '',
            component: PlannerPageComponent,
          },
        ],
      },
    ],
  },
];
