import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";

export const routes: Routes = [
  {
    path: "analysis",
    loadChildren: () => import("./analysis/analysis/_analysis.module").then(m => m.AnalysisModule)
  },
  {
    path: "map",
    loadChildren: () => import("./map/map.module").then(m => m.MapModule)
  },
  {
    path: "translations",
    loadChildren: () => import("./translations/translations.module").then(m => m.TranslationsModule)
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
