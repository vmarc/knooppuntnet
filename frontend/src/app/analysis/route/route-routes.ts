import { Routes } from '@angular/router';
import { RouteSegmentsPageComponent } from '@app/analysis/route/internal/segments/route-segments-page.component';
import { RouteChangesPageComponent } from './internal/changes/route-changes-page.component';
import { RouteDetailsPageComponent } from './internal/details/route-details-page.component';
import { RouteMapPageComponent } from './internal/map/route-map-page.component';

export const routeRoutes: Routes = [
  {
    path: '',
    children: [
      {
        path: ':routeId',
        component: RouteDetailsPageComponent,
      },
      {
        path: ':routeId/map',
        component: RouteMapPageComponent,
      },
      {
        path: ':routeId/changes',
        component: RouteChangesPageComponent,
      },
      {
        path: ':routeId/segments',
        component: RouteSegmentsPageComponent,
      },
    ],
  },
];
