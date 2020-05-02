import {ChangeDetectionStrategy} from "@angular/core";
import {Component} from "@angular/core";

@Component({
  selector: "kpn-page-footer",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="footer links">
      <li>
        <a
          [href]="docsUrl()"
          class="external"
          target="knooppuntnet-documentation"
          i18n="@@footer.documentation">
          documentation
        </a>
      </li>
      <li>
        <a
          href="https://www.openstreetmap.org/message/new/vmarc"
          class="external"
          target="_blank"
          rel="nofollow noreferrer"
          i18n="@@footer.contact">
          contact
        </a>
      </li>
      <li>
        <a
          href="https://github.com/vmarc/knooppuntnet/issues"
          class="external"
          target="_blank"
          rel="nofollow noreferrer"
          i18n="@@footer.issues">
          issues
        </a>
      </li>
      <li>
        <a
          routerLink="/status"
          i18n="@@footer.status">
          status
        </a>
      </li>
    </ul>
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
