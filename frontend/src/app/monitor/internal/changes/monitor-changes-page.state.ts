import { MonitorChangesPage } from '@api/common/monitor/monitor-changes-page';
import { ApiResponse } from '@api/custom/api-response';

export const initialState: MonitorChangesPageState = {
  pageIndex: 0,
  response: null,
};

export interface MonitorChangesPageState {
  pageIndex: number;
  response: ApiResponse<MonitorChangesPage> | null;
}
