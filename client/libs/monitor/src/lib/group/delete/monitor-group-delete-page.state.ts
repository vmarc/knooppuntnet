import { MonitorGroupPage } from '@api/common/monitor';
import { ApiResponse } from '@api/custom';

export const initialState: MonitorGroupDeletePageState = {
  groupName: '',
  response: null,
};

export interface MonitorGroupDeletePageState {
  groupName: string;
  response: ApiResponse<MonitorGroupPage> | null;
}
