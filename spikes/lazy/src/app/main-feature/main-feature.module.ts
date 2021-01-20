import {CommonModule} from '@angular/common';
import {NgModule} from '@angular/core';
import {Feature1LazyLoadComponent} from './feature-1-lazy-load.component';
import {Feature2LazyLoadComponent} from './feature-2-lazy-load.component';
import {Feature3LazyLoadComponent} from './feature-3-lazy-load.component';
import {Feature4LazyLoadComponent} from './feature-4-lazy-load.component';
import {MainFeatureRoutingModule} from './main-feature-routing.module';
import {MainFeatureComponent} from './main-feature.component';

@NgModule({
  imports: [
    CommonModule,
    MainFeatureRoutingModule,
  ],
  declarations: [
    MainFeatureComponent,
    Feature1LazyLoadComponent,
    Feature2LazyLoadComponent,
    Feature3LazyLoadComponent,
    Feature4LazyLoadComponent,
  ]
})
export class MainFeatureModule {
}
