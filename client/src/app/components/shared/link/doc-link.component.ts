import {ChangeDetectionStrategy} from '@angular/core';
import {Component, Input} from '@angular/core';
import {I18nService} from '../../../i18n/i18n.service';

@Component({
  selector: 'kpn-doc-link',
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

  constructor(private i18nService: I18nService) {
  }

  href(): string {
    const languageSpecificSubject = this.i18nService.translation(`@@wiki.${this.subject}`);
    return `https://wiki.openstreetmap.org/wiki/${languageSpecificSubject}`;
  }
}
