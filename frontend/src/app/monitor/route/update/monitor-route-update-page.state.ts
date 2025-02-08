import { MonitorRouteUpdatePage } from '@api/common/monitor/monitor-route-update-page';
import { ApiResponse } from '@api/custom/api-response';

export const initialState: MonitorRouteUpdatePageState = {
  groupName: '',
  routeName: '',
  routeDescription: '',
  groupLink: '',
  response: null,
};

export interface MonitorRouteUpdatePageState {
  readonly groupName: string;
  readonly routeName: string;
  readonly routeDescription: string;
  readonly groupLink: string;
  readonly response: ApiResponse<MonitorRouteUpdatePage> | null;
}
