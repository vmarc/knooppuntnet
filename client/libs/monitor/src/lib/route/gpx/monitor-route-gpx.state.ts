import { MonitorRouteGpxPage } from '@api/common/monitor';
import { ApiResponse } from '@api/custom';

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
