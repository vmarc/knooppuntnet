import { MonitorChangesPage } from '@api/common/monitor/monitor-changes-page';
import { MonitorGroupChangesPage } from '@api/common/monitor/monitor-group-changes-page';
import { MonitorGroupPage } from '@api/common/monitor/monitor-group-page';
import { MonitorGroupsPage } from '@api/common/monitor/monitor-groups-page';
import { MonitorRouteChangePage } from '@api/common/monitor/monitor-route-change-page';
import { MonitorRouteChangesPage } from '@api/common/monitor/monitor-route-changes-page';
import { MonitorRouteDetailsPage } from '@api/common/monitor/monitor-route-details-page';
import { MonitorRouteInfoPage } from '@api/common/monitor/monitor-route-info-page';
import { MonitorRouteMapPage } from '@api/common/monitor/monitor-route-map-page';
import { MonitorRouteNokSegment } from '@api/common/monitor/monitor-route-nok-segment';
import { ApiResponse } from '@api/custom/api-response';

export const initialState: MonitorState = {
  admin: false,
  adminRole: false,
  monitorRouteId: '',
  routeId: 0,
  routeName: '',
  routeDescription: '',
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
  routeInfoPage: null,
  routeDetailsPage: null,
  routeMapPage: null,
  routeMapSelectedDeviation: null,
  routeChangesPageIndex: 0,
  routeChangesPage: null,
  routeChangePage: null,
};

export interface MonitorState {
  admin: boolean;
  adminRole: boolean;
  monitorRouteId: string;
  routeId: number;
  routeName: string;
  routeDescription: string;
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
  routeInfoPage: ApiResponse<MonitorRouteInfoPage>;
  routeDetailsPage: ApiResponse<MonitorRouteDetailsPage>;
  routeMapPage: ApiResponse<MonitorRouteMapPage>;
  routeMapSelectedDeviation: MonitorRouteNokSegment;
  routeChangesPageIndex: number;
  routeChangesPage: ApiResponse<MonitorRouteChangesPage>;
  routeChangePage: ApiResponse<MonitorRouteChangePage>;
}

export const monitorFeatureKey = 'monitor';
