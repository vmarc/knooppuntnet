import { Routes } from '@angular/router';
import { BaseSidebarComponent } from '@app/base';
import { MapLayerTranslationService } from '@app/components/ol/services';
import { NewMapService } from '@app/components/ol/services';
import { PoiTileLayerService } from '@app/components/ol/services';
import { provideEffects } from '@ngrx/effects';
import { provideState } from '@ngrx/store';
import { PlannerMapService } from './pages/planner/planner-map.service';
import { PlannerPageComponent } from './pages/planner/planner-page.component';
import { PlannerSidebarComponent } from './pages/planner/sidebar/planner-sidebar.component';
import { PlannerToolbarComponent } from './pages/planner/sidebar/planner-toolbar.component';
import { MapPageComponent } from './pages/selector/_map-page.component';
import { PlannerService } from './planner.service';
import { MapService } from './services/map.service';
import { PlannerEffects } from './store/planner-effects';
import { plannerReducer } from './store/planner-reducer';
import { plannerFeatureKey } from './store/planner-state';

export const plannerRoutes: Routes = [
  {
    path: '',
    providers: [
      provideState({
        name: plannerFeatureKey,
        reducer: plannerReducer,
      }),
      provideEffects([PlannerEffects]),
      PlannerService,
      PlannerMapService,
      MapService,
      NewMapService,
      MapLayerTranslationService,
      PoiTileLayerService,
    ],
    children: [
      {
        path: '',
        pathMatch: 'full',
        component: MapPageComponent,
      },
      {
        path: '',
        pathMatch: 'full',
        component: BaseSidebarComponent,
        outlet: 'sidebar',
      },
      {
        path: ':networkType',
        children: [
          {
            path: '',
            component: PlannerPageComponent,
          },
          {
            path: '',
            component: PlannerSidebarComponent,
            outlet: 'sidebar',
          },
          {
            path: '',
            component: PlannerToolbarComponent,
            outlet: 'toolbar',
          },
        ],
      },
    ],
  },
];
