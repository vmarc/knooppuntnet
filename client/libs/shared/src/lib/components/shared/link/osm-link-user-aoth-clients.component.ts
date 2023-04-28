import { AsyncPipe } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { selectUserUser } from '@app/core';
import { Store } from '@ngrx/store';

@Component({
  selector: 'kpn-osm-link-user-oath-clients',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <a
      class="external"
      href="{{ link(user$ | async) }}"
      rel="nofollow noreferrer"
      target="_blank"
      i18n="@@osm-link.oath-clients"
    >
      list of authorised applications
    </a>
  `,
  standalone: true,
  imports: [AsyncPipe],
})
export class OsmLinkUserAothClientsComponent {
  readonly user$ = this.store.select(selectUserUser);

  constructor(private store: Store) {}

  link(user: string): string {
    return `https://www.openstreetmap.org/user/${user}/oauth_clients`;
  }
}
