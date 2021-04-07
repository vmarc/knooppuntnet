import { Routes } from '@angular/router';
import { RouterModule } from '@angular/router';
import { Util } from '../components/shared/util';
import { NgModule } from '@angular/core';
import { SettingsPageComponent } from './settings-page.component';
import { SettingsSidebarComponent } from './settings-sidebar.component';

const routes: Routes = [
  Util.routePath('', SettingsPageComponent, SettingsSidebarComponent),
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class SettingsRoutingModule {}
