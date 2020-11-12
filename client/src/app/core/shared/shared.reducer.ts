import {Action, createReducer} from "@ngrx/store";
import {on} from "@ngrx/store";
import {SharedState} from "./shared.model";
import {routerNavigatedAction} from "@ngrx/router-store";
import {paramsIn} from "../core.state";
import {actionSharedNetworkTypeSelected} from "./shared.actions";

const initialSharedState: SharedState = {
  defaultNetworkType: null,
};

const reducerX = createReducer(
  initialSharedState,
  on(
    routerNavigatedAction,
    (state, action) => {
      const params = paramsIn(action.payload.routerState.root);
      const networkType = params.get("networkType");
      if (networkType) {
        return {...state, defaultNetworkType: networkType};
      }
      return state;
    }
  ),
  on(
    actionSharedNetworkTypeSelected,
    (state, action) => ({...state, defaultNetworkType: action.networkType})
  )
);

export function sharedReducer(
  state: SharedState | undefined,
  action: Action
) {
  return reducerX(state, action);
}
