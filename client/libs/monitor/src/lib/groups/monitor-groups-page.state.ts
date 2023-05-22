import { MonitorGroupsPage } from '@api/common/monitor';
import { ApiResponse } from '@api/custom';

export const initialState: MonitorGroupsPageState = {
  response: null,
};

export interface MonitorGroupsPageState {
  response: ApiResponse<MonitorGroupsPage> | null;
}
