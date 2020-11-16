import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';

@Component({
  selector: 'kpn-fact-deleted',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <p i18n="@@fact.description.deleted">
      Deleted from the OpenStreetMap database.
    </p>
  `
})
export class FactDeletedComponent {
}
