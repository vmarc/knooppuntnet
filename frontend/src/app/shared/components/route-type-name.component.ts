import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouteType } from '@api/common/route-type';
import { Translations } from '@app/shared/i18n/translations';

@Component({
  selector: 'ui-route-type-name',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `{{ routeTypeName() }}`,
})
export class RouteTypeNameComponent {
  readonly routeType = input.required<RouteType>();

  protected readonly routeTypeName = computed(() =>
    Translations.get('route-type.' + this.routeType())
  );
}
