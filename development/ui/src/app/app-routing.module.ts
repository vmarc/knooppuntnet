import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';

export const routes: Routes = [
  {
    path: 'analysis',
    loadChildren: './analysis/analysis.module#AnalysisModule'
  },
  {
    path: 'planner',
    loadChildren: './planner/planner.module#PlannerModule'
  },
  {
    path: 'translations',
    loadChildren: './translations/translations.module#TranslationsModule'
  },
  {
    path: '',
    loadChildren: './base/base.module#BaseModule'
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
