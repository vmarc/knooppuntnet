import { MonitorRouteUpdatePage } from '@api/common/monitor';
import { ApiResponse } from '@api/custom';

export const initialState: MonitorRouteUpdatePageState = {
  groupName: '',
  routeName: '',
  routeDescription: '',
  response: null,
};

export interface MonitorRouteUpdatePageState {
  readonly groupName: string;
  readonly routeName: string;
  readonly routeDescription: string;
  readonly response: ApiResponse<MonitorRouteUpdatePage> | null;
}
