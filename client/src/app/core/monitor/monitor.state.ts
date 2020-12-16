import {MonitorRouteChangePage} from '@api/common/monitor/monitor-route-change-page';
import {MonitorRouteChangesPage} from '@api/common/monitor/monitor-route-changes-page';
import {MonitorRouteDetailsPage} from '@api/common/monitor/monitor-route-details-page';
import {MonitorRouteMapPage} from '@api/common/monitor/monitor-route-map-page';
import {MonitorRoutesPage} from '@api/common/monitor/monitor-routes-page';
import {ApiResponse} from '@api/custom/api-response';

export const initialState: MonitorState = {
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
  mapOsmRelationVisible: false
};

export interface MonitorState {
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
}
