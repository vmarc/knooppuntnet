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
import {NodeEffects} from './analysis/node/node.effects';

@NgModule({
  imports: [
    CommonModule,
    StoreModule.forRoot(reducers, {
      metaReducers,
      runtimeChecks: {
        strictStateImmutability: true,
        strictActionImmutability: true,
        strictStateSerializability: false,
        strictActionSerializability: false,
        strictActionWithinNgZone: true,
        strictActionTypeUniqueness: true
      }
    }),
    StoreRouterConnectingModule.forRoot(),
    EffectsModule.forRoot([
      SharedEffects,
      DemoEffects,
      NodeEffects
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
