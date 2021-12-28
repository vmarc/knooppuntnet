import { SubsetChangesPage } from '@api/common/subset/subset-changes-page';
import { ApiResponse } from '@api/custom/api-response';
import { Subset } from '@api/custom/subset';
import { routerNavigatedAction } from '@ngrx/router-store';
import { routerNavigationAction } from '@ngrx/router-store';
import { on } from '@ngrx/store';
import { createReducer } from '@ngrx/store';
import { RoutingUtil } from '../../../base/routing-util';
import { Util } from '../../../components/shared/util';
import { Countries } from '../../../kpn/common/countries';
import { NetworkTypes } from '../../../kpn/common/network-types';
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
  on(routerNavigationAction, (state, action) => {
    const util = new RoutingUtil(action);
    let changesPage: ApiResponse<SubsetChangesPage> = null;
    if (util.isSubsetChangesPage()) {
      changesPage = state.changesPage;
    }

    return {
      ...state,
      networksPage: null,
      factsPage: null,
      orphanNodesPage: null,
      orphanRoutesPage: null,
      mapPage: null,
      changesPage,
    };
  }),
  on(routerNavigatedAction, (state, action) => {
    const params = Util.paramsIn(action.payload.routerState.root);
    const country = Countries.withDomain(params.get('country'));
    const networkType = NetworkTypes.withName(params.get('networkType'));
    if (networkType && country) {
      const subset: Subset = {
        country,
        networkType,
      };
      return { ...state, subset };
    }
    return state;
  }),
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
  }))
);
