import { MonitorRouteDetailsPage } from '@api/common/monitor/monitor-route-details-page';
import { ApiResponse } from '@api/custom/api-response';

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
