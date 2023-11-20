import { Params } from '@angular/router';
import { NetworkType } from '@api/custom';
import { MapPosition } from '@app/ol/domain';
import { BrowserStorageService } from '@app/services';
import { PlannerState } from '../../store/planner-state';

describe('PlannerStateService', () => {
  // const defaultState: PlannerState = {
  //   networkType: NetworkType.hiking,
  //   position: new MapPosition(14, 712444.7410769509, 6830625.472668002, 0),
  //   mapMode: 'surface',
  //   resultMode: 'compact',
  //   layerStates: [],
  //   poiLayerStates: [
  //     { layerName: 'hiking-biking', visible: true, enabled: true },
  //     { layerName: 'landmarks', visible: true, enabled: true },
  //     { layerName: 'restaurants', visible: true, enabled: true },
  //     { layerName: 'places-to-stay', visible: true, enabled: true },
  //     { layerName: 'tourism', visible: true, enabled: true },
  //     { layerName: 'amenity', visible: false, enabled: true },
  //     { layerName: 'shops', visible: false, enabled: true },
  //     { layerName: 'foodshops', visible: false, enabled: true },
  //     { layerName: 'sports', visible: false, enabled: true },
  //   ],
  // };
  //
  // const storage: Storage = {
  //   getItem: jest.fn(),
  //   key: jest.fn(),
  //   length: 0,
  //   removeItem: jest.fn(),
  //   setItem: jest.fn(),
  //   clear: jest.fn(),
  // };
  // const browserStorageService = new BrowserStorageService(storage);
  // // const plannerStateService = new PlannerStateService(browserStorageService);

  it('toQueryParams', () => {
    const state: PlannerState = {
      networkType: NetworkType.hiking,
      position: new MapPosition(12, 1, 2, 0),
      mapMode: 'survey',
      resultMode: 'detailed',
      layerStates: [
        { layerName: 'layer-1', visible: true, enabled: true },
        { layerName: 'layer-2', visible: true, enabled: true },
        { layerName: 'layer-3', visible: false, enabled: true },
      ],
      poiLayerStates: [
        { layerName: 'poi-layer-1', visible: true, enabled: true },
        { layerName: 'poi-layer-2', visible: true, enabled: true },
        { layerName: 'poi-layer-3', visible: false, enabled: true },
      ],
    };

    // const params = plannerStateService.toQueryParams(state);
    //
    // expect(params['mode']).toEqual('survey');
    // expect(params['position']).toEqual('0.00001797,0.00000898,12');
    // expect(params['result']).toEqual('detailed');
    // expect(params['layers']).toEqual('layer-1,layer-2');
    // expect(params['pois']).toEqual('true');
    // expect(params['poi-layers']).toEqual('poi-layer-1,poi-layer-2');
  });

  it('default planner state', () => {
    const routeParams: Params = [];
    const queryParams: Params = [];
    // const state = plannerStateService.toPlannerState(routeParams, queryParams);
    // expect(state).toEqual(defaultState);
  });

  it('networkType', () => {
    const routeParams: Params = {
      networkType: 'cycling',
    };
    const queryParams: Params = {};
    // const state = plannerStateService.toPlannerState(routeParams, queryParams);
    // const expectedState = {
    //   ...defaultState,
    //   networkType: NetworkType.cycling,
    // };
    // expect(state).toEqual(expectedState);
  });

  it('networkType invalid value', () => {
    const routeParams: Params = {
      networkType: 'unknown',
    };
    const queryParams: Params = {};
    // const state = plannerStateService.toPlannerState(routeParams, queryParams);
    // expect(state).toEqual(defaultState);
  });

  // it.todo('xxx');
  //
  // it.todo('poi layers from browser storage');
  //
  // it.todo('unknown layer');
  //
  // it.todo('unknown poi layer');
  //
  // it.todo('mapMode invalid value');
  //
  // it.todo('resultMode invalid value');
});
