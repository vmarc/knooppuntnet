import {Params} from '@angular/router';
import {createFeatureSelector} from '@ngrx/store';
import {createSelector} from '@ngrx/store';
import {selectRouteParams} from '../../core/core.state';
import {DemoRootState} from './demo.state';
import {demoFeatureKey} from './demo.state';
import {DemoState} from './demo.state';
import {VideoState} from './demo.state';

export const selectDemoState = createFeatureSelector<DemoRootState, DemoState>(demoFeatureKey);

export const selectDemo = createSelector(
  selectDemoState,
  (state: DemoState) => state
);

export const selectDemoEnabled = createSelector(
  selectDemo,
  (state: DemoState) => state.enabled
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
  (video: string) => `/videos/en/${video}.mp4`
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
