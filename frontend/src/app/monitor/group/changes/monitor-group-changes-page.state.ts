import { MonitorGroupChangesPage } from '@api/common/monitor/monitor-group-changes-page';
import { ApiResponse } from '@api/custom/api-response';

export const initialState: MonitorGroupChangesPageState = {
  groupName: '',
  groupDescription: '',
  response: null,
  pageIndex: 0,
};

export interface MonitorGroupChangesPageState {
  groupName: string;
  groupDescription: string;
  response: ApiResponse<MonitorGroupChangesPage> | null;
  pageIndex: number;
}
