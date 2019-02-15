import {Component} from '@angular/core';

@Component({
  selector: 'kpn-page-footer',
  template: `
    <div class="footer">
      <kpn-link-about></kpn-link-about>
      |
      <kpn-link-glossary></kpn-link-glossary>
      |
      <kpn-link-links></kpn-link-links>
      |
      <kpn-link-overview></kpn-link-overview>
      |
      <a href="https://www.openstreetmap.org/message/new/vmarc" class="external" target="_blank">Contact</a>
      |
      <a href="https://github.com/vmarc/knooppuntnet/issues" class="external" target="_blank">Issues</a>
    </div>
  `,
  styles: [`
    .footer {
      padding: 15px;
      border-top-width: 1px;
      border-top-style: solid;
      border-top-color: lightgray;
      text-align: center;
    }
  `]
})
export class PageFooterComponent {
}
