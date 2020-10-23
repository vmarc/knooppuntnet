import {Action, createReducer, on} from "@ngrx/store";
import {DemoState} from "./demo.model";
import {actionDemoPause, actionDemoPlay} from "./demo.actions";

export const initialState: DemoState = {
  playing: false,
  currentVideo: "",
  progress: 0,
};

const reducer = createReducer(
  initialState,
  on(
    actionDemoPause,
    (state, action) => ({...state, ...action})
  ),
  on(
    actionDemoPlay,
    (state, {video}) => ({
      ...state,
      currentVideo: video
    })
  )
);

export function demoReducer(
  state: DemoState | undefined,
  action: Action
) {
  return reducer(state, action);
}
