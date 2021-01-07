import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from './home.component';

const routes: Routes = [
  {
    path: '',
    component: HomeComponent
  },
  {
    path: 'map',
    loadChildren: () => import('./map/map.module').then(m => m.MapModule)
  }
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes, {enableTracing: true, relativeLinkResolution: 'legacy'})
  ],
  exports: [
    RouterModule
  ]
})
export class AppRoutingModule {
}
