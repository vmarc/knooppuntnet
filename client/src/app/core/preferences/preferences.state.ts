export interface PreferencesState {
  networkType: string;
  instructions: boolean;
  extraLayers: boolean;
}

export const initialState: PreferencesState = {
  networkType: null,
  instructions: false,
  extraLayers: false
};
