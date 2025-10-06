import { computed } from '@angular/core';
import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { IntegrityData } from '@app/shared/components/indicator/integrity-data';
import { TermComponent } from '@app/shared/components/term/term.component';
import { RouteScopes } from '@app/shared/kpn/common/route-scopes';
import { RouteTypes } from '@app/shared/kpn/common/route-types';

@Component({
  selector: 'ui-expected-route-count-ok',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-term
      level="info"
      name="Expected routes"
      title="OK - Expected routes"
      i18n="@@integrity-indicator.green.text"
    >
      The number of routes found in this network node ({{ actualRouteCount() }}) does match the
      expected number of routes ({{ expectedRouteCount() }}) as defined in the *"{{ tag() }}"* tag
      on this node. This is what we expect.
    </ui-term>
  `,
  imports: [TermComponent],
})
export class ExpectedRouteCountOkComponent {
  readonly data = input.required<IntegrityData>();
  readonly expectedRouteCount = computed(() => this.data().expected);
  readonly actualRouteCount = computed(() => this.data().actual);
  readonly tag = computed(() => {
    const routeTypeLetter = RouteTypes.letter(this.data().routeType);
    const routeScopeLetter = RouteScopes.letter(this.data().routeScope);
    return `expected_${routeScopeLetter}${routeTypeLetter}n_route_relations`;
  });
}
