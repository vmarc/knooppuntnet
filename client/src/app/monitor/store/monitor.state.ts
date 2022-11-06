import { MonitorChangesPage } from '@api/common/monitor/monitor-changes-page';
import { MonitorGroupChangesPage } from '@api/common/monitor/monitor-group-changes-page';
import { MonitorGroupPage } from '@api/common/monitor/monitor-group-page';
import { MonitorGroupsPage } from '@api/common/monitor/monitor-groups-page';
import { MonitorRouteAddPage } from '@api/common/monitor/monitor-route-add-page';
import { MonitorRouteChangePage } from '@api/common/monitor/monitor-route-change-page';
import { MonitorRouteChangesPage } from '@api/common/monitor/monitor-route-changes-page';
import { MonitorRouteDetailsPage } from '@api/common/monitor/monitor-route-details-page';
import { MonitorRouteDeviation } from '@api/common/monitor/monitor-route-deviation';
import { MonitorRouteInfoPage } from '@api/common/monitor/monitor-route-info-page';
import { MonitorRouteMapPage } from '@api/common/monitor/monitor-route-map-page';
import { MonitorRouteSegment } from '@api/common/monitor/monitor-route-segment';
import { MonitorRouteUpdatePage } from '@api/common/monitor/monitor-route-update-page';
import { ApiResponse } from '@api/custom/api-response';
import { MonitorMapMode } from '../route/map/monitor-map-mode';

export class MonitorRouteSaveState {
  constructor(
    readonly saveRouteEnabled: boolean = false,
    readonly saveRouteStatus: 'todo' | 'busy' | 'done' = 'todo',
    readonly uploadGpxEnabled: boolean = false,
    readonly uploadGpxStatus: 'todo' | 'busy' | 'done' = 'todo',
    readonly analyzeEnabled: boolean = false,
    readonly analyzeStatus: 'todo' | 'busy' | 'done' = 'todo',
    readonly errors: string[] = [],
    readonly done: boolean = false
  ) {}
}

export const initialState: MonitorState = {
  admin: false,
  adminRole: false,
  routeId: '',
  relationId: 0,
  routeName: '',
  routeDescription: '',
  groupName: '',
  groupDescription: '',
  mapMode: MonitorMapMode.comparison,
  mapReferenceVisible: false,
  mapMatchesVisible: false,
  mapDeviationsVisible: false,
  mapOsmRelationVisible: false,
  changesPageIndex: 0,
  changesPage: null,
  groupsPage: null,
  groupPage: null,
  groupChangesPageIndex: 0,
  groupChangesPage: null,
  routeAddPage: null,
  routeUpdatePage: null,
  routeInfoPage: null,
  routeSaveState: null,
  routeDetailsPage: null,
  routeMapPage: null,
  routeMapSelectedDeviation: null,
  routeMapSelectedOsmSegment: null,
  routeChangesPageIndex: 0,
  routeChangesPage: null,
  routeChangePage: null,
};

export interface MonitorState {
  admin: boolean;
  adminRole: boolean;
  routeId: string;
  relationId: number;
  routeName: string;
  routeDescription: string;
  groupName: string;
  groupDescription: string;
  mapMode: MonitorMapMode;
  mapReferenceVisible: boolean;
  mapMatchesVisible: boolean;
  mapDeviationsVisible: boolean;
  mapOsmRelationVisible: boolean;
  changesPageIndex: number;
  changesPage: ApiResponse<MonitorChangesPage>;
  groupsPage: ApiResponse<MonitorGroupsPage>;
  groupPage: ApiResponse<MonitorGroupPage>;
  groupChangesPageIndex: number;
  groupChangesPage: ApiResponse<MonitorGroupChangesPage>;
  routeAddPage: ApiResponse<MonitorRouteAddPage>;
  routeUpdatePage: ApiResponse<MonitorRouteUpdatePage>;
  routeInfoPage: ApiResponse<MonitorRouteInfoPage>;
  routeSaveState: MonitorRouteSaveState | null;
  routeDetailsPage: ApiResponse<MonitorRouteDetailsPage>;
  routeMapPage: ApiResponse<MonitorRouteMapPage>;
  routeMapSelectedDeviation: MonitorRouteDeviation;
  routeMapSelectedOsmSegment: MonitorRouteSegment;
  routeChangesPageIndex: number;
  routeChangesPage: ApiResponse<MonitorRouteChangesPage>;
  routeChangePage: ApiResponse<MonitorRouteChangePage>;
}

export const monitorFeatureKey = 'monitor';
