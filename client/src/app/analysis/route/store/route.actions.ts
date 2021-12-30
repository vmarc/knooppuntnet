import { RouteChangesPage } from '@api/common/route/route-changes-page';
import { RouteDetailsPage } from '@api/common/route/route-details-page';
import { RouteMapPage } from '@api/common/route/route-map-page';
import { ApiResponse } from '@api/custom/api-response';
import { NetworkType } from '@api/custom/network-type';
import { createAction } from '@ngrx/store';
import { props } from '@ngrx/store';
import { ChangeOption } from '../../changes/store/changes.actions';

export const actionRouteId = createAction(
  '[Route] Id',
  props<{ routeId: string }>()
);

export const actionRouteLink = createAction(
  '[Route] Link',
  props<{ routeId: string; routeName: string; networkType: NetworkType }>()
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

export const actionRouteChangesPageIndex = createAction(
  '[RouteChangesPage] Page index',
  props<{ pageIndex: number }>()
);

export const actionRouteChangesFilterOption = createAction(
  '[RouteChangesPage] Filter option',
  props<{ option: ChangeOption }>()
);
