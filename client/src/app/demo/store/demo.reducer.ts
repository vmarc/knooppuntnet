import { createReducer } from '@ngrx/store';
import { on } from '@ngrx/store';
import { actionDemoPause } from './demo.actions';
import { actionDemoStartVideo } from './demo.actions';
import { actionDemoPlayingChanged } from './demo.actions';
import { actionDemoEnabledChanged } from './demo.actions';
import { actionDemoCanPlay } from './demo.actions';
import { actionDemoTimeUpdate } from './demo.actions';
import { DemoState } from './demo.state';

const initialState: DemoState = {
  video: '',
  enabled: false,
  playing: false,
  time: 0,
  duration: 0,
  videoPlayButtonEnabled: false,
};

export const demoReducer = createReducer<DemoState>(
  initialState,
  on(actionDemoPause, (state, action): DemoState => ({ ...state, ...action })),
  on(
    actionDemoStartVideo,
    (state, { video }): DemoState => ({
      ...state,
      currentVideo: video,
    })
  ),
  on(
    actionDemoPlayingChanged,
    (state, { playing }): DemoState => ({
      ...state,
      playing,
      videoPlayButtonEnabled: false,
    })
  ),
  on(
    actionDemoEnabledChanged,
    (state, { enabled }): DemoState => ({
      ...state,
      enabled,
    })
  ),
  on(
    actionDemoCanPlay,
    (state, { duration }): DemoState => ({
      ...state,
      duration,
      videoPlayButtonEnabled: true,
    })
  ),
  on(
    actionDemoTimeUpdate,
    (state, { time }): DemoState => ({
      ...state,
      time,
    })
  )
);
