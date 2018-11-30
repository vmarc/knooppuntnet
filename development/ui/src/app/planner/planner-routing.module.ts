import {RouterModule, Routes} from "@angular/router";
import {NgModule} from "@angular/core";
import {PlannerSidebarComponent} from "./planner-sidebar.component";
import {MapPageComponent} from "./pages/map/map-page.component";

const routes: Routes = [
  {
    path: '',
    component: PlannerSidebarComponent,
    outlet: "sidebar"
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
