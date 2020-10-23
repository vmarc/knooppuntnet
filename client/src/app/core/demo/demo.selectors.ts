import {createSelector} from "@ngrx/store";
import {DemoState} from "./demo.model";
import {selectDemoState} from "../core.state";

export const selectDemo = createSelector(
  selectDemoState,
  (state: DemoState) => state
);

export const selectDemoProgress = createSelector(
  selectDemo,
  (state: DemoState) => state.progress
);

export const selectDemoPlaying = createSelector(
  selectDemo,
  (state: DemoState) => state.playing
);

export const selectDemoCurrentVideo = createSelector(
  selectDemo,
  (state: DemoState) => state.currentVideo
);
