import { createReducer } from '@ngrx/store';
import { on } from '@ngrx/store';
import { actionMonitorRouteAdminRelationIdChanged } from './monitor.actions';
import { MonitorState } from './monitor.state';
import { initialState } from './monitor.state';

export const monitorReducer = createReducer<MonitorState>(
  initialState,
  on(actionMonitorRouteAdminRelationIdChanged, (state): MonitorState => {
    return {
      ...state,
      routeInfoPage: null,
    };
  })
);
