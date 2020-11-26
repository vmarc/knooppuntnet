import {createAction, props} from '@ngrx/store';

export const actionDemoVideoPlayerAvailable = createAction(
  '[Demo] Video player available'
);

export const actionDemoCanPlay = createAction(
  '[Demo] Can play',
  props<{ duration: number }>()
);

export const actionDemoTimeUpdate = createAction(
  '[Demo] Time update',
  props<{ time: number }>()
);

export const actionDemoUpdateProgress = createAction(
  '[Demo] Update progress',
  props<{ progress: number }>()
);

export const actionDemoStartVideo = createAction(
  '[Demo] Start video',
  props<{ video: string }>()
);

export const actionDemoPlay = createAction(
  '[Demo] Play'
);

export const actionDemoPause = createAction(
  '[Demo] Pause'
);

export const actionDemoControlPlay = createAction(
  '[Demo] Control play',
  props<{ video: string }>()
);

export const actionDemoPlayingChanged = createAction(
  '[Demo] Playing changed',
  props<{ playing: boolean }>()
);

export const actionDemoEnabledChanged = createAction(
  '[Demo] Enabled changed',
  props<{ enabled: boolean }>()
);

export const actionDemoEnd = createAction(
  '[Demo] End'
);
