import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {MainFeatureComponent} from './main-feature.component';

const routes: Routes = [
  {
    path: '',
    component: MainFeatureComponent
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MainFeatureRoutingModule {
}
