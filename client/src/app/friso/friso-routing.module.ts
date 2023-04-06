import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { Util } from '../components/shared/util';
import { FrisoPageComponent } from '@app/friso/friso/friso-page.component';
import { FrisoSidebarComponent } from '@app/friso/friso/friso-sidebar.component';

const routes: Routes = [
  Util.routePath('', FrisoPageComponent, FrisoSidebarComponent),
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class FrisoRoutingModule {}
