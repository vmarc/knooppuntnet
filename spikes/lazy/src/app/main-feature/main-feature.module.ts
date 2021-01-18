import {CommonModule} from '@angular/common';
import {NgModule} from '@angular/core';
import {Feature1ContainerComponent} from './feature-1-container.component';
import {Feature2ContainerComponent} from './feature-2-container.component';
import {Feature3ContainerComponent} from './feature-3-container.component';
import {Feature4ContainerComponent} from './feature-4-container.component';
import {MainFeatureRoutingModule} from './main-feature-routing.module';
import {MainFeatureComponent} from './main-feature.component';

@NgModule({
  imports: [
    CommonModule,
    MainFeatureRoutingModule,
  ],
  declarations: [
    MainFeatureComponent,
    Feature1ContainerComponent,
    Feature2ContainerComponent,
    Feature3ContainerComponent,
    Feature4ContainerComponent,
  ]
})
export class MainFeatureModule {
}
