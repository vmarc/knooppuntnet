import {NgModule} from '@angular/core';
import {Routes} from '@angular/router';
import {RouterModule} from '@angular/router';
import {Page6Component} from './page6.component';

const routes: Routes = [
  {
    path: '',
    component: Page6Component
  }
];

@NgModule({
  imports: [
    RouterModule.forChild(routes)
  ],
  exports: [
    RouterModule
  ]
})
export class Page6RoutingModule {
}
