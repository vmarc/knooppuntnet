import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';

@Component({
  selector: 'kpn-not-found-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: ` <h1 i18n="@@not-found.title">Not found</h1> `,
})
export class NotFoundPageComponent {}
