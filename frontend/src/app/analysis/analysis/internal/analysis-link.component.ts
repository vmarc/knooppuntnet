import { computed } from '@angular/core';
import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { RouteType } from '@api/common/route-type';
import { Translations } from '@app/shared/i18n/translations';
import { RouterService } from '@app/shared/services/router.service';
import { NzIconDirective } from 'ng-zorro-antd/icon';

@Component({
  selector: 'ui-analysis-link',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <a [routerLink]="link()">
      <nz-icon [nzType]="routeType()" />
      <span>{{ name() }}</span>
    </a>
  `,
  styles: `
    a {
      display: block;
      margin-top: 0.5em;
      margin-bottom: 0.5em;
    }

    a > nz-icon {
      padding-right: 1em;
    }
  `,
  providers: [RouterService],
  imports: [NzIconDirective, RouterLink],
})
export class AnalysisLinkComponent {
  readonly routeType = input.required<RouteType>();
  protected readonly name = computed(() => Translations.get(`route-type.${this.routeType()}`));
  protected readonly link = computed(() => `/analysis/${this.routeType()}`);
}
