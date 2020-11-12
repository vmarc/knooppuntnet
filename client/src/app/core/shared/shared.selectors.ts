import {createSelector} from "@ngrx/store";
import {selectSharedState} from "../core.state";
import {SharedState} from "./shared.model";

export const selectSharedDefaultNetworkType = createSelector(
  selectSharedState,
  (state: SharedState) => state.defaultNetworkType
);
