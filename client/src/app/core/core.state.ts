import {ActionReducerMap, createFeatureSelector, MetaReducer} from '@ngrx/store';
import {ActionReducer} from '@ngrx/store';
import {demoReducer} from './demo/demo.reducer';
import {DemoState} from './demo/demo.model';
import {RouterStateUrl} from './router/router.state';
import {RouterReducerState} from '@ngrx/router-store';
import {routerReducer} from '@ngrx/router-store';
import * as fromRouter from '@ngrx/router-store';
import {SharedState} from './shared/shared.model';
import {sharedReducer} from './shared/shared.reducer';
import {localStorageSync} from 'ngrx-store-localstorage';
import {PreferencesState} from './preferences/preferences.model';
import {preferencesReducer} from './preferences/preferences.reducer';

export const reducers: ActionReducerMap<AppState> = {
  preferences: preferencesReducer,
  shared: sharedReducer,
  demo: demoReducer,
  router: routerReducer
};

export function localStorageSyncReducer(reducer: ActionReducer<any>): ActionReducer<any> {
  return localStorageSync({keys: ['preferences'], rehydrate: true})(reducer);
}

export const metaReducers: MetaReducer<AppState>[] = [localStorageSyncReducer];

export const selectPreferencesState = createFeatureSelector<AppState, PreferencesState>('preferences');

export const selectSharedState = createFeatureSelector<AppState, SharedState>('shared');

export const selectDemoState = createFeatureSelector<AppState, DemoState>('demo');

export const selectRouterState = createFeatureSelector<AppState, RouterReducerState<RouterStateUrl>>('router');

export const {
  // selectCurrentRoute,   // select the current route
  // selectFragment,       // select the current route fragment
  // selectQueryParams,    // select the current route query params
  // selectQueryParam,     // factory function to select a query param
  selectRouteParams,    // select the current route params
  // selectRouteParam,     // factory function to select a route param
  // selectRouteData,      // select the current route data
  // selectUrl,            // select the current url
} = fromRouter.getSelectors(selectRouterState);

export interface AppState {
  preferences: PreferencesState;
  shared: SharedState;
  demo: DemoState;
  router: RouterReducerState<RouterStateUrl>;
}
