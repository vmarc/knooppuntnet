import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { Translations } from '@app/shared/i18n/translations';

@Component({
  selector: 'ui-doc-link',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <a
      [href]="href()"
      (click)="$event.stopPropagation()"
      target="knooppuntnet-documentation"
      title="Go to documentation"
      i18n-title="@@doc-link.go-to-documentation"
    >
      <mat-icon svgIcon="help" />
    </a>
  `,
  styles: `
    :host {
      height: 24px;
    }
  `,
  imports: [MatIconModule],
})
export class DocLinkComponent {
  subject = input.required<string>();

  href(): string {
    const languageSpecificSubject = Translations.get(`@@wiki.${this.subject()}`);
    return `https://wiki.openstreetmap.org/wiki/${languageSpecificSubject}`;
  }
}
