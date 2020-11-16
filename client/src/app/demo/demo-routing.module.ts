import {Routes} from '@angular/router';
import {RouterModule} from '@angular/router';
import {NgModule} from '@angular/core';
import {DemoMenuComponent} from './menu/demo-menu.component';
import {DemoSidebarComponent} from './components/demo-sidebar.component';
import {DemoVideoComponent} from './components/demo-video.component';

const routes: Routes = [
  {
    path: ':video',
    children: [
      {
        path: '',
        component: DemoVideoComponent
      },
      {
        path: '',
        component: DemoSidebarComponent,
        outlet: 'sidebar'
      }
    ]
  },
  {
    path: '',
    children: [
      {
        path: '',
        component: DemoMenuComponent
      },
      {
        path: '',
        component: DemoSidebarComponent,
        outlet: 'sidebar'
      }
    ]
  },
];

@NgModule({
  imports: [
    RouterModule.forChild(routes)
  ],
  exports: [
    RouterModule
  ]
})
export class DemoRoutingModule {
}
