import { MonitorRouteSegmentsPage } from '@api/common/monitor/monitor-route-segments-page';
import { MonitorRouteSummary } from '@api/common/monitor/monitor-route-summary';
import { ApiResponse } from '@api/custom/api-response';

export const initialState: MonitorRouteSegmentsPageState = {
  summary: null,
  response: null,
};

export interface MonitorRouteSegmentsPageState {
  summary: MonitorRouteSummary | null;
  response: ApiResponse<MonitorRouteSegmentsPage> | null;
}
