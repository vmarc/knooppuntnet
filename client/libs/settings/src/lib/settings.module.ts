import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { SharedModule } from '@app/components/shared';
import { SettingsPageComponent } from './settings-page.component';
import { SettingsRoutingModule } from './settings-routing.module';
import { SettingsSidebarComponent } from './settings-sidebar.component';

@NgModule({
  declarations: [SettingsPageComponent, SettingsSidebarComponent],
  imports: [
    CommonModule,
    SettingsRoutingModule,
    SharedModule,
    MatSlideToggleModule,
  ],
})
export class SettingsModule {}
