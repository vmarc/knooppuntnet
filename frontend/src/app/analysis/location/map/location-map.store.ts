import { inject } from '@angular/core';
import { LocationMapPage } from '@api/common/location';
import { ApiResponse } from '@api/custom';
import { MapPosition } from '@app/ol/domain';
import { ApiService } from '@app/services';
import { withHooks } from '@ngrx/signals';
import { patchState } from '@ngrx/signals';
import { withMethods } from '@ngrx/signals';
import { withState } from '@ngrx/signals';
import { signalStore } from '@ngrx/signals';
import { RouterService } from '../../../shared/services/router.service';
import { LocationStore } from '../location.store';
import { LocationMapService } from './location-map.service';

export interface LocationMapState {
  response: ApiResponse<LocationMapPage> | null;
}

const initialState: LocationMapState = {
  response: null,
};

export const LocationMapStore = signalStore(
  withState(initialState),
  withMethods((store) => {
    const apiService = inject(ApiService);
    const locationStore = inject(LocationStore);
    const locationMapService = inject(LocationMapService);
    const routerService = inject(RouterService);

    const load = () => {
      apiService.locationMap(locationStore.locationKey()).subscribe((response) => {
        if (response.result) {
          locationStore.updateSummary(response.result.summary);
        }
        patchState(store, {
          response,
        });
      });
    };

    return {
      init: () => {
        locationStore.initLocationPage();
        load();
      },
      afterViewInit: () => {
        const geoJson = store.response().result.geoJson;
        const bounds = store.response().result.bounds;
        const mapPositionFromUrl: MapPosition = undefined; // TODO routerService.queryParamMapPosition();
        const surveyDateValues = null; // TODO eliminate surveyDateValues
        locationMapService.init(
          locationStore.locationKey().networkType,
          surveyDateValues,
          geoJson,
          bounds,
          mapPositionFromUrl
        );
      },
      destroy: (): void => {
        locationMapService.destroy();
      },
    };
  }),
  withHooks({
    onInit({ init }): void {
      init();
    },
    onDestroy({ destroy }): void {
      destroy();
    },
  })
);
