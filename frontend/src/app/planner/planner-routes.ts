import { Routes } from '@angular/router';
import { NewMapService } from '@app/ol/services';
import { PoiTileLayerService } from '@app/ol/services';
import { provideEffects } from '@ngrx/effects';
import { provideState } from '@ngrx/store';
import { PlannerMapService } from './pages/planner/planner-map.service';
import { PlannerPageComponent } from './pages/planner/planner-page.component';
import { MapPageComponent } from './pages/selector/_map-page.component';
import { PdfService } from './pdf/pdf.service';
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
      MapService,
      NewMapService,
      PdfService,
      PlannerMapService,
      PlannerService,
      PoiTileLayerService,
    ],
    children: [
      {
        path: '',
        pathMatch: 'full',
        component: MapPageComponent,
      },
      {
        path: ':networkType',
        children: [
          {
            path: '',
            component: PlannerPageComponent,
          },
        ],
      },
    ],
  },
];
