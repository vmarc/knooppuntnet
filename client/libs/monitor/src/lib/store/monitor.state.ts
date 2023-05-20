import { MonitorChangesPage } from '@api/common/monitor';
import { MonitorGroupChangesPage } from '@api/common/monitor';
import { MonitorGroupPage } from '@api/common/monitor';
import { MonitorGroupsPage } from '@api/common/monitor';
import { MonitorRouteAddPage } from '@api/common/monitor';
import { MonitorRouteChangePage } from '@api/common/monitor';
import { MonitorRouteChangesPage } from '@api/common/monitor';
import { MonitorRouteDetailsPage } from '@api/common/monitor';
import { MonitorRouteInfoPage } from '@api/common/monitor';
import { MonitorRouteUpdatePage } from '@api/common/monitor';
import { MonitorRouteGpxPage } from '@api/common/monitor';
import { ApiResponse } from '@api/custom';

export const initialMonitorRouteSaveState: MonitorRouteSaveState = {
  saveRouteEnabled: false,
  saveRouteStatus: 'todo',
  uploadGpxEnabled: false,
  uploadGpxStatus: 'todo',
  analyzeEnabled: false,
  analyzeStatus: 'todo',
  errors: [],
  done: false,
};

export interface MonitorRouteSaveState {
  readonly saveRouteEnabled: boolean;
  readonly saveRouteStatus: 'todo' | 'busy' | 'done';
  readonly uploadGpxEnabled: boolean;
  readonly uploadGpxStatus: 'todo' | 'busy' | 'done';
  readonly analyzeEnabled: boolean;
  readonly analyzeStatus: 'todo' | 'busy' | 'done';
  readonly errors: string[];
  readonly done: boolean;
}

export const initialState: MonitorState = {
  admin: false,
  adminRole: false,
  relationId: undefined,
  routeName: undefined,
  routeDescription: undefined,
  groupName: undefined,
  groupDescription: undefined,
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
  routeGpxPage: undefined,
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
  routeGpxPage: ApiResponse<MonitorRouteGpxPage> | undefined;
  routeChangesPageIndex: number | undefined;
  routeChangesPage: ApiResponse<MonitorRouteChangesPage> | undefined;
  routeChangePage: ApiResponse<MonitorRouteChangePage> | undefined;
}

export const monitorFeatureKey = 'monitor';
