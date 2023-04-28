import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { Routes } from '@angular/router';
import { Util } from '@app/components/shared';
import { ChangesPageComponent } from './page/_changes-page.component';
import { ChangesSidebarComponent } from './sidebar/changes-sidebar.component';

const routes: Routes = [
  Util.routePath('', ChangesPageComponent, ChangesSidebarComponent),
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ChangesRoutingModule {}
