import { MonitorRouteGpxPage } from '@api/common/monitor/monitor-route-gpx-page';
import { ApiResponse } from '@api/custom/api-response';

export const initialState: MonitorRouteGpxState = {
  groupName: '',
  routeName: '',
  subRelationId: '',
  groupLink: '',
  routeLink: '',
  response: null,
};

export interface MonitorRouteGpxState {
  groupName: string;
  routeName: string;
  subRelationId: string;
  groupLink: string;
  routeLink: string;
  response: ApiResponse<MonitorRouteGpxPage> | null;
}
