export interface PreferencesState {
  networkType: string;
  instructions: boolean;
  extraLayers: boolean;
  itemsPerPage: number;
  impact: boolean;
  showAppearanceOptions: boolean;
  showLegend: boolean;
  showOptions: boolean;
  showProposed: boolean;
  planProposed: boolean;
}

export const initialState: PreferencesState = {
  networkType: null,
  instructions: false,
  extraLayers: false,
  itemsPerPage: 25,
  impact: true,
  showAppearanceOptions: true,
  showLegend: true,
  showOptions: true,
  showProposed: true,
  planProposed: false,
};
