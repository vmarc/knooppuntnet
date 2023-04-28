import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { EffectsModule } from '@ngrx/effects';
import { StoreRouterConnectingModule } from '@ngrx/router-store';
import { StoreModule } from '@ngrx/store';
import { StoreDevtoolsModule } from '@ngrx/store-devtools';
// eslint-disable-next-line @softarc/sheriff/deep-import
import { environment } from '../../../../../src/environments/environment';
import { metaReducers, reducers } from './core.state';
import { SharedEffects } from './shared/shared.effects';
import { UserEffects } from './user/user.effects';

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
        strictActionTypeUniqueness: true,
      },
    }),
    StoreRouterConnectingModule.forRoot(),
    EffectsModule.forRoot([SharedEffects, UserEffects]),
    environment.production
      ? []
      : StoreDevtoolsModule.instrument({
          name: 'Knooppuntnet',
          serialize: {
            options: {
              map: true,
            },
          },
        }),
  ],
})
export class CoreModule {}
