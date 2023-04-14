import { NetworkType } from '@api/custom';
import { MapLayerState } from '@app/components/ol/domain';
import { MapPosition } from '@app/components/ol/domain';
import { MapMode } from '@app/components/ol/services';

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
