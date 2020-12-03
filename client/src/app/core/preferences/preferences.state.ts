export interface PreferencesState {
  networkType: string;
  instructions: boolean;
  extraLayers: boolean;
  itemsPerPage: number;
  impact: boolean;
}

export const initialState: PreferencesState = {
  networkType: null,
  instructions: false,
  extraLayers: false,
  itemsPerPage: 25,
  impact: true
};
