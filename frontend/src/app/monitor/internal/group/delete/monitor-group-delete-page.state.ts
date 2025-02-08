import { MonitorGroupPage } from '@api/common/monitor/monitor-group-page';
import { ApiResponse } from '@api/custom/api-response';

export const initialState: MonitorGroupDeletePageState = {
  groupName: '',
  response: null,
};

export interface MonitorGroupDeletePageState {
  groupName: string;
  response: ApiResponse<MonitorGroupPage> | null;
}
