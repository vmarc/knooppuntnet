export const initialState: MonitorRouteMapPageState = {
  groupName: '',
  routeName: '',
  relationId: 0,
  routeDescription: '',
};

export interface MonitorRouteMapPageState {
  groupName: string;
  routeName: string;
  relationId: number;
  routeDescription: string;
}
