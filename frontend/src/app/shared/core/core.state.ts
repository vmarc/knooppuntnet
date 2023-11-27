import { getRouterSelectors } from '@ngrx/router-store';
import { RouterReducerState } from '@ngrx/router-store';
import { routerReducer } from '@ngrx/router-store';
import { ActionReducerMap } from '@ngrx/store';
import { MetaReducer } from '@ngrx/store';
import { ActionReducer } from '@ngrx/store';
import { localStorageSync } from 'ngrx-store-localstorage';
import { pageReducer } from './page/page.reducer';
import { PageState } from './page/page.state';
import { preferencesReducer } from './preferences/preferences.reducer';
import { PreferencesState } from './preferences/preferences.state';
import { RouterStateUrl } from './router/router.state';
import { sharedReducer } from './shared/shared.reducer';
import { SharedState } from './shared/shared.state';
import { userReducer } from './user/user.reducer';
import { UserState } from './user/user.state';

export interface AppState {
  preferences: PreferencesState;
  shared: SharedState;
  user: UserState;
  router: RouterReducerState<RouterStateUrl>;
  page: PageState;
}

export const reducers: ActionReducerMap<AppState> = {
  preferences: preferencesReducer,
  shared: sharedReducer,
  user: userReducer,
  router: routerReducer,
  page: pageReducer,
};

export const localStorageSyncReducer = (reducer: ActionReducer<any>): ActionReducer<any> =>
  localStorageSync({ keys: ['preferences'], rehydrate: true })(reducer);

export const metaReducers: MetaReducer<AppState>[] = [localStorageSyncReducer];

export const {
  // selectCurrentRoute,   // select the current route
  selectFragment, // select the current route fragment
  selectQueryParams, // select the current route query params
  selectQueryParam, // factory function to select a query param
  selectRouteParams, // select the current route params
  selectRouteParam, // factory function to select a route param
  // selectRouteData,      // select the current route data
  selectUrl, // select the current url
} = getRouterSelectors();
