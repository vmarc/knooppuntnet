import {RouterReducerState} from '@ngrx/router-store';
import {routerReducer} from '@ngrx/router-store';
import {getSelectors} from '@ngrx/router-store';
import {ActionReducerMap} from '@ngrx/store';
import {createFeatureSelector} from '@ngrx/store';
import {MetaReducer} from '@ngrx/store';
import {ActionReducer} from '@ngrx/store';
import {localStorageSync} from 'ngrx-store-localstorage';
import {locationReducer} from './analysis/location/location.reducer';
import {LocationState} from './analysis/location/location.state';
import {networkReducer} from './analysis/network/network.reducer';
import {NetworkState} from './analysis/network/network.state';
import {nodeReducer} from './analysis/node/node.reducer';
import {NodeState} from './analysis/node/node.state';
import {routeReducer} from './analysis/route/route.reducer';
import {RouteState} from './analysis/route/route.state';
import {subsetReducer} from './analysis/subset/subset.reducer';
import {SubsetState} from './analysis/subset/subset.state';
import {demoReducer} from './demo/demo.reducer';
import {DemoState} from './demo/demo.state';
import {longDistanceReducer} from './longdistance/long-distance.reducer';
import {LongDistanceState} from './longdistance/long-distance.state';
import {preferencesReducer} from './preferences/preferences.reducer';
import {PreferencesState} from './preferences/preferences.state';
import {RouterStateUrl} from './router/router.state';
import {sharedReducer} from './shared/shared.reducer';
import {SharedState} from './shared/shared.state';

export interface AppState {
  preferences: PreferencesState;
  shared: SharedState;
  demo: DemoState;
  node: NodeState;
  route: RouteState;
  network: NetworkState;
  subset: SubsetState;
  location: LocationState;
  longDistance: LongDistanceState;
  router: RouterReducerState<RouterStateUrl>;
}

export const reducers: ActionReducerMap<AppState> = {
  preferences: preferencesReducer,
  shared: sharedReducer,
  demo: demoReducer,
  node: nodeReducer,
  route: routeReducer,
  network: networkReducer,
  subset: subsetReducer,
  location: locationReducer,
  longDistance: longDistanceReducer,
  router: routerReducer
};

export function localStorageSyncReducer(reducer: ActionReducer<any>): ActionReducer<any> {
  return localStorageSync({keys: ['preferences'], rehydrate: true})(reducer);
}

export const metaReducers: MetaReducer<AppState>[] = [localStorageSyncReducer];

export const selectPreferencesState = createFeatureSelector<AppState, PreferencesState>('preferences');

export const selectSharedState = createFeatureSelector<AppState, SharedState>('shared');

export const selectDemoState = createFeatureSelector<AppState, DemoState>('demo');

export const selectNodeState = createFeatureSelector<AppState, NodeState>('node');

export const selectRouteState = createFeatureSelector<AppState, RouteState>('route');

export const selectNetworkState = createFeatureSelector<AppState, NetworkState>('network');

export const selectSubsetState = createFeatureSelector<AppState, SubsetState>('subset');

export const selectLocationState = createFeatureSelector<AppState, LocationState>('location');

export const selectLongDistanceState = createFeatureSelector<AppState, LongDistanceState>('longDistance');

export const selectRouterState = createFeatureSelector<AppState, RouterReducerState<RouterStateUrl>>('router');

export const {
  // selectCurrentRoute,   // select the current route
  // selectFragment,       // select the current route fragment
  // selectQueryParams,    // select the current route query params
  // selectQueryParam,     // factory function to select a query param
  selectRouteParams,    // select the current route params
  // selectRouteParam,     // factory function to select a route param
  // selectRouteData,      // select the current route data
  selectUrl,            // select the current url
} = getSelectors(selectRouterState);
