import {CommonModule} from '@angular/common';
import {NgModule} from '@angular/core';
import {EffectsModule} from '@ngrx/effects';
import {StoreRouterConnectingModule} from '@ngrx/router-store';
import {StoreModule} from '@ngrx/store';
import {StoreDevtoolsModule} from '@ngrx/store-devtools';
import {environment} from '../../environments/environment';
import {NodeEffects} from './analysis/node/node.effects';
import {RouteEffects} from './analysis/route/route.effects';
import {metaReducers, reducers} from './core.state';
import {DemoEffects} from './demo/demo.effects';
import {DemoService} from './demo/demo.service';
import {LongDistanceEffects} from './longdistance/long-distance.effects';
import {SharedEffects} from './shared/shared.effects';

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
      NodeEffects,
      RouteEffects,
      LongDistanceEffects
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
