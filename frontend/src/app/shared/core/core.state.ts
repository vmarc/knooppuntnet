import { getRouterSelectors } from '@ngrx/router-store';
import { RouterReducerState } from '@ngrx/router-store';
import { routerReducer } from '@ngrx/router-store';
import { ActionReducerMap } from '@ngrx/store';
import { pageReducer } from './page/page.reducer';
import { PageState } from './page/page.state';
import { RouterStateUrl } from './router/router.state';
import { sharedReducer } from './shared/shared.reducer';
import { SharedState } from './shared/shared.state';

export interface AppState {
  shared: SharedState;
  router: RouterReducerState<RouterStateUrl>;
  page: PageState;
}

export const reducers: ActionReducerMap<AppState> = {
  shared: sharedReducer,
  router: routerReducer,
  page: pageReducer,
};

export const {
  selectFragment, // select the current route fragment
  selectQueryParams, // select the current route query params
  selectQueryParam, // factory function to select a query param
  selectRouteParams, // select the current route params
  selectRouteParam, // factory function to select a route param
} = getRouterSelectors();
