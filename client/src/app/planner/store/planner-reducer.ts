import { on } from '@ngrx/store';
import { createReducer } from '@ngrx/store';
import { actionPlannerPosition } from './planner-actions';
import { actionPlannerResultMode } from './planner-actions';
import { actionPlannerMapMode } from './planner-actions';
import { actionPlannerLoad } from './planner-actions';
import { initialState } from './planner-state';
import { PlannerState } from './planner-state';

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
