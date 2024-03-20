import { Routes } from '@angular/router';
import { RouteChangesPageComponent } from './changes/route-changes-page.component';
import { RouteDetailsPageComponent } from './details/route-details-page.component';
import { RouteMapPageComponent } from './map/route-map-page.component';

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
    ],
  },
];
