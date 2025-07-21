import { MonitorRouteDeviationsPage } from '@api/common/monitor/monitor-route-deviations-page';
import { MonitorRouteSummary } from '@api/common/monitor/monitor-route-summary';
import { ApiResponse } from '@api/custom/api-response';

export const initialState: MonitorRouteDeviationsPageState = {
  summary: null,
  response: null,
};

export interface MonitorRouteDeviationsPageState {
  summary: MonitorRouteSummary | null;
  response: ApiResponse<MonitorRouteDeviationsPage> | null;
}
