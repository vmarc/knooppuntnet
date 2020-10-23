import {createAction, props} from "@ngrx/store";

export const actionDemoPlay = createAction(
  '[Demo] Play',
  props<{ video: string }>()
);

export const actionDemoPause = createAction(
  '[Demo] Pause',
  props<{ video: string /* remove parameter? */ }>()
);
