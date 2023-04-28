import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { Routes } from '@angular/router';
import { JosmComponent } from './josm.component';

const routes: Routes = [
  {
    path: 'josm',
    component: JosmComponent,
  },
  {
    path: 'analysis',
    loadChildren: () =>
      import('@app/analysis/analysis').then((m) => m.AnalysisModule),
  },
  {
    path: 'map',
    loadChildren: () => import('@app/planner').then((m) => m.PlannerModule),
  },
  {
    path: 'status',
    loadChildren: () => import('@app/status').then((m) => m.StatusModule),
  },
  {
    path: 'settings',
    loadChildren: () => import('@app/settings').then((m) => m.SettingsModule),
  },
  {
    path: 'poi',
    loadChildren: () => import('@app/poi').then((m) => m.PoiModule),
  },
  {
    path: 'demo',
    loadChildren: () => import('@app/demo').then((m) => m.DemoModule),
  },
  {
    path: 'monitor',
    loadChildren: () => import('@app/monitor').then((m) => m.MonitorModule),
  },
  {
    path: 'friso',
    loadChildren: () => import('@app/friso').then((m) => m.FrisoModule),
  },
  {
    path: '',
    loadChildren: () => import('@app/base').then((m) => m.BaseModule),
  },
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes, {
      enableTracing: false,
    }),
  ],
  exports: [RouterModule],
})
export class AppRoutingModule {}
