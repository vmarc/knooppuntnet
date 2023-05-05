import { Routes } from '@angular/router';
import { Util } from '@app/components/shared';
import { provideEffects } from '@ngrx/effects';
import { provideState } from '@ngrx/store';
import { DemoSidebarComponent } from './components/demo-sidebar.component';
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
      Util.routePath(':video', DemoVideoComponent, DemoSidebarComponent),
      Util.routePath('', DemoMenuComponent, DemoSidebarComponent),
    ],
  },
];
