import {MonitorAdminRouteGroupPage} from '@api/common/monitor/monitor-admin-route-group-page';
import {MonitorRouteChangePage} from '@api/common/monitor/monitor-route-change-page';
import {MonitorRouteChangesPage} from '@api/common/monitor/monitor-route-changes-page';
import {MonitorRouteDetailsPage} from '@api/common/monitor/monitor-route-details-page';
import {MonitorRouteMapPage} from '@api/common/monitor/monitor-route-map-page';
import {MonitorRoutesPage} from '@api/common/monitor/monitor-routes-page';
import {RouteGroupsPage} from '@api/common/monitor/route-groups-page';
import {ApiResponse} from '@api/custom/api-response';
import {AppState} from '../../core/core.state';

export const initialState: MonitorState = {
  admin: true,
  routeId: 0,
  routeName: '',
  routes: null,
  details: null,
  changes: null,
  change: null,
  map: null,
  mapMode: null,
  mapGpxVisible: false,
  mapGpxOkVisible: false,
  mapGpxNokVisible: false,
  mapOsmRelationVisible: false,
  routeGroups: null,
  adminRouteGroupPage: null
};

export interface MonitorState {
  admin: boolean;
  routeId: number;
  routeName: string;
  routes: ApiResponse<MonitorRoutesPage>;
  details: ApiResponse<MonitorRouteDetailsPage>;
  changes: ApiResponse<MonitorRouteChangesPage>;
  change: ApiResponse<MonitorRouteChangePage>;
  map: ApiResponse<MonitorRouteMapPage>;
  mapMode: string;
  mapGpxVisible: boolean;
  mapGpxOkVisible: boolean;
  mapGpxNokVisible: boolean;
  mapOsmRelationVisible: boolean;
  routeGroups: ApiResponse<RouteGroupsPage>;
  adminRouteGroupPage: ApiResponse<MonitorAdminRouteGroupPage>;
}

export const monitorFeatureKey = 'monitor';

export interface MonitorRootState extends AppState {
  [monitorFeatureKey]: MonitorState;
}
