import { MonitorGroupPage } from '@api/common/monitor';
import { ApiResponse } from '@api/custom';

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
