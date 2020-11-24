import {createReducer} from '@ngrx/store';
import {on} from '@ngrx/store';
import {routerNavigatedAction} from '@ngrx/router-store';
import {initialState} from './preferences.state';
import * as PreferencesActions from './preferences.actions';
import {Util} from '../../components/shared/util';

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
    PreferencesActions.networkType,
    (state, action) => ({...state, networkType: action.networkType})
  ),
  on(
    PreferencesActions.instructions,
    (state, action) => ({...state, instructions: action.instructions})
  ),
  on(
    PreferencesActions.extraLayers,
    (state, action) => ({...state, extraLayers: action.extraLayers})
  )
);
