import { createFeatureSelector } from '@ngrx/store';
import { createSelector } from '@ngrx/store';
import { monitorFeatureKey } from './monitor.state';
import { MonitorState } from './monitor.state';

export const selectMonitorState =
  createFeatureSelector<MonitorState>(monitorFeatureKey);

export const selectMonitorAdmin = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.admin
);

export const selectMonitorAdminRole = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.adminRole
);

export const selectMonitorChangesPage = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.changesPage
);

export const selectMonitorChangesPageIndex = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.changesPageIndex
);

export const selectMonitorGroupsPage = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.groupsPage
);

export const selectMonitorGroupPage = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.groupPage
);

export const selectMonitorGroupChangesPage = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.groupChangesPage
);

export const selectMonitorGroupChangesPageIndex = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.groupChangesPageIndex
);

export const selectMonitorRouteAddPage = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.routeAddPage
);

export const selectMonitorRouteUpdatePage = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.routeUpdatePage
);

export const selectMonitorRouteInfoPage = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.routeInfoPage
);

export const selectMonitorRouteSaveState = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.routeSaveState
);

export const selectMonitorRouteDetailsPage = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.routeDetailsPage
);

export const selectMonitorRouteChangesPage = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.routeChangesPage
);

export const selectMonitorRouteChangesPageIndex = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.routeChangesPageIndex
);

export const selectMonitorRouteChangePage = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.routeChangePage
);

export const selectMonitorRelationId = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.relationId
);

export const selectMonitorRouteName = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.routeName
);

export const selectMonitorRouteDescription = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.routeDescription
);

export const selectMonitorGroupName = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.groupName
);

export const selectMonitorGroupDescription = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.groupDescription
);
