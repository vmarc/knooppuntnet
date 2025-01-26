import { MapLayerState } from '@app/ol/domain';
import { MapPosition } from '@app/ol/domain';
import { MapMode } from '@app/ol/services';
import { MapResultMode } from '../../../ol/services/map-result-mode';

export const initialPlannerState: PlannerState = {
  position: null,
  mapMode: 'surface',
  resultMode: 'compact',
  urlLayerIds: [],
  layerStates: [],
  poiLayerStates: [],
};

export type PlannerState = {
  position: MapPosition;
  mapMode: MapMode;
  resultMode: MapResultMode;
  layerStates: MapLayerState[];
  urlLayerIds: string[];
  poiLayerStates: MapLayerState[];
};
