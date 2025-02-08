import { MonitorGroupPage } from '@api/common/monitor/monitor-group-page';
import { ApiResponse } from '@api/custom/api-response';

export const initialState: MonitorGroupUpdatePageState = {
  groupName: '',
  response: null,
};

export interface MonitorGroupUpdatePageState {
  groupName: string;
  response: ApiResponse<MonitorGroupPage> | null;
}
