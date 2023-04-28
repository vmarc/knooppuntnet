import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { I18nService } from '@app/i18n';

@Component({
  selector: 'kpn-doc-link',
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
  styles: [
    `
      :host {
        height: 24px;
      }
    `,
  ],
  standalone: true,
  imports: [MatIconModule],
})
export class DocLinkComponent {
  @Input() subject: string;

  constructor(private i18nService: I18nService) {}

  href(): string {
    const languageSpecificSubject = this.i18nService.translation(
      `@@wiki.${this.subject}`
    );
    return `https://wiki.openstreetmap.org/wiki/${languageSpecificSubject}`;
  }
}
