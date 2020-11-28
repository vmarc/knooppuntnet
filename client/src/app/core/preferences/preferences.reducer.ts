import {routerNavigatedAction} from '@ngrx/router-store';
import {createReducer} from '@ngrx/store';
import {on} from '@ngrx/store';
import {Util} from '../../components/shared/util';
import {actionPreferencesNetworkType} from './preferences.actions';
import {actionPreferencesInstructions} from './preferences.actions';
import {actionPreferencesExtraLayers} from './preferences.actions';
import {initialState} from './preferences.state';

export const preferencesReducer = createReducer(
  initialState,
  on(
    routerNavigatedAction,
    (state, action) => {
      const params = Util.paramsIn(action.payload.routerState.root);
      const networkType = params.get('networkType');
      if (networkType) {
        return {...state, networkType};
      }
      return state;
    }
  ),
  on(
    actionPreferencesNetworkType,
    (state, action) => ({...state, networkType: action.networkType})
  ),
  on(
    actionPreferencesInstructions,
    (state, action) => ({...state, instructions: action.instructions})
  ),
  on(
    actionPreferencesExtraLayers,
    (state, action) => ({...state, extraLayers: action.extraLayers})
  )
);
