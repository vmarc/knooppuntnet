import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { selectUserUser } from '@app/core/user/user.selectors';

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
})
export class OsmLinkUserAothClientsComponent {
  readonly user$ = this.store.select(selectUserUser);

  constructor(private store: Store) {}

  link(user: string): string {
    return `https://www.openstreetmap.org/user/${user}/oauth_clients`;
  }
}
