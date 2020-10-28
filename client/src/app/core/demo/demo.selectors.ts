import {createSelector} from "@ngrx/store";
import {DemoState} from "./demo.model";
import {VideoState} from "./demo.model";
import {selectDemoState} from "../core.state";
import {selectRouteParams} from "../core.state";
import {Params} from "@angular/router";

export const selectDemo = createSelector(
  selectDemoState,
  (state: DemoState) => state
);

export const selectDemoPlaying = createSelector(
  selectDemo,
  (state: DemoState) => state.playing
);

export const selectDemoVideo = createSelector(
  selectRouteParams,
  (params: Params) => params?.video
);

export const selectDemoVideoSource = createSelector(
  selectDemoVideo,
  (video: string) => `http://knooppuntnet.nl/public/${video}.mp4`
);

export const selectCurrentVideoState = createSelector(
  selectDemoVideo,
  selectDemoPlaying,
  (video: string, playing: boolean) => new VideoState(video, playing)
);

export const selectDemoVideoPlayButtonEnabled = createSelector(
  selectDemo,
  (state: DemoState) => state.videoPlayButtonEnabled
);

export const selectDemoTime = createSelector(
  selectDemo,
  (state: DemoState) => state.time
);

export const selectDemoDuration = createSelector(
  selectDemo,
  (state: DemoState) => state.duration
);

export const selectDemoProgress = createSelector(
  selectDemoTime,
  selectDemoDuration,
  (time: number, duration: number) => {
    if (duration > 0) {
      return time / duration;
    }
    return 0;
  }
);
