import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {PlannerSidebarComponent} from "./sidebar/_planner-sidebar.component";
import {PlannerPageComponent} from "./pages/planner/planner-page.component";
import {MapPageComponent} from "./pages/map/map-page.component";

const routes: Routes = [
  {
    path: '',
    component: PlannerSidebarComponent,
    outlet: "sidebar"
  },
  {
    path: '',
    component: PlannerPageComponent
  },
  {
    path: 'map/:networkType',
    component: MapPageComponent
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
export class PlannerRoutingModule {
}
