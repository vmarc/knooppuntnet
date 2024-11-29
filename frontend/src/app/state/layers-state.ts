import { NetworkType } from '@api/custom';
import { LayerType } from '../map/layers/layer-type';

export interface LayersState {
  networkType: NetworkType;
  layerEnabled: ReadonlyMap<LayerType, boolean>;
  zoom: number;
}
