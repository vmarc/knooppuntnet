import { MonitorRouteDetailsPage } from '@api/common/monitor/monitor-route-details-page';
import { ApiResponse } from '@api/custom/api-response';

export const initialState: MonitorRouteDeletePageState = {
  groupName: '',
  routeName: '',
  routeDescription: '',
  groupLink: '',
  response: null,
};

export interface MonitorRouteDeletePageState {
  groupName: string;
  routeName: string;
  routeDescription: string;
  groupLink: string;
  response: ApiResponse<MonitorRouteDetailsPage> | null;
}
