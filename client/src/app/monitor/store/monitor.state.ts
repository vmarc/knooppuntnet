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
import { MapPosition } from '../../components/ol/domain/map-position';
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
  relationId: undefined,
  routeName: undefined,
  routeDescription: undefined,
  groupName: undefined,
  groupDescription: undefined,
  mapMode: undefined,
  mapReferenceVisible: undefined,
  mapMatchesVisible: undefined,
  mapDeviationsVisible: undefined,
  mapOsmRelationVisible: undefined,
  mapOsmRelationAvailable: undefined,
  mapOsmRelationEmpty: undefined,
  mapPosition: undefined,
  changesPageIndex: undefined,
  changesPage: undefined,
  groupsPage: undefined,
  groupPage: undefined,
  groupChangesPageIndex: undefined,
  groupChangesPage: undefined,
  routeAddPage: undefined,
  routeUpdatePage: undefined,
  routeInfoPage: undefined,
  routeSaveState: undefined,
  routeDetailsPage: undefined,
  routeMapPage: undefined,
  routeMapSelectedDeviation: undefined,
  routeMapSelectedOsmSegment: undefined,
  routeChangesPageIndex: undefined,
  routeChangesPage: undefined,
  routeChangePage: undefined,
};

export interface MonitorState {
  admin: boolean;
  adminRole: boolean;
  relationId: number | undefined;
  routeName: string | undefined;
  routeDescription: string | undefined;
  groupName: string | undefined;
  groupDescription: string | undefined;
  mapMode: MonitorMapMode | undefined;
  mapReferenceVisible: boolean | undefined;
  mapMatchesVisible: boolean | undefined;
  mapDeviationsVisible: boolean | undefined;
  mapOsmRelationVisible: boolean | undefined;
  mapOsmRelationAvailable: boolean | undefined;
  mapOsmRelationEmpty: boolean | undefined;
  mapPosition: MapPosition | undefined;
  changesPageIndex: number | undefined;
  changesPage: ApiResponse<MonitorChangesPage> | undefined;
  groupsPage: ApiResponse<MonitorGroupsPage> | undefined;
  groupPage: ApiResponse<MonitorGroupPage> | undefined;
  groupChangesPageIndex: number | undefined;
  groupChangesPage: ApiResponse<MonitorGroupChangesPage> | undefined;
  routeAddPage: ApiResponse<MonitorRouteAddPage> | undefined;
  routeUpdatePage: ApiResponse<MonitorRouteUpdatePage> | undefined;
  routeInfoPage: ApiResponse<MonitorRouteInfoPage> | undefined;
  routeSaveState: MonitorRouteSaveState | null;
  routeDetailsPage: ApiResponse<MonitorRouteDetailsPage> | undefined;
  routeMapPage: ApiResponse<MonitorRouteMapPage> | undefined;
  routeMapSelectedDeviation: MonitorRouteDeviation | undefined;
  routeMapSelectedOsmSegment: MonitorRouteSegment | undefined;
  routeChangesPageIndex: number | undefined;
  routeChangesPage: ApiResponse<MonitorRouteChangesPage> | undefined;
  routeChangePage: ApiResponse<MonitorRouteChangePage> | undefined;
}

export const monitorFeatureKey = 'monitor';
