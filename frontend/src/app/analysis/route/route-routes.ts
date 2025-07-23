import { Routes } from '@angular/router';
import { RouteMembersPageComponent } from '@app/analysis/route/internal/members/route-members-page.component';
import { RoutePathsPageComponent } from '@app/analysis/route/internal/paths/route-paths-page.component';
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
        path: ':routeId/members',
        component: RouteMembersPageComponent,
      },
      {
        path: ':routeId/paths',
        component: RoutePathsPageComponent,
      },
      {
        path: ':routeId/segments',
        component: RouteSegmentsPageComponent,
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
