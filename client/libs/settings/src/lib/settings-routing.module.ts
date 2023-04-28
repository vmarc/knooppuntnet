import { NgModule } from '@angular/core';
import { Routes } from '@angular/router';
import { RouterModule } from '@angular/router';
import { Util } from '@app/components/shared';
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
