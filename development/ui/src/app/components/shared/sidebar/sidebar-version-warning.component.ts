import {Component} from '@angular/core';

@Component({
  selector: 'kpn-sidebar-version-warning',
  template: `
    <div class="warning">
      This version of knooppuntnet is still under construction.
      The functionality is still incomplete, and what is there may not work
      correctly yet. Until the work is finished, it is perhaps better to
      use <a href="https://knooppuntnet.nl" class="external" target="_blank">knooppuntnet.nl</a>
      instead.
    </div>
  `,
  styles: [`
    .warning {
      display: inline-block;
      max-width: 360px;
      font-style: italic;
      color: red;
      padding: 20px;
    }
  `]
})
export class SidebarVersionWarningComponent {
}
