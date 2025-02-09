import { RouteType } from '@api/common/route-type';
import { LayerType } from './layer-type';

export interface LayersState {
  routeType: RouteType;
  layerEnabled: ReadonlyMap<LayerType, boolean>;
  zoom: number;
}
