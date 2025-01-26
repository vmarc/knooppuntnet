import { RouteType } from '@api/common';
import { AnalysisStrategy } from './analysis-strategy';

export type Preferences = {
  strategy: AnalysisStrategy;
  routeType: RouteType;
  extraLayers: boolean;
  pageSize: number;
  impact: boolean;
  showLegend: boolean;
  showOptions: boolean;
  showProposed: boolean;
  planProposed: boolean;
};
