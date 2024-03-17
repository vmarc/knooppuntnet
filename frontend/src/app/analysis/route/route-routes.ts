import { Routes } from '@angular/router';
import { provideEffects } from '@ngrx/effects';
import { provideState } from '@ngrx/store';
import { RouteChangesPageComponent } from './changes/route-changes-page.component';
import { RoutePageComponent } from './details/route-page.component';
import { RouteMapPageComponent } from './map/route-map-page.component';
import { RouteMapService } from './map/components/route-map.service';
import { RouteEffects } from './store/route.effects';
import { routeReducer } from './store/route.reducer';
import { routeFeatureKey } from './store/route.state';

export const routeRoutes: Routes = [
  {
    path: '',
    providers: [
      provideState({
        name: routeFeatureKey,
        reducer: routeReducer,
      }),
      provideEffects([RouteEffects]),
      RouteMapService, // provided here: referenced in effects
    ],
    children: [
      {
        path: ':routeId',
        component: RoutePageComponent,
      },
      {
        path: ':routeId/map',
        component: RouteMapPageComponent,
      },
      {
        path: ':routeId/changes',
        component: RouteChangesPageComponent,
      },
    ],
  },
];
