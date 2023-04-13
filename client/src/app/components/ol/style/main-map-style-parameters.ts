import { MapMode } from '../services';
import { SurveyDateValues } from '../services';

export class MainMapStyleParameters {
  constructor(
    public mapMode: MapMode,
    public showProposed: boolean,
    public surveyDateValues: SurveyDateValues,
    public selectedRouteId: string,
    public selectedNodeId: string,
    public highlightedRouteId: string
  ) {}
}
