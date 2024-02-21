export const initialState: MonitorRouteMapPageState = {
  groupName: '',
  routeName: '',
  subRelationIndex: 0,
  routeDescription: '',
};

export interface MonitorRouteMapPageState {
  groupName: string;
  routeName: string;
  subRelationIndex: number;
  routeDescription: string;
}
