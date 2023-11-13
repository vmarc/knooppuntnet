import { MonitorChangesPage } from '@api/common/monitor';
import { ApiResponse } from '@api/custom';

export const initialState: MonitorChangesPageState = {
  pageIndex: 0,
  response: null,
};

export interface MonitorChangesPageState {
  pageIndex: number;
  response: ApiResponse<MonitorChangesPage> | null;
}
