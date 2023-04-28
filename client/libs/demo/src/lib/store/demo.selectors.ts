import { Params } from '@angular/router';
import { selectRouteParams } from '@app/core';
import { createFeatureSelector } from '@ngrx/store';
import { createSelector } from '@ngrx/store';
import { demoFeatureKey } from './demo.state';
import { DemoState } from './demo.state';

export const selectDemoState = createFeatureSelector<DemoState>(demoFeatureKey);

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
  (params: Params) => params?.['video']
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
