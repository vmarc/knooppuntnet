import { Routes } from '@angular/router';
import { Util } from '@app/components/shared';
import { SettingsPageComponent } from './settings-page.component';
import { SettingsSidebarComponent } from './settings-sidebar.component';

export const settingsRoutes: Routes = [
  Util.routePath('', SettingsPageComponent, SettingsSidebarComponent),
];
