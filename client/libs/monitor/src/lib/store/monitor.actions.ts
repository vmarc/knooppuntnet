import { MonitorChangesPage } from '@api/common/monitor';
import { MonitorGroupChangesPage } from '@api/common/monitor';
import { MonitorRouteChangePage } from '@api/common/monitor';
import { MonitorRouteChangesPage } from '@api/common/monitor';
import { MonitorRouteSaveResult } from '@api/common/monitor';
import { ApiResponse } from '@api/custom';
import { createAction } from '@ngrx/store';
import { props } from '@ngrx/store';
import { MonitorRouteParameters } from '../route/components/monitor-route-parameters';

export const actionMonitorGroupChangesPageInit = createAction(
  '[MonitorGroupChangesPage] Init'
);

export const actionMonitorGroupChangesPageDestroy = createAction(
  '[MonitorGroupChangesPage] Destroy'
);

export const actionMonitorGroupChangesPageIndex = createAction(
  '[MonitorGroupChangesPage] Page index',
  props<{ pageIndex: number }>()
);

export const actionMonitorGroupChangesPageLoaded = createAction(
  '[MonitorGroupChangesPage] Loaded',
  props<ApiResponse<MonitorGroupChangesPage>>()
);

export const actionMonitorRouteChangesPageInit = createAction(
  '[MonitorRouteChangesPage] Init'
);

export const actionMonitorRouteChangesPageDestroy = createAction(
  '[MonitorRouteChangesPage] Destroy'
);

export const actionMonitorRouteChangesPageIndex = createAction(
  '[MonitorRouteChangesPage] Page index',
  props<{ pageIndex: number }>()
);

export const actionMonitorRouteChangesPageLoaded = createAction(
  '[MonitorRouteChangesPage] Loaded',
  props<ApiResponse<MonitorRouteChangesPage>>()
);

export const actionMonitorRouteChangePageInit = createAction(
  '[MonitorRouteChangePage] Init'
);

export const actionMonitorRouteChangePageDestroy = createAction(
  '[MonitorRouteChangePage] Destroy'
);

export const actionMonitorRouteChangePageLoaded = createAction(
  '[MonitorRouteChangePage] Loaded',
  props<ApiResponse<MonitorRouteChangePage>>()
);

export const actionMonitorRouteInfo = createAction(
  '[Monitor] Route info',
  props<{ relationId: number | null }>()
);

export const actionMonitorRouteAdminRelationIdChanged = createAction(
  '[Monitor] Relation id changed'
);

export const actionMonitorRouteSaveInit = createAction(
  '[Monitor] Route save init',
  props<MonitorRouteParameters>()
);

export const actionMonitorRouteSaveDestroy = createAction(
  '[Monitor] Route save Destroy'
);

export const actionMonitorRouteSaved = createAction(
  '[Monitor] Route saved',
  props<ApiResponse<MonitorRouteSaveResult>>()
);

export const actionMonitorChangesPageInit = createAction(
  '[MonitorChangesPage] Init'
);

export const actionMonitorChangesPageDestroy = createAction(
  '[MonitorChangesPage] Destroy'
);

export const actionMonitorChangesPageIndex = createAction(
  '[MonitorChangesPage] Page index',
  props<{ pageIndex: number }>()
);

export const actionMonitorChangesPageLoaded = createAction(
  '[MonitorChangesPage] Loaded',
  props<ApiResponse<MonitorChangesPage>>()
);
