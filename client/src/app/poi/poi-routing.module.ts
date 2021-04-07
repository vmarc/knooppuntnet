import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PoiAreasPageComponent } from './areas/poi-areas-page.component';
import { BaseSidebarComponent } from '../base/base-sidebar.component';
import { Util } from '../components/shared/util';

const routes: Routes = [
  Util.routePath('areas', PoiAreasPageComponent, BaseSidebarComponent),
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class PoiRoutingModule {}
