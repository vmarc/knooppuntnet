import { LongdistanceRouteChangePage } from '@api/common/monitor/longdistance-route-change-page';
import { LongdistanceRouteChangesPage } from '@api/common/monitor/longdistance-route-changes-page';
import { LongdistanceRouteDetailsPage } from '@api/common/monitor/longdistance-route-details-page';
import { LongdistanceRouteMapPage } from '@api/common/monitor/longdistance-route-map-page';
import { LongdistanceRoutesPage } from '@api/common/monitor/longdistance-routes-page';
import { MonitorAdminGroupPage } from '@api/common/monitor/monitor-admin-group-page';
import { MonitorChangesPage } from '@api/common/monitor/monitor-changes-page';
import { MonitorGroupChangesPage } from '@api/common/monitor/monitor-group-changes-page';
import { MonitorGroupPage } from '@api/common/monitor/monitor-group-page';
import { MonitorGroupsPage } from '@api/common/monitor/monitor-groups-page';
import { MonitorRouteChangePage } from '@api/common/monitor/monitor-route-change-page';
import { MonitorRouteChangesPage } from '@api/common/monitor/monitor-route-changes-page';
import { MonitorRouteDetailsPage } from '@api/common/monitor/monitor-route-details-page';
import { MonitorRouteMapPage } from '@api/common/monitor/monitor-route-map-page';
import { ApiResponse } from '@api/custom/api-response';
import { AppState } from '../../core/core.state';

export const initialState: MonitorState = {
  admin: true,
  routeId: 0,
  routeName: '',
  groupName: '',
  groupDescription: '',
  mapMode: null,
  mapGpxVisible: false,
  mapGpxOkVisible: false,
  mapGpxNokVisible: false,
  mapOsmRelationVisible: false,
  changesPageIndex: 0,
  changesPage: null,
  groupsPage: null,
  groupPage: null,
  groupChangesPageIndex: 0,
  groupChangesPage: null,
  adminGroupPage: null,
  routeDetailsPage: null,
  routeMapPage: null,
  routeChangesPageIndex: 0,
  routeChangesPage: null,
  routeChangePage: null,

  longdistanceRouteId: 0,
  longdistanceRouteName: '',
  longdistanceRoutesPage: null,
  longdistanceRouteDetailsPage: null,
  longdistanceRouteChangesPage: null,
  longdistanceRouteChangePage: null,
  longdistanceRouteMapPage: null,
  longdistanceRouteMapMode: null,
  longdistanceRouteMapGpxVisible: null,
  longdistanceRouteMapGpxOkVisible: null,
  longdistanceRouteMapGpxNokVisible: null,
  longdistanceRouteMapOsmRelationVisible: null,
};

export interface MonitorState {
  admin: boolean;
  routeId: number;
  routeName: string;
  groupName: string;
  groupDescription: string;
  mapMode: string;
  mapGpxVisible: boolean;
  mapGpxOkVisible: boolean;
  mapGpxNokVisible: boolean;
  mapOsmRelationVisible: boolean;
  changesPageIndex: number;
  changesPage: ApiResponse<MonitorChangesPage>;
  groupsPage: ApiResponse<MonitorGroupsPage>;
  groupPage: ApiResponse<MonitorGroupPage>;
  groupChangesPageIndex: number;
  groupChangesPage: ApiResponse<MonitorGroupChangesPage>;
  adminGroupPage: ApiResponse<MonitorAdminGroupPage>;
  routeDetailsPage: ApiResponse<MonitorRouteDetailsPage>;
  routeMapPage: ApiResponse<MonitorRouteMapPage>;
  routeChangesPageIndex: number;
  routeChangesPage: ApiResponse<MonitorRouteChangesPage>;
  routeChangePage: ApiResponse<MonitorRouteChangePage>;

  longdistanceRouteId: number;
  longdistanceRouteName: string;
  longdistanceRoutesPage: ApiResponse<LongdistanceRoutesPage>;
  longdistanceRouteDetailsPage: ApiResponse<LongdistanceRouteDetailsPage>;
  longdistanceRouteChangesPage: ApiResponse<LongdistanceRouteChangesPage>;
  longdistanceRouteChangePage: ApiResponse<LongdistanceRouteChangePage>;
  longdistanceRouteMapPage: ApiResponse<LongdistanceRouteMapPage>;
  longdistanceRouteMapMode: string;
  longdistanceRouteMapGpxVisible: boolean;
  longdistanceRouteMapGpxOkVisible: boolean;
  longdistanceRouteMapGpxNokVisible: boolean;
  longdistanceRouteMapOsmRelationVisible: boolean;
}

export const monitorFeatureKey = 'monitor';

export interface MonitorRootState extends AppState {
  [monitorFeatureKey]: MonitorState;
}
