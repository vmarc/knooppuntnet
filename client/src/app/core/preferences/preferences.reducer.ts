import {Action, createReducer} from "@ngrx/store";
import {on} from "@ngrx/store";
import {routerNavigatedAction} from "@ngrx/router-store";
import {paramsIn} from "../core.state";
import {PreferencesState} from "./preferences.model";
import {actionPreferencesNetworkType} from "./preferences.actions";
import {actionPreferencesInstructions} from "./preferences.actions";
import {actionPreferencesExtraLayers} from "./preferences.actions";

const initialState: PreferencesState = {
  networkType: null,
  instructions: false,
  extraLayers: false
};

const reducer = createReducer(
  initialState,
  on(
    routerNavigatedAction,
    (state, action) => {
      const params = paramsIn(action.payload.routerState.root);
      const networkType = params.get("networkType");
      if (networkType) {
        return {...state, networkType: networkType};
      }
      return state;
    }
  ),
  on(
    actionPreferencesNetworkType,
    (state, action) => ({...state, networkType: action.networkType})
  ),
  on(
    actionPreferencesInstructions,
    (state, action) => ({...state, instructions: action.instructions})
  ),
  on(
    actionPreferencesExtraLayers,
    (state, action) => ({...state, extraLayers: action.extraLayers})
  )
);

export function preferencesReducer(
  state: PreferencesState | undefined,
  action: Action
) {
  return reducer(state, action);
}
