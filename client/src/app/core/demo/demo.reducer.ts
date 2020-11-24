import {createReducer, on} from '@ngrx/store';
import {DemoState} from './demo.state';
import * as DemoActions from './demo.actions';

const initialState: DemoState = {
  video: '',
  enabled: false,
  playing: false,
  time: 0,
  duration: 0,
  videoPlayButtonEnabled: false
};

export const demoReducer = createReducer(
  initialState,
  on(
    DemoActions.pause,
    (state, action) => ({...state, ...action})
  ),
  on(
    DemoActions.startVideo,
    (state, {video}) => ({
      ...state,
      currentVideo: video
    })
  ),
  on(
    DemoActions.playingChanged,
    (state, {playing}) => ({
      ...state,
      playing,
      videoPlayButtonEnabled: false
    })
  ),
  on(
    DemoActions.enabledChanged,
    (state, {enabled}) => ({
      ...state,
      enabled
    })
  ),
  on(
    DemoActions.canPlay,
    (state, {duration}) => ({
      ...state,
      duration,
      videoPlayButtonEnabled: true
    })
  ),
  on(
    DemoActions.timeUpdate,
    (state, {time}) => ({
      ...state,
      time
    })
  )
);
