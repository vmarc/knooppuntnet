import { MonitorRouteChangePage } from '@api/common/monitor/monitor-route-change-page';
import { ApiResponse } from '@api/custom/api-response';

export const initialState: MonitorRouteChangePageState = {
  groupName: '',
  routeName: '',
  routeDescription: '',
  changeSetId: '',
  replicationNumber: '',
  response: null,
};

export interface MonitorRouteChangePageState {
  groupName: string;
  routeName: string;
  routeDescription: string;
  changeSetId: string;
  replicationNumber: string;
  response: ApiResponse<MonitorRouteChangePage> | null;
}
