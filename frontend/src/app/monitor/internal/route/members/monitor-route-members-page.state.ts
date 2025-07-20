import { MonitorRouteMembersPage } from '@api/common/monitor/monitor-route-members-page';
import { ApiResponse } from '@api/custom/api-response';

export const initialState: MonitorRouteMembersPageState = {
  groupName: '',
  routeName: '',
  routeDescription: '',
  memberCount: 999,
  response: null,
};

export interface MonitorRouteMembersPageState {
  groupName: string;
  routeName: string;
  routeDescription: string;
  memberCount: number;
  response: ApiResponse<MonitorRouteMembersPage> | null;
}
