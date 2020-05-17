import {ChangeDetectionStrategy} from "@angular/core";
import {Component, Input} from "@angular/core";

/* tslint:disable:template-i18n */
@Component({
  selector: "kpn-doc-link",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <a [href]="href()"
       target="knooppuntnet-documentation"
       title="Go to documentation"
       i18n-title="@@doc-link.go-to-documentation">
      <mat-icon svgIcon="help"></mat-icon>
    </a>
  `
})
export class DocLinkComponent {

  @Input() subject: string;

  href(): string {
    return `docs/en.html#${this.subject}`;
  }
}
