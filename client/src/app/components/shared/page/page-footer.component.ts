import {Component} from "@angular/core";

@Component({
  selector: "kpn-page-footer",
  template: `
    <div class="footer">
      <a [href]="docsUrl()" class="external" target="knooppuntnet-documentation">documentation</a>
      |
      <a href="https://www.openstreetmap.org/message/new/vmarc" class="external" target="_blank">contact</a>
      |
      <a href="https://github.com/vmarc/knooppuntnet/issues" class="external" target="_blank">issues</a>
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

  docsUrl(): string {
    return "docs/en.html";
  }

}
