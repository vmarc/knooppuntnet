import { MonitorRouteMembersPage } from '@api/common/monitor/monitor-route-members-page';
import { MonitorRouteSummary } from '@api/common/monitor/monitor-route-summary';
import { ApiResponse } from '@api/custom/api-response';

export const initialState: MonitorRouteMembersPageState = {
  summary: null,
  response: null,
};

export interface MonitorRouteMembersPageState {
  summary: MonitorRouteSummary | null;
  response: ApiResponse<MonitorRouteMembersPage> | null;
}
