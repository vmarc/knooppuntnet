import { RouterReducerState } from '@ngrx/router-store';
import { routerReducer } from '@ngrx/router-store';
import { getSelectors } from '@ngrx/router-store';
import { Store } from '@ngrx/store';
import { ActionReducerMap } from '@ngrx/store';
import { createFeatureSelector } from '@ngrx/store';
import { MetaReducer } from '@ngrx/store';
import { ActionReducer } from '@ngrx/store';
import { localStorageSync } from 'ngrx-store-localstorage';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { filter } from 'rxjs/operators';
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

export const localStorageSyncReducer = (
  reducer: ActionReducer<any>
): ActionReducer<any> =>
  localStorageSync({ keys: ['preferences'], rehydrate: true })(reducer);

export const metaReducers: MetaReducer<AppState>[] = [localStorageSyncReducer];

export const selectPreferencesState =
  createFeatureSelector<PreferencesState>('preferences');

export const selectSharedState = createFeatureSelector<SharedState>('shared');

export const selectUserState = createFeatureSelector<UserState>('user');

export const selectRouterState =
  createFeatureSelector<RouterReducerState<RouterStateUrl>>('router');

export const selectPageState = createFeatureSelector<PageState>('page');

export const {
  // selectCurrentRoute,   // select the current route
  selectFragment, // select the current route fragment
  selectQueryParams, // select the current route query params
  selectQueryParam, // factory function to select a query param
  selectRouteParams, // select the current route params
  selectRouteParam, // factory function to select a route param
  // selectRouteData,      // select the current route data
  selectUrl, // select the current url
} = getSelectors(selectRouterState);

export const selectDefined = <K>(
  store: Store<AppState>,
  selector: (state: AppState) => K
): Observable<K> => {
  return store.select(selector).pipe(filter((x) => !!x));
};

export const selectFalse = (
  store: Store<AppState>,
  selector: (state: AppState) => boolean
): Observable<boolean> => {
  return store.select(selector).pipe(map((e) => e === false));
};
