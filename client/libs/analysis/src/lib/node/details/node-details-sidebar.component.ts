import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { SidebarFooterComponent } from '@app/components/shared/sidebar';

@Component({
  selector: 'kpn-node-details-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="sidebar-body sidebar-logo"></div>
    <div class="sidebar-footer">
      <div class="tips">
        <p i18n="@@node.details-sidebar.tip">
          TIP: click on other nodes or routes in the map to go to the details of
          these nodes or routes. Use ctrl-click to open to open these details in
          another window.
        </p>
      </div>
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

      .tips {
        font-style: italic;
        color: gray;
        margin: 1em;
      }
    `,
  ],
  standalone: true,
  imports: [SidebarFooterComponent],
})
export class NodeDetailsSidebarComponent {}
