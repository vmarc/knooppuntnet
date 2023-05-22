import { MonitorGroupChangesPage } from '@api/common/monitor';
import { ApiResponse } from '@api/custom';

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
