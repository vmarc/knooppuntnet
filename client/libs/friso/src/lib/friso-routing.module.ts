import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { Routes } from '@angular/router';
import { Util } from '@app/components/shared';
import { FrisoPageComponent } from './friso/friso-page.component';
import { FrisoSidebarComponent } from './friso/friso-sidebar.component';

const routes: Routes = [
  Util.routePath('', FrisoPageComponent, FrisoSidebarComponent),
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class FrisoRoutingModule {}
