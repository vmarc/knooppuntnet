import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { PageComponent } from '../../../components/page/page.component';

@Component({
  selector: 'ui-not-found-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-page>
      <h1 i18n="@@not-found.title">Not found</h1>
    </ui-page>
  `,
  imports: [PageComponent],
})
export class NotFoundPageComponent {}
