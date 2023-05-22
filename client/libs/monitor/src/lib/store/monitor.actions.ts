import { createAction } from '@ngrx/store';
import { props } from '@ngrx/store';

export const actionMonitorRouteInfo = createAction(
  '[Monitor] Route info',
  props<{ relationId: number | null }>()
);

export const actionMonitorRouteAdminRelationIdChanged = createAction(
  '[Monitor] Relation id changed'
);
