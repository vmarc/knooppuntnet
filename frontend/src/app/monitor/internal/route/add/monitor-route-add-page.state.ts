import { MonitorRouteAddPage } from '@api/common/monitor/monitor-route-add-page';
import { ApiResponse } from '@api/custom/api-response';

export const initialState: MonitorRouteAddPageState = {
  groupName: '',
  groupDescription: '',
  groupLink: '',
  response: null,
};

export class MonitorRouteAddPageState {
  groupName: string;
  groupDescription: string;
  groupLink: string;
  response: ApiResponse<MonitorRouteAddPage> | null;
}
