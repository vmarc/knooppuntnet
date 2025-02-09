import { inject } from '@angular/core';
import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { UserService } from '@app/shared/user/user.service';

@Component({
  selector: 'kpn-osm-link-user-oath-clients',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <a
      class="external"
      [href]="link()"
      [rel]="'nofollow noreferrer'"
      target="_blank"
      i18n="@@osm-link.oath-clients"
    >
      list of authorised applications
    </a>
  `,
})
export class OsmLinkUserAothClientsComponent {
  private readonly userService = inject(UserService);
  readonly link = computed(
    () => `https://www.openstreetmap.org/user/${this.userService.user()}/oauth_clients`
  );
}
