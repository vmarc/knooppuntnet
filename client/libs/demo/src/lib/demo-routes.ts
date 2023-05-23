import { Routes } from '@angular/router';
import { provideEffects } from '@ngrx/effects';
import { provideState } from '@ngrx/store';
import { DemoVideoComponent } from './components/demo-video.component';
import { DemoService } from './demo.service';
import { DemoMenuComponent } from './menu/demo-menu.component';
import { DemoEffects } from './store/demo.effects';
import { demoReducer } from './store/demo.reducer';
import { demoFeatureKey } from './store/demo.state';

export const demoRoutes: Routes = [
  {
    path: '',
    providers: [
      provideState({
        name: demoFeatureKey,
        reducer: demoReducer,
      }),
      provideEffects([DemoEffects]),
      DemoService,
    ],
    children: [
      {
        path: ':video',
        component: DemoVideoComponent,
      },
      {
        path: '',
        component: DemoMenuComponent,
      },
    ],
  },
];
