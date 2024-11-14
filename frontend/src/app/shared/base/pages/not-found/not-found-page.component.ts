import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OldPageComponent } from '@app/components/shared/page';
import { SidebarComponent } from '@app/components/shared/sidebar';

@Component({
  selector: 'kpn-not-found-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-old-page>
      <h1 i18n="@@not-found.title">Not found</h1>
      <kpn-sidebar sidebar />
    </kpn-old-page>
  `,
  standalone: true,
  imports: [OldPageComponent, SidebarComponent],
})
export class NotFoundPageComponent {}
