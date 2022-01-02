export enum AnalysisMode {
  location = 'location',
  network = 'network',
}

export interface PreferencesState {
  analysisMode: AnalysisMode;
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
}

export const initialState: PreferencesState = {
  analysisMode: AnalysisMode.location,
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
