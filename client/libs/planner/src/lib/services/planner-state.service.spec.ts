import { Params } from '@angular/router';
import { NetworkType } from '@api/custom';
import { MapPosition } from '@app/components/ol/domain';
import { BrowserStorageService } from '@app/services';
import { PlannerState } from '../store/planner-state';
import { PlannerStateService } from './planner-state.service';

describe('PlannerStateService', () => {
  const defaultState: PlannerState = {
    networkType: NetworkType.hiking,
    position: new MapPosition(14, 712444.7410769509, 6830625.472668002, 0),
    mapMode: 'surface',
    resultMode: 'compact',
    layerStates: [],
    pois: false,
    poiLayerStates: [
      { layerName: 'hiking-biking', visible: true },
      { layerName: 'landmarks', visible: true },
      { layerName: 'restaurants', visible: true },
      { layerName: 'places-to-stay', visible: true },
      { layerName: 'tourism', visible: true },
      { layerName: 'amenity', visible: false },
      { layerName: 'shops', visible: false },
      { layerName: 'foodshops', visible: false },
      { layerName: 'sports', visible: false },
    ],
  };

  it('toQueryParams', () => {
    const storage: BrowserStorageService = null;
    const service = new PlannerStateService(storage);
    const state: PlannerState = {
      networkType: NetworkType.hiking,
      position: new MapPosition(12, 1, 2, 0),
      mapMode: 'survey',
      resultMode: 'detailed',
      layerStates: [
        { layerName: 'layer-1', visible: true },
        { layerName: 'layer-2', visible: true },
        { layerName: 'layer-3', visible: false },
      ],
      pois: true,
      poiLayerStates: [
        { layerName: 'poi-layer-1', visible: true },
        { layerName: 'poi-layer-2', visible: true },
        { layerName: 'poi-layer-3', visible: false },
      ],
    };

    const params = service.toQueryParams(state);

    expect(params['mode']).toEqual('survey');
    expect(params['position']).toEqual('0.00001797,0.00000898,12');
    expect(params['result']).toEqual('detailed');
    expect(params['layers']).toEqual('layer-1,layer-2');
    expect(params['pois']).toEqual('true');
    expect(params['poi-layers']).toEqual('poi-layer-1,poi-layer-2');
  });

  it('default planner state', () => {
    const browserStorageService = jasmine.createSpyObj(
      'browserStorageService',
      ['get']
    );
    browserStorageService.get.and.returnValue('');
    const service = new PlannerStateService(browserStorageService);
    const routeParams: Params = [];
    const queryParams: Params = [];
    const state = service.toPlannerState(routeParams, queryParams);
    expect(state).toEqual(defaultState);
  });

  it('networkType', () => {
    const browserStorageService = jasmine.createSpyObj(
      'browserStorageService',
      ['get']
    );
    browserStorageService.get.and.returnValue('');
    const service = new PlannerStateService(browserStorageService);
    const routeParams: Params = {
      networkType: 'cycling',
    };
    const queryParams: Params = {};
    const state = service.toPlannerState(routeParams, queryParams);
    const expectedState = {
      ...defaultState,
      networkType: NetworkType.cycling,
    };
    expect(state).toEqual(expectedState);
  });

  it('networkType invalid value', () => {
    const browserStorageService = jasmine.createSpyObj(
      'browserStorageService',
      ['get']
    );
    browserStorageService.get.and.returnValue('');
    const service = new PlannerStateService(browserStorageService);
    const routeParams: Params = {
      networkType: 'unknown',
    };
    const queryParams: Params = {};
    const state = service.toPlannerState(routeParams, queryParams);
    expect(state).toEqual(defaultState);
  });

  it('xxx', () => {});

  it('poi layers from browser storage', () => {});

  it('unknown layer', () => {});

  it('unknown poi layer', () => {});

  it('mapMode invalid value', () => {});

  it('resultMode invalid value', () => {});
});
