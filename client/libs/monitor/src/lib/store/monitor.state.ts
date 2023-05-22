import { MonitorRouteInfoPage } from '@api/common/monitor';
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
}

export const monitorFeatureKey = 'monitor';
