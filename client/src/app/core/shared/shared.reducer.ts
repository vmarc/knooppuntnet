import {Action, createReducer} from "@ngrx/store";
import {SharedState} from "./shared.model";

const initialSharedState: SharedState = {};

const reducer = createReducer(
  initialSharedState
);

export function sharedReducer(
  state: SharedState | undefined,
  action: Action
) {
  return reducer(state, action);
}
