import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { SidebarFooterComponent } from './sidebar-footer.component';

@Component({
  selector: 'kpn-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="sidebar-body sidebar-logo">
      <ng-content />
    </div>
    <div class="sidebar-footer">
      <kpn-sidebar-footer />
    </div>
  `,
  styles: [
    `
      :host {
        display: flex;
        flex-direction: column;
        min-height: calc(100vh - 48px);
      }

      .sidebar-body {
        flex: 1;
      }

      .sidebar-footer {
        flex: 0;
      }
    `,
  ],
  standalone: true,
  imports: [SidebarFooterComponent],
})
export class SidebarComponent {}
