import { RouteChangesPage } from '@api/common/route/route-changes-page';
import { RouteDetailsPage } from '@api/common/route/route-details-page';
import { RouteMapPage } from '@api/common/route/route-map-page';
import { ApiResponse } from '@api/custom/api-response';
import { createAction } from '@ngrx/store';
import { props } from '@ngrx/store';

export const actionRouteId = createAction(
  '[Route] Id',
  props<{ routeId: string }>()
);

export const actionRouteLink = createAction(
  '[Route] Link',
  props<{ routeId: string; routeName: string }>()
);

export const actionRouteDetailsPageInit = createAction(
  '[RouteDetailsPage] Init'
);

export const actionRouteDetailsPageLoaded = createAction(
  '[RouteDetailsPage] Loaded',
  props<{ response: ApiResponse<RouteDetailsPage> }>()
);

export const actionRouteMapPageInit = createAction('[RouteMapPage] Init');

export const actionRouteMapPageLoaded = createAction(
  '[RouteMapPage] Loaded',
  props<{ response: ApiResponse<RouteMapPage> }>()
);

export const actionRouteChangesPageInit = createAction(
  '[RouteChangesPage] Init'
);

export const actionRouteChangesPageLoaded = createAction(
  '[RouteChangesPage] Loaded',
  props<{ response: ApiResponse<RouteChangesPage> }>()
);
