import {NetworkType} from './network-type';

export interface NodeOrphanRouteReference {
  readonly networkType: NetworkType;
  readonly routeId: number;
  readonly routeName: string;
}
