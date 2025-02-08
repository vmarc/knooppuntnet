import { MonitorGroupPage } from '@api/common/monitor/monitor-group-page';
import { ApiResponse } from '@api/custom/api-response';

export const initialState: MonitorGroupPageState = {
  groupName: '',
  groupDescription: '',
  response: null,
};

export interface MonitorGroupPageState {
  groupName: string;
  groupDescription: string;
  response: ApiResponse<MonitorGroupPage> | null;
}
