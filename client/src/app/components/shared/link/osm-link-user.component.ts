import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { AppState } from '../../../core/core.state';
import { selectSharedUser } from '../../../core/shared/shared.selectors';

@Component({
  selector: 'kpn-osm-link-user',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-osm-link
      kind="user"
      [elementId]="user$ | async"
      [title]="user$ | async"
    ></kpn-osm-link>
  `,
})
export class OsmLinkUserComponent {
  readonly user$ = this.store.select(selectSharedUser);

  constructor(private store: Store<AppState>) {}
}
