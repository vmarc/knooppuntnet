import { createFeatureSelector } from '@ngrx/store';
import { createSelector } from '@ngrx/store';
import { monitorFeatureKey } from './monitor.state';
import { MonitorState } from './monitor.state';

export const selectMonitorState =
  createFeatureSelector<MonitorState>(monitorFeatureKey);

export const selectMonitorChangesPage = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.changesPage
);

export const selectMonitorChangesPageIndex = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.changesPageIndex
);

export const selectMonitorGroupChangesPage = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.groupChangesPage
);

export const selectMonitorGroupChangesPageIndex = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.groupChangesPageIndex
);

export const selectMonitorRouteInfoPage = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.routeInfoPage
);

export const selectMonitorRouteChangesPage = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.routeChangesPage
);

export const selectMonitorRouteChangePage = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.routeChangePage
);

export const selectMonitorRouteName = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.routeName
);

export const selectMonitorGroupName = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.groupName
);

export const selectMonitorGroupDescription = createSelector(
  selectMonitorState,
  (state: MonitorState) => state.groupDescription
);
