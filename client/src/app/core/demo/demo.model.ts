import {AppState} from "../core.state";

export interface DemoState {
  playing: Boolean;
  currentVideo: string;
  progress: number;
}

export interface State extends AppState {
  demo: DemoState;
}
