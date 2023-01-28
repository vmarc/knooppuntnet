import { routerNavigationAction } from '@ngrx/router-store';
import { on } from '@ngrx/store';
import { createReducer } from '@ngrx/store';
import { RoutingUtil } from '../../../base/routing-util';
import { actionPreferencesAnalysisStrategy } from '../../../core/preferences/preferences.actions';
import { AnalysisStrategy } from '../../../core/preferences/preferences.state';
import { actionChangesPageSize } from './changes.actions';
import { actionChangesImpact } from './changes.actions';
import { actionChangesFilterOption } from './changes.actions';
import { actionChangesPageLoaded } from './changes.actions';
import { actionChangesPageIndex } from './changes.actions';
import { actionChangesPageInit } from './changes.actions';
import { ChangesState } from './changes.state';
import { initialState } from './changes.state';

export const changesReducer = createReducer<ChangesState>(
  initialState,
  on(routerNavigationAction, (state, action): ChangesState => {
    const util = new RoutingUtil(action);

    if (util.isChangesPage()) {
      const queryParams = action.payload.routerState.root.queryParams;
      let strategy: AnalysisStrategy;
      if ('network' === queryParams['strategy']) {
        strategy = AnalysisStrategy.network;
      } else if ('location' === queryParams['strategy']) {
        strategy = AnalysisStrategy.location;
      }

      const changesParameters = util.changesParameters();

      return {
        ...state,
        // TODO strategy,
        changesParameters,
      };
    } else {
      return {
        ...state,
        changesPage: null,
      };
    }
  }),
  on(
    actionChangesImpact,
    (state, action): ChangesState => ({
      ...state,
      changesParameters: {
        ...state.changesParameters,
        impact: action.impact,
        pageIndex: 0,
      },
    })
  ),
  on(
    actionChangesPageSize,
    (state, action): ChangesState => ({
      ...state,
      changesParameters: {
        ...state.changesParameters,
        pageSize: action.pageSize,
        pageIndex: 0,
      },
    })
  ),
  on(
    actionChangesPageIndex,
    (state, action): ChangesState => ({
      ...state,
      changesParameters: {
        ...state.changesParameters,
        pageIndex: action.pageIndex,
      },
    })
  ),
  on(
    actionChangesFilterOption,
    (state, action): ChangesState => ({
      ...state,
      changesParameters: {
        ...state.changesParameters,
        year: action.option.year,
        month: action.option.month,
        day: action.option.day,
        impact: action.option.impact,
        pageIndex: 0,
      },
    })
  ),
  on(
    actionPreferencesAnalysisStrategy,
    (state, action): ChangesState => ({
      ...state,
      // TODO strategy: action.strategy,
      changesParameters: {
        ...state.changesParameters,
        pageIndex: 0,
      },
    })
  ),
  on(
    actionChangesPageInit,
    (state): ChangesState => ({
      ...state,
      // TODO pageIndex: 0,
    })
  ),
  on(
    actionChangesPageIndex,
    (state, { pageIndex }): ChangesState => ({
      ...state,
      // TODO pageIndex,
    })
  ),
  on(
    actionChangesPageLoaded,
    (state, response): ChangesState => ({
      ...state,
      changesPage: response,
    })
  )
);
