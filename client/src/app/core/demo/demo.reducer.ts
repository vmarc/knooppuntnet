import {createReducer, on} from '@ngrx/store';
import {DemoState} from './demo.state';
import {actionDemoPause} from "./demo.actions";
import {actionDemoStartVideo} from "./demo.actions";
import {actionDemoPlayingChanged} from "./demo.actions";
import {actionDemoEnabledChanged} from "./demo.actions";
import {actionDemoCanPlay} from "./demo.actions";
import {actionDemoTimeUpdate} from "./demo.actions";

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
    actionDemoPause,
    (state, action) => ({...state, ...action})
  ),
  on(
    actionDemoStartVideo,
    (state, {video}) => ({
      ...state,
      currentVideo: video
    })
  ),
  on(
    actionDemoPlayingChanged,
    (state, {playing}) => ({
      ...state,
      playing,
      videoPlayButtonEnabled: false
    })
  ),
  on(
    actionDemoEnabledChanged,
    (state, {enabled}) => ({
      ...state,
      enabled
    })
  ),
  on(
    actionDemoCanPlay,
    (state, {duration}) => ({
      ...state,
      duration,
      videoPlayButtonEnabled: true
    })
  ),
  on(
    actionDemoTimeUpdate,
    (state, {time}) => ({
      ...state,
      time
    })
  )
);
