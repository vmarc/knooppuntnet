import { NetworkType } from '@api/custom/network-type';
import { MapLayerState } from '@app/components/ol/domain/map-layer-state';
import { MapPosition } from '@app/components/ol/domain/map-position';
import { MapMode } from '@app/components/ol/services/map-mode';

export const initialState: PlannerState = {
  networkType: NetworkType.hiking,
  position: null,
  mapMode: null,
  resultMode: null,
  layerStates: [],
  pois: false,
  poiLayerStates: [],
};

export interface PlannerState {
  networkType: NetworkType;
  position: MapPosition;
  mapMode: MapMode;
  resultMode: string; // 'compact' | 'detailed'
  layerStates: MapLayerState[];
  pois: boolean;
  poiLayerStates: MapLayerState[];
}

export const plannerFeatureKey = 'planner';
