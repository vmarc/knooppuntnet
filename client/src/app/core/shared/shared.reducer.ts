import {createReducer} from "@ngrx/store";
import {SharedState} from "./shared.model";

const initialSharedState: SharedState = {};

export const sharedReducer = createReducer(
  initialSharedState
);
