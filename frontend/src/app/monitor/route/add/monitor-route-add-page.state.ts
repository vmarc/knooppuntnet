import { MonitorRouteAddPage } from '@api/common/monitor';
import { ApiResponse } from '@api/custom';

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
