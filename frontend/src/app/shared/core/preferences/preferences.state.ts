export enum AnalysisStrategy {
  location = 'location',
  network = 'network',
}

export type PreferencesState = {
  strategy: AnalysisStrategy;
  networkType: string;
  instructions: boolean;
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
  strategy: AnalysisStrategy.location,
  networkType: null,
  instructions: false,
  extraLayers: false,
  pageSize: 25,
  impact: true,
  showAppearanceOptions: true,
  showLegend: true,
  showOptions: true,
  showProposed: true,
  planProposed: false,
};
