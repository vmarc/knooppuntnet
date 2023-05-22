import { MonitorRouteInfoPage } from '@api/common/monitor';
import { MonitorRouteChangePage } from '@api/common/monitor';
import { MonitorRouteChangesPage } from '@api/common/monitor';
import { ApiResponse } from '@api/custom';

export const initialState: MonitorState = {
  admin: false,
  adminRole: false,
  relationId: undefined,
  routeName: undefined,
  routeDescription: undefined,
  groupName: undefined,
  groupDescription: undefined,
  routeInfoPage: undefined,
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
  routeInfoPage: ApiResponse<MonitorRouteInfoPage> | undefined;
  routeChangesPageIndex: number | undefined;
  routeChangesPage: ApiResponse<MonitorRouteChangesPage> | undefined;
  routeChangePage: ApiResponse<MonitorRouteChangePage> | undefined;
}

export const monitorFeatureKey = 'monitor';
