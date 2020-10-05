import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";

const routes: Routes = [
  {
    path: "analysis",
    loadChildren: () => import("./analysis/analysis/analysis.module").then(m => m.AnalysisModule)
  },
  {
    path: "map",
    loadChildren: () => import("./map/map.module").then(m => m.MapModule)
  },
  {
    path: "status",
    loadChildren: () => import("./status/status.module").then(m => m.StatusModule)
  },
  {
    path: "settings",
    loadChildren: () => import("./settings/settings.module").then(m => m.SettingsModule)
  },
  {
    path: "poi",
    loadChildren: () => import("./poi/poi.module").then(m => m.PoiModule)
  },
  {
    path: "",
    loadChildren: () => import("./base/base.module").then(m => m.BaseModule)
  }
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes, {enableTracing: false})
  ],
  exports: [
    RouterModule
  ]
})
export class AppRoutingModule {
}
