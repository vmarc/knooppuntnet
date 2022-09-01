import { Bounds } from '@api/common/bounds';
import { MonitorChangesPage } from '@api/common/monitor/monitor-changes-page';
import { MonitorGroupChangesPage } from '@api/common/monitor/monitor-group-changes-page';
import { MonitorGroupPage } from '@api/common/monitor/monitor-group-page';
import { MonitorGroupProperties } from '@api/common/monitor/monitor-group-properties';
import { MonitorGroupsPage } from '@api/common/monitor/monitor-groups-page';
import { MonitorRouteAddPage } from '@api/common/monitor/monitor-route-add-page';
import { MonitorRouteChangePage } from '@api/common/monitor/monitor-route-change-page';
import { MonitorRouteChangesPage } from '@api/common/monitor/monitor-route-changes-page';
import { MonitorRouteDetailsPage } from '@api/common/monitor/monitor-route-details-page';
import { MonitorRouteInfoPage } from '@api/common/monitor/monitor-route-info-page';
import { MonitorRouteMapPage } from '@api/common/monitor/monitor-route-map-page';
import { MonitorRouteNokSegment } from '@api/common/monitor/monitor-route-nok-segment';
import { MonitorRouteUpdatePage } from '@api/common/monitor/monitor-route-update-page';
import { ApiResponse } from '@api/custom/api-response';
import { createAction } from '@ngrx/store';
import { props } from '@ngrx/store';
import { MonitorRouteParameters } from '../route/components/monitor-route-parameters';

export const actionMonitorAdmin = createAction(
  '[Monitor] Admin',
  props<{ admin: boolean }>()
);

export const actionMonitorGroupsPageInit = createAction(
  '[MonitorGroupsPage] Init'
);

export const actionMonitorGroupsPageLoaded = createAction(
  '[MonitorGroupsPage] Loaded',
  props<ApiResponse<MonitorGroupsPage>>()
);

export const actionMonitorNavigateGroup = createAction(
  '[MonitorGroupsPage] Navigate to group',
  props<{ groupName: string; groupDescription: string }>()
);

export const actionMonitorGroupDeleteInit = createAction(
  '[MonitorAdminGroupDeletePage] Init'
);

export const actionMonitorGroupDeleteLoaded = createAction(
  '[MonitorAdminGroupDeletePage] Loaded',
  props<ApiResponse<MonitorGroupPage>>()
);

export const actionMonitorGroupUpdateInit = createAction(
  '[MonitorAdminGroupUpdatePage] Init'
);

export const actionMonitorGroupUpdateLoaded = createAction(
  '[MonitorAdminGroupUpdatePage] Loaded',
  props<ApiResponse<MonitorGroupPage>>()
);

export const actionMonitorGroupPageInit = createAction(
  '[MonitorGroupPage] Init'
);

export const actionMonitorGroupPageLoaded = createAction(
  '[MonitorGroupPage] Loaded',
  props<ApiResponse<MonitorGroupPage>>()
);

export const actionMonitorGroupChangesPageInit = createAction(
  '[MonitorGroupChangesPage] Init'
);

export const actionMonitorGroupChangesPageIndex = createAction(
  '[MonitorGroupChangesPage] Page index',
  props<{ pageIndex: number }>()
);

export const actionMonitorGroupChangesPageLoaded = createAction(
  '[MonitorGroupChangesPage] Loaded',
  props<ApiResponse<MonitorGroupChangesPage>>()
);

export const actionMonitorRouteDetailsPageInit = createAction(
  '[MonitorRouteDetailsPage] Init'
);

export const actionMonitorRouteDetailsPageLoaded = createAction(
  '[MonitorRouteDetailsPage] Loaded',
  props<ApiResponse<MonitorRouteDetailsPage>>()
);

export const actionMonitorRouteMapPageInit = createAction(
  '[MonitorRouteMapPage] Init'
);

export const actionMonitorRouteMapPageLoaded = createAction(
  '[MonitorRouteMapPage] Loaded',
  props<ApiResponse<MonitorRouteMapPage>>()
);

