import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {SidebarComponent} from '../components/shared/sidebar/sidebar.component';
import {Util} from '../components/shared/util';
import {LongDistanceExampleComponent} from './example/long-distance-example.component';
import {LongDistanceRoutesComponent} from './routes/long-distance-routes.component';

const routes: Routes = [
  Util.routePath('routes', LongDistanceRoutesComponent, SidebarComponent),
  Util.routePath('example', LongDistanceExampleComponent, SidebarComponent)
];

@NgModule({
  imports: [
    RouterModule.forChild(routes)
  ],
  exports: [
    RouterModule
  ]
})
export class LongDistanceRoutingModule {
}
