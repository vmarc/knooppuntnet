import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { SidebarComponent } from '@app/components/shared/sidebar';

@Component({
  selector: 'kpn-settings-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: '<kpn-sidebar/>',
  imports: [SidebarComponent],
})
export class SettingsSidebarComponent {}
