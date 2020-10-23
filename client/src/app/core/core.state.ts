import {ActionReducerMap, createFeatureSelector, MetaReducer} from "@ngrx/store";
import {demoReducer} from "./demo/demo.reducer";
import {DemoState} from "./demo/demo.model";

export const reducers: ActionReducerMap<AppState> = {
  demo: demoReducer
};

export const metaReducers: MetaReducer<AppState>[] = [];

export const selectDemoState = createFeatureSelector<AppState, DemoState>("demo");

export interface AppState {
  demo: DemoState;
}
