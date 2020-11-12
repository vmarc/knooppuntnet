import {ActionReducerMap, createFeatureSelector, MetaReducer} from "@ngrx/store";
import {demoReducer} from "./demo/demo.reducer";
import {DemoState} from "./demo/demo.model";
import {RouterStateUrl} from "./router/router.state";
import {RouterReducerState} from "@ngrx/router-store";
import {routerReducer} from "@ngrx/router-store";
import * as fromRouter from '@ngrx/router-store';
import {SharedState} from "./shared/shared.model";
import {sharedReducer} from "./shared/shared.reducer";
import {localStorageSync} from "ngrx-store-localstorage";
import {ActionReducer} from "@ngrx/store";
import {Map} from "immutable";
import {ActivatedRouteSnapshot} from "@angular/router";

export const reducers: ActionReducerMap<AppState> = {
  shared: sharedReducer,
  demo: demoReducer,
  router: routerReducer
};

export function localStorageSyncReducer(reducer: ActionReducer<any>): ActionReducer<any> {
  return localStorageSync({keys: ["shared"], rehydrate: true})(reducer);
}

export const metaReducers: MetaReducer<AppState>[] = [localStorageSyncReducer];

export const selectSharedState = createFeatureSelector<AppState, SharedState>("shared");

export const selectDemoState = createFeatureSelector<AppState, DemoState>("demo");

export const selectRouterState = createFeatureSelector<AppState, RouterReducerState<RouterStateUrl>>("router");

export const {
  selectCurrentRoute,   // select the current route
  selectFragment,       // select the current route fragment
  selectQueryParams,    // select the current route query params
  selectQueryParam,     // factory function to select a query param
  selectRouteParams,    // select the current route params
  selectRouteParam,     // factory function to select a route param
  selectRouteData,      // select the current route data
  selectUrl,            // select the current url
} = fromRouter.getSelectors(selectRouterState);

export function paramsIn(routeSnapshot: ActivatedRouteSnapshot): Map<string, string> {
  let route = routeSnapshot;
  let params = Map(Object.keys(route.params).map(key => [key, route.params[key]]));
  while (route.firstChild) {
    route = route.firstChild;
    Object.keys(route.params).forEach(key => params = params.set(key, route.params[key]));
  }
  return params;
}

export interface AppState {
  shared: SharedState,
  demo: DemoState;
  router: RouterReducerState<RouterStateUrl>;
}
