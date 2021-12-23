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

export const demoFeatureKey = 'demo';
