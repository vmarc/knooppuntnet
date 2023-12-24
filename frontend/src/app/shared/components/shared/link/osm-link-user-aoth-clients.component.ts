import { AsyncPipe } from '@angular/common';
import { inject } from '@angular/core';
import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { UserStore } from '../../../user/user.store';

@Component({
  selector: 'kpn-osm-link-user-oath-clients',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <a
      class="external"
      href="{{ link() }}}"
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
  private readonly userStore = inject(UserStore);
  readonly link = computed(
    () => `https://www.openstreetmap.org/user/${this.userStore.user()}/oauth_clients`
  );
}