export const actionMonitorRouteChangesPageInit = createAction(
  '[MonitorRouteChangesPage] Init'
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

export const actionMonitorRouteChangePageLoaded = createAction(
  '[MonitorRouteChangePage] Loaded',
  props<ApiResponse<MonitorRouteChangePage>>()
);

export const actionMonitorRouteMapMode = createAction(
  '[Monitor] Map mode',
  props<{ mode: string }>()
);

export const actionMonitorRouteMapSelectDeviation = createAction(
  '[Monitor] Map select deviation',
  props<MonitorRouteNokSegment | null>()
);

export const actionMonitorRouteMapFocus = createAction(
  '[Monitor] Focus',
  props<Bounds>()
);

export const actionMonitorRouteMapReferenceVisible = createAction(
  '[Monitor] Map reference visible',
  props<{ visible: boolean }>()
);

export const actionMonitorRouteMapOkVisible = createAction(
  '[Monitor] Map ok visible',
  props<{ visible: boolean }>()
);

export const actionMonitorRouteMapNokVisible = createAction(
  '[Monitor] Map nok visible',
  props<{ visible: boolean }>()
);

export const actionMonitorRouteMapOsmRelationVisible = createAction(
  '[Monitor] Map osm relation visible',
  props<{ visible: boolean }>()
);

export const actionMonitorRouteMapJosmLoadRouteRelation = createAction(
  '[Monitor] Map josm load route relation'
);

export const actionMonitorRouteMapJosmZoomToFitRoute = createAction(
  '[Monitor] Map josm zoom to fit route'
);

export const actionMonitorRouteMapJosmZoomToSelectedDeviation = createAction(
  '[Monitor] Map josm zoom to fit selected deviation'
);

export const actionMonitorGroupAdd = createAction(
  '[MonitorAdminGroupAddPage] Add group',
  props<MonitorGroupProperties>()
);

export const actionMonitorGroupUpdate = createAction(
  '[MonitorAdminGroupUpdatePage] Update',
  props<{ groupId: string; properties: MonitorGroupProperties }>()
);

export const actionMonitorGroupDelete = createAction(
  '[MonitorAdminGroupDeletePage] Delete',
  props<{ groupId: string }>()
);

export const actionMonitorRouteInfo = createAction(
  '[Monitor] Route info',
  props<{ relationId: string }>()
);

export const actionMonitorRouteAdminRelationIdChanged = createAction(
  '[Monitor] Relation id changed'
);

export const actionMonitorRouteAddPageInit = createAction(
  '[MonitorRouteAddPage] Init'
);

export const actionMonitorRouteAddPageLoaded = createAction(
  '[MonitorRouteAddPage] Loaded',
  props<ApiResponse<MonitorRouteAddPage>>()
);

export const actionMonitorRouteSaveInit = createAction(
  '[Monitor] Route save init',
  props<MonitorRouteParameters>()
);

export const actionMonitorRouteUploadInit = createAction(
  '[Monitor] Route upload init',
  props<MonitorRouteParameters>()
);

export const actionMonitorRouteUploaded = createAction(
  '[Monitor] Route uploaded',
  props<MonitorRouteParameters>()
);

export const actionMonitorRouteAnalyzed = createAction(
  '[Monitor] Route analyzed'
);

export const actionMonitorRouteSaved = createAction('[Monitor] Route saved');

export const actionMonitorRouteDeletePageInit = createAction(
  '[Monitor] Route delete init'
);

export const actionMonitorRouteUpdatePageInit = createAction(
  '[MonitorRouteUpdatePage] Init'
);

export const actionMonitorRouteUpdatePageLoaded = createAction(
  '[MonitorRouteUpdatePage] Loaded',
  props<ApiResponse<MonitorRouteUpdatePage>>()
);

export const actionMonitorRouteDelete = createAction('[Monitor] Route delete');

export const actionMonitorRouteInfoLoaded = createAction(
  '[Monitor] Route info loaded',
  props<ApiResponse<MonitorRouteInfoPage>>()
);

export const actionMonitorChangesPageInit = createAction(
  '[MonitorChangesPage] Init'
);

export const actionMonitorChangesPageIndex = createAction(
  '[MonitorChangesPage] Page index',
  props<{ pageIndex: number }>()
);

export const actionMonitorChangesPageLoaded = createAction(
  '[MonitorChangesPage] Loaded',
  props<ApiResponse<MonitorChangesPage>>()
);
