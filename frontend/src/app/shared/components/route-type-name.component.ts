import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouteType } from '@api/common';
import { Translations } from '@app/i18n';

@Component({
  selector: 'kpn-route-type-name',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `{{ routeTypeName() }}`,
})
export class RouteTypeNameComponent {
  routeType = input.required<RouteType>();

  protected routeTypeName = computed(() => Translations.get('route-type.' + this.routeType()));
}
