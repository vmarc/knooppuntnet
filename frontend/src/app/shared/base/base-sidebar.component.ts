import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { SidebarComponent } from '@app/shared/components/sidebar/sidebar.component';

@Component({
  selector: 'ui-base-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: ` <ui-sidebar /> `,
  imports: [SidebarComponent],
})
export class BaseSidebarComponent {}
