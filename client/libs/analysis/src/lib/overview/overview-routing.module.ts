import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { Routes } from '@angular/router';
import { Util } from '@app/components/shared';
import { OverviewPageComponent } from './overview/_overview-page.component';
import { OverviewSidebarComponent } from './overview/overview-sidebar.component';

const routes: Routes = [
  Util.routePath('', OverviewPageComponent, OverviewSidebarComponent),
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class OverviewRoutingModule {}
