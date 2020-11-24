import {createAction, props} from '@ngrx/store';

export const videoPlayerAvailable = createAction(
  '[Demo] Video player available'
);

export const canPlay = createAction(
  '[Demo] Can play',
  props<{ duration: number }>()
);

export const timeUpdate = createAction(
  '[Demo] Time update',
  props<{ time: number }>()
);

export const updateProgress = createAction(
  '[Demo] Update progress',
  props<{ progress: number }>()
);

export const startVideo = createAction(
  '[Demo] Start video',
  props<{ video: string }>()
);

export const play = createAction(
  '[Demo] Play'
);

export const pause = createAction(
  '[Demo] Pause'
);

export const controlPlay = createAction(
  '[Demo] Control play',
  props<{ video: string }>()
);

export const playingChanged = createAction(
  '[Demo] Playing changed',
  props<{ playing: boolean }>()
);

export const enabledChanged = createAction(
  '[Demo] Enabled changed',
  props<{ enabled: boolean }>()
);

export const end = createAction(
  '[Demo] End'
);
