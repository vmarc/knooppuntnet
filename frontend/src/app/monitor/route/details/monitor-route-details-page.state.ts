import { MonitorRouteDetailsPage } from '@api/common/monitor';
import { ApiResponse } from '@api/custom';

export const initialState: MonitorRouteDetailsPageState = {
  groupName: '',
  routeName: '',
  routeDescription: '',
  response: null,
};

export interface MonitorRouteDetailsPageState {
  groupName: string;
  routeName: string;
  routeDescription: string;
  response: ApiResponse<MonitorRouteDetailsPage> | null;
}
