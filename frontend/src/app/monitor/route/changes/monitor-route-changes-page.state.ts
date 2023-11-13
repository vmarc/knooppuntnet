import { MonitorRouteChangesPage } from '@api/common/monitor';
import { ApiResponse } from '@api/custom';

export const initialState: MonitorRouteChangesPageState = {
  groupName: '',
  routeName: '',
  routeDescription: '',
  pageIndex: 0,
  response: null,
};

export interface MonitorRouteChangesPageState {
  groupName: string;
  routeName: string;
  routeDescription: string;
  pageIndex: number;
  response: ApiResponse<MonitorRouteChangesPage> | null;
}
