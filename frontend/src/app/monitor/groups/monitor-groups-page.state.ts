import { MonitorGroupsPage } from '@api/common/monitor/monitor-groups-page';
import { ApiResponse } from '@api/custom/api-response';

export const initialState: MonitorGroupsPageState = {
  response: null,
};

export interface MonitorGroupsPageState {
  response: ApiResponse<MonitorGroupsPage> | null;
}
