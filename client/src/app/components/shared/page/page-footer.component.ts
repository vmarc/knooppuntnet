import {Component} from "@angular/core";

@Component({
  selector: "kpn-page-footer",
  template: `
    <div class="footer">
      <a
        [href]="docsUrl()"
        class="external link-list-entry"
        target="knooppuntnet-documentation"
        i18n="@@footer.documentation">
        documentation
      </a>
      <a
        href="https://www.openstreetmap.org/message/new/vmarc"
        class="external link-list-entry"
        target="_blank"
        i18n="@@footer.contact">
        contact
      </a>
      <a
        href="https://github.com/vmarc/knooppuntnet/issues"
        class="external"
        target="_blank"
        i18n="@@footer.issues">
        issues
      </a>
    </div>
  `,
  styles: [`
    .footer {
      padding: 15px
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
