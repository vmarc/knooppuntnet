import { MonitorGroupPage } from '@api/common/monitor';
import { ApiResponse } from '@api/custom';

export const initialState: MonitorGroupUpdatePageState = {
  groupName: '',
  response: null,
};

export interface MonitorGroupUpdatePageState {
  groupName: string;
  response: ApiResponse<MonitorGroupPage> | null;
}
