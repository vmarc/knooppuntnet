import { Routes } from '@angular/router';
import { RouteMembersPageComponent } from '@app/analysis/route/internal/members/route-members-page.component';
import { RoutePathsPageComponent } from '@app/analysis/route/internal/paths/route-paths-page.component';
import { RouteSegmentsPageComponent } from '@app/analysis/route/internal/segments/route-segments-page.component';
import { RouteComponent } from '@app/analysis/route/internal/route.component';
import { RouteChangesPageComponent } from './internal/changes/route-changes-page.component';
import { RouteDetailsPageComponent } from './internal/details/route-details-page.component';

export const routeRoutes: Routes = [
  {
    path: ':routeId',
    component: RouteComponent,
    children: [
      {
        path: '',
        component: RouteDetailsPageComponent,
      },
      {
        path: 'members',
        component: RouteMembersPageComponent,
      },
      {
        path: 'paths',
        component: RoutePathsPageComponent,
      },
      {
        path: 'segments',
        component: RouteSegmentsPageComponent,
      },
      {
        path: 'changes',
        component: RouteChangesPageComponent,
      },
    ],
  },
];
