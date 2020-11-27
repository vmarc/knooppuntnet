import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {CounterComponent} from './counter/counter.component';
import {HomeComponent} from './home/home.component';
import {IssuesComponent} from './issue/issues.component';
import {NodeComponent} from './node/node.component';

const routes: Routes = [
  {
    path: 'counter',
    component: CounterComponent
  },
  {
    path: 'issues',
    component: IssuesComponent
  },
  {
    path: 'node',
    component: NodeComponent
  },
  {
    path: '**',
    component: HomeComponent
  }
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes)
  ],
  exports: [
    RouterModule
  ]
})
export class AppRoutingModule {
}
