import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { SidebarFooterComponent } from './sidebar-footer.component';

@Component({
  selector: 'kpn-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="sidebar-body sidebar-logo">
      <div>
        <ng-content />
      </div>
      <div class="body-bottom">
        <ng-content select="[body-bottom]" />
      </div>
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
        align-items: stretch;
        min-height: calc(100vh - 48px);
      }

      .sidebar-body {
        display: flex;
        flex-direction: column;
        align-items: stretch;
        align-content: stretch;
        flex: 1;
      }

      .sidebar-footer {
        flex: 0;
      }

      .body-bottom {
        margin-top: auto;
      }
    `,
  ],
  standalone: true,
  imports: [SidebarFooterComponent],
})
export class SidebarComponent {}
