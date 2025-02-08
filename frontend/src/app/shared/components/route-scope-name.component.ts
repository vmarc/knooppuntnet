import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouteScope } from '@api/common/route-scope';
import { Translations } from '@app/i18n';

@Component({
  selector: 'kpn-route-scope-name',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `{{ routeScopeName() }}`,
})
export class RouteScopeNameComponent {
  routeScope = input.required<RouteScope>();

  protected routeScopeName = computed(() => Translations.get('route-scope.' + this.routeScope()));
}
