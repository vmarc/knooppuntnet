import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { SidebarComponent } from '@app/shared/components/sidebar/sidebar.component';

@Component({
  selector: 'kpn-base-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: ` <kpn-sidebar></kpn-sidebar> `,
  imports: [SidebarComponent],
})
export class BaseSidebarComponent {}
