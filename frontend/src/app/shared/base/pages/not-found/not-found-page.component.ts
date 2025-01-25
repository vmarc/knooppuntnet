import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { PageComponent } from '../../../components/shared/page/page.component';

@Component({
  selector: 'kpn-not-found-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      <h1 i18n="@@not-found.title">Not found</h1>
    </kpn-page>
  `,
  imports: [PageComponent],
})
export class NotFoundPageComponent {}
