import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';

@Component({
  selector: 'kpn-fact-route-fixmetodo',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <markdown i18n="@@fact.description.route-fixmetodo">
      Route definition needs work (has tag _"fixmetodo"_).
    </markdown>
  `,
})
export class FactRouteFixmetodoComponent {}
