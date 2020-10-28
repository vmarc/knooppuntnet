import {ActionReducerMap, createFeatureSelector, MetaReducer} from "@ngrx/store";
import {demoReducer} from "./demo/demo.reducer";
import {DemoState} from "./demo/demo.model";
import {RouterStateUrl} from "./router/router.state";
import {RouterReducerState} from "@ngrx/router-store";
import {routerReducer} from "@ngrx/router-store";
import * as fromRouter from '@ngrx/router-store';

export const reducers: ActionReducerMap<AppState> = {
  demo: demoReducer,
  router: routerReducer
};

export const metaReducers: MetaReducer<AppState>[] = [];

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


export interface AppState {
  demo: DemoState;
  router: RouterReducerState<RouterStateUrl>;
}
