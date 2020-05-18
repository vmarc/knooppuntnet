import {ChangeDetectionStrategy} from "@angular/core";
import {Component, Input} from "@angular/core";

@Component({
  selector: "kpn-doc-link",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <a [href]="href()"
       (click)="$event.stopPropagation();"
       target="knooppuntnet-documentation"
       title="Go to documentation"
       i18n-title="@@doc-link.go-to-documentation">
      <mat-icon svgIcon="help"></mat-icon>
    </a>
  `,
  styles: [`
    :host {
      height: 24px;
    }
  `]
})
export class DocLinkComponent {

  @Input() subject: string;

  href(): string {
    return `docs/en.html#${this.subject}`;
  }
}
