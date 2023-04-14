import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { Util } from '@app/components/shared';
import { ChangesSidebarComponent } from '../components/changes/filter';
import { ChangesPageComponent } from './page/_changes-page.component';

const routes: Routes = [
  Util.routePath('', ChangesPageComponent, ChangesSidebarComponent),
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ChangesRoutingModule {}
