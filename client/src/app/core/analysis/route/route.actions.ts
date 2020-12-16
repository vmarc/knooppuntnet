import {createAction} from '@ngrx/store';
import {props} from '@ngrx/store';
import {RouteChangesPage} from '@api/common/route/route-changes-page';
import {RouteDetailsPage} from '@api/common/route/route-details-page';
import {RouteMapPage} from '@api/common/route/route-map-page';
import {ApiResponse} from '@api/custom/api-response';

export const actionRouteLink = createAction(
  '[Route] Link',
  props<{ routeId: string; routeName: string }>()
);

export const actionRouteDetailsLoaded = createAction(
  '[Route] Details loaded',
  props<{ response: ApiResponse<RouteDetailsPage> }>()
);

export const actionRouteMapLoaded = createAction(
  '[Route] Map loaded',
  props<{ response: ApiResponse<RouteMapPage> }>()
);

export const actionRouteChangesLoaded = createAction(
  '[Route] Changes loaded',
  props<{ response: ApiResponse<RouteChangesPage> }>()
);
