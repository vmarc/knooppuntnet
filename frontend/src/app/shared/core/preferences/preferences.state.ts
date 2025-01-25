export type AnalysisStrategy = 'location' | 'network';

export type PreferencesState = {
  strategy: AnalysisStrategy;
  routeType: string;
  extraLayers: boolean;
  pageSize: number;
  impact: boolean;
  showAppearanceOptions: boolean;
  showLegend: boolean;
  showOptions: boolean;
  showProposed: boolean;
  planProposed: boolean;
};

export const initialPreferencesState: PreferencesState = {
  strategy: 'location',
  routeType: null,
  extraLayers: false,
  pageSize: 25,
  impact: true,
  showAppearanceOptions: true,
  showLegend: true,
  showOptions: true,
  showProposed: true,
  planProposed: false,
};
