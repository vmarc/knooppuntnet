import { MonitorRouteDetailsPage } from '@api/common/monitor/monitor-route-details-page';
import { MonitorRouteSummary } from '@api/common/monitor/monitor-route-summary';
import { ApiResponse } from '@api/custom/api-response';

export const initialState: MonitorRouteDetailsPageState = {
  summary: null,
  response: null,
};

export interface MonitorRouteDetailsPageState {
  summary: MonitorRouteSummary | null;
  response: ApiResponse<MonitorRouteDetailsPage> | null;
}
