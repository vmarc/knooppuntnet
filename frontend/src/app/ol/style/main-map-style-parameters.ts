import { SurveyDateValues } from '@app/core';
import { MapMode } from '../services';

export class MainMapStyleParameters {
  constructor(
    public mapMode: MapMode,
    public showProposed: boolean,
    public surveyDateValues: SurveyDateValues,
    public selectedRouteId: string,
    public selectedNodeId: string
  ) {}
}
