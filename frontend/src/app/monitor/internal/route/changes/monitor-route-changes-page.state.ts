import { MonitorRouteChangesPage } from '@api/common/monitor/monitor-route-changes-page';
import { ApiResponse } from '@api/custom/api-response';

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
