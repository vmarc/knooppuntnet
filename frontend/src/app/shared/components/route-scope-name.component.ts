import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouteScope } from '@api/common/route-scope';
import { Translations } from '@app/shared/i18n/translations';

@Component({
  selector: 'ui-route-scope-name',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `{{ routeScopeName() }}`,
})
export class RouteScopeNameComponent {
  readonly routeScope = input.required<RouteScope>();

  protected readonly routeScopeName = computed(() =>
    Translations.get('route-scope.' + this.routeScope())
  );
}
