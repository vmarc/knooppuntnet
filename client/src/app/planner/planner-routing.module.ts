import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { BaseSidebarComponent } from '../base/base-sidebar.component';
import { Util } from '../components/shared/util';
import { PlannerPageComponent } from './pages/planner/planner-page.component';
import { PlannerSidebarComponent } from './pages/planner/sidebar/planner-sidebar.component';
import { PlannerToolbarComponent } from './pages/planner/sidebar/planner-toolbar.component';
import { MapPageComponent } from './pages/selector/_map-page.component';

const routes: Routes = [
  Util.routePath('', MapPageComponent, BaseSidebarComponent),
  {
    path: ':networkType',
    children: [
      {
        path: '',
        component: PlannerPageComponent,
      },
      {
        path: '',
        component: PlannerSidebarComponent,
        outlet: 'sidebar',
      },
      {
        path: '',
        component: PlannerToolbarComponent,
        outlet: 'toolbar',
      },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class PlannerRoutingModule {}
