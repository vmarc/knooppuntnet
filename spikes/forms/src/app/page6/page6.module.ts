import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { EffectsModule } from '@ngrx/effects';
import { StoreModule } from '@ngrx/store';
import { SharedModule } from '../shared/shared.module';
import { Page6RoutingModule } from './page6-routing.module';
import { Page6Component } from './page6.component';
import { Page6Effects } from './store/page6.effects';
import { page6Reducer } from './store/page6.reducer';
import { page6FeatureKey } from './store/page6.state';

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    Page6RoutingModule,
    StoreModule.forFeature(page6FeatureKey, page6Reducer),
    EffectsModule.forFeature([Page6Effects]),
    ReactiveFormsModule,
    MatButtonModule,
  ],
  declarations: [Page6Component],
  exports: [],
  providers: [],
})
export class Page6Module {}
