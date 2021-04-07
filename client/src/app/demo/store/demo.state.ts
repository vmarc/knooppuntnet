import { AppState } from '../../core/core.state';

export class VideoState {
  constructor(public video: string, public playing: boolean) {}
}

export interface DemoState {
  video: string;
  enabled: boolean;
  playing: boolean;
  time: number;
  duration: number;
  videoPlayButtonEnabled: boolean;
}

export interface State extends AppState {
  demo: DemoState;
}

export const demoFeatureKey = 'demo';

export interface DemoRootState extends AppState {
  [demoFeatureKey]: DemoState;
}
