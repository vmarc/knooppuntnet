import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { Translations } from '@app/shared/i18n/translations';
import { NzButtonComponent } from 'ng-zorro-antd/button';
import { NzIconDirective } from 'ng-zorro-antd/icon';

@Component({
  selector: 'ui-doc-link',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <a
      nz-button
      nzShape="circle"
      [href]="href()"
      (click)="$event.stopPropagation()"
      target="knooppuntnet-documentation"
      title="Go to documentation"
      i18n-title="@@doc-link.go-to-documentation"
    >
      <nz-icon nzType="question" />
    </a>
  `,
  styles: `
    :host {
      height: 24px;
    }
  `,
  imports: [MatIconModule, NzButtonComponent, NzIconDirective],
})
export class DocLinkComponent {
  readonly subject = input.required<string>();

  href(): string {
    const languageSpecificSubject = Translations.get(`@@wiki.${this.subject()}`);
    return `https://wiki.openstreetmap.org/wiki/${languageSpecificSubject}`;
  }
}
