import {CommonModule} from '@angular/common';
import {NgModule} from '@angular/core';
import {StoreModule} from '@ngrx/store';
import {DemoEffects} from './demo/demo.effects';
import {EffectsModule} from '@ngrx/effects';
import {StoreDevtoolsModule} from '@ngrx/store-devtools';
import {environment} from '../../environments/environment';
import {metaReducers, reducers} from './core.state';
import {StoreRouterConnectingModule} from '@ngrx/router-store';
import {DemoService} from './demo/demo.service';
import {SharedEffects} from './shared/shared.effects';

@NgModule({
  imports: [
    CommonModule,
    StoreModule.forRoot(reducers, {metaReducers}),
    StoreRouterConnectingModule.forRoot(),
    EffectsModule.forRoot([
      SharedEffects,
      DemoEffects
    ]),
    environment.production
      ? []
      : StoreDevtoolsModule.instrument({
        name: 'Knooppuntnet'
      })
  ],
  providers: [
    DemoService
  ]
})
export class CoreModule {
}
