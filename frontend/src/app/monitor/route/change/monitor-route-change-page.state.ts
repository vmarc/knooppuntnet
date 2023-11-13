import { MonitorRouteChangePage } from '@api/common/monitor';
import { ApiResponse } from '@api/custom';

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
