import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { PageComponent } from '@app/components/shared/page';
import { SidebarComponent } from '@app/components/shared/sidebar';

@Component({
  selector: 'kpn-not-found-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      <h1 i18n="@@not-found.title">Not found</h1>
      <kpn-sidebar sidebar />
    </kpn-page>
  `,
  standalone: true,
  imports: [PageComponent, SidebarComponent],
})
export class NotFoundPageComponent {}
