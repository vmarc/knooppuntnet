import { on } from '@ngrx/store';
import { createReducer } from '@ngrx/store';
import { actionSubsetChangesPageInit } from './subset.actions';
import { actionSubsetMapPageInit } from './subset.actions';
import { actionSubsetOrphanRoutesPageInit } from './subset.actions';
import { actionSubsetOrphanNodesPageInit } from './subset.actions';
import { actionSubsetFactsPageInit } from './subset.actions';
import { actionSubsetNetworksPageInit } from './subset.actions';
import { actionSubsetMapPageLoad } from './subset.actions';
import { actionSubsetOrphanRoutesPageLoad } from './subset.actions';
import { actionSubsetFactsPageLoad } from './subset.actions';
import { actionSubsetOrphanNodesPageLoad } from './subset.actions';
import { actionSubsetNetworksPageLoad } from './subset.actions';
import { actionSubsetFactDetailsPageLoad } from './subset.actions';
import { actionSubsetFactDetailsPageLoaded } from './subset.actions';
import { actionSubsetChangesPageLoad } from './subset.actions';
import { actionSubsetChangesPageImpact } from './subset.actions';
import { actionSubsetChangesPageSize } from './subset.actions';
import { actionSubsetChangesPageIndex } from './subset.actions';
import { actionSubsetChangesFilterOption } from './subset.actions';
import { actionSubsetNetworksPageLoaded } from './subset.actions';
import { actionSubsetFactsPageLoaded } from './subset.actions';
import { actionSubsetOrphanNodesPageLoaded } from './subset.actions';
import { actionSubsetOrphanRoutesPageLoaded } from './subset.actions';
import { actionSubsetMapPageLoaded } from './subset.actions';
import { actionSubsetChangesPageLoaded } from './subset.actions';
import { initialState } from './subset.state';

export const subsetReducer = createReducer(
  initialState,
  on(
    actionSubsetNetworksPageInit,
    actionSubsetFactsPageInit,
    actionSubsetOrphanNodesPageInit,
    actionSubsetOrphanRoutesPageInit,
    actionSubsetMapPageInit,
    actionSubsetChangesPageInit,
    (state, action) => {
      return {
        ...state,
        networksPage: null,
        factsPage: null,
        subsetFact: null,
        factDetailsPage: null,
        orphanNodesPage: null,
        orphanRoutesPage: null,
        mapPage: null,
        changesPage: null,
        changesParameters: null,
      };
    }
  ),
  on(
    actionSubsetNetworksPageLoad,
    actionSubsetFactsPageLoad,
    actionSubsetOrphanNodesPageLoad,
    actionSubsetOrphanRoutesPageLoad,
    actionSubsetMapPageLoad,
    (state, { subset }) => ({
      ...state,
      subset,
    })
  ),
  on(actionSubsetNetworksPageLoaded, (state, { response }) => ({
    ...state,
    subsetInfo: response.result.subsetInfo,
    networksPage: response,
  })),
  on(actionSubsetFactsPageLoaded, (state, { response }) => ({
    ...state,
    subsetInfo: response.result.subsetInfo,
    factsPage: response,
  })),
  on(actionSubsetFactDetailsPageLoad, (state, { subsetFact }) => ({
    ...state,
    subsetFact,
    subset: subsetFact.subset,
  })),
  on(actionSubsetFactDetailsPageLoaded, (state, { response }) => ({
    ...state,
    subsetInfo: response.result.subsetInfo,
    factDetailsPage: response,
  })),
  on(actionSubsetOrphanNodesPageLoaded, (state, { response }) => ({
    ...state,
    subsetInfo: response.result.subsetInfo,
    orphanNodesPage: response,
  })),
  on(actionSubsetOrphanRoutesPageLoaded, (state, { response }) => ({
    ...state,
    subsetInfo: response.result.subsetInfo,
    orphanRoutesPage: response,
  })),
  on(actionSubsetMapPageLoaded, (state, { response }) => ({
    ...state,
    subsetInfo: response.result.subsetInfo,
    mapPage: response,
  })),
  on(actionSubsetChangesPageLoad, (state, { subset, changesParameters }) => ({
    ...state,
    subset,
    changesParameters,
  })),
  on(actionSubsetChangesPageLoaded, (state, { response }) => ({
    ...state,
    subsetInfo: response.result.subsetInfo,
    changesPage: response,
  })),
  on(actionSubsetChangesFilterOption, (state, action) => ({
    ...state,
    changesParameters: {
      ...state.changesParameters,
      year: action.option.year,
      month: action.option.month,
      day: action.option.day,
      impact: action.option.impact,
      pageIndex: 0,
    },
  })),
  on(actionSubsetChangesPageImpact, (state, action) => ({
    ...state,
    changesParameters: {
      ...state.changesParameters,
      impact: action.impact,
      pageIndex: 0,
    },
  })),
  on(actionSubsetChangesPageSize, (state, action) => ({
    ...state,
    changesParameters: {
      ...state.changesParameters,
      pageSize: action.pageSize,
      pageIndex: 0,
    },
  })),
  on(actionSubsetChangesPageIndex, (state, action) => ({
    ...state,
    changesParameters: {
      ...state.changesParameters,
      pageIndex: action.pageIndex,
    },
  }))
);
