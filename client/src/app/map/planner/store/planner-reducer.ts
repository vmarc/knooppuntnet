import { actionPlannerPosition } from '@app/map/planner/store/planner-actions';
import { actionPlannerResultMode } from '@app/map/planner/store/planner-actions';
import { actionPlannerMapMode } from '@app/map/planner/store/planner-actions';
import { actionPlannerLoad } from '@app/map/planner/store/planner-actions';
import { initialState } from '@app/map/planner/store/planner-state';
import { PlannerState } from '@app/map/planner/store/planner-state';
import { on } from '@ngrx/store';
import { createReducer } from '@ngrx/store';

export const plannerReducer = createReducer<PlannerState>(
  initialState,
  on(actionPlannerLoad, (oldState, state): PlannerState => {
    return state;
  }),
  on(
    actionPlannerMapMode,
    (state, { mapMode }): PlannerState => ({
      ...state,
      mapMode,
    })
  ),
  on(
    actionPlannerResultMode,
    (state, { resultMode }): PlannerState => ({
      ...state,
      resultMode,
    })
  ),
  on(
    actionPlannerPosition,
    (state, position): PlannerState => ({
      ...state,
      position,
    })
  )
);
