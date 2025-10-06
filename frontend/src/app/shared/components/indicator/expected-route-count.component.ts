import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { ExpectedRouteCountNokComponent } from '@app/shared/components/indicator/expected-route-count-nok.component';
import { ExpectedRouteCountOkComponent } from '@app/shared/components/indicator/expected-route-count-ok.component';
import { IntegrityData } from '@app/shared/components/indicator/integrity-data';

@Component({
  selector: 'ui-expected-route-count',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (expectedRouteCountOk()) {
      <div class="color-info">
        <span class="kpn-label">
          <ui-expected-route-count-ok [data]="data()" />
        </span>
        <span>{{ expectedRouteCount() }}</span>
      </div>
    } @else {
      <div class="color-error">
        <span class="kpn-label">
          <ui-expected-route-count-nok [data]="data()" />
        </span>
        <span>{{ expectedRouteCount() }}</span
        ><span>, </span>
        <span i18n="@@network-nodes.table.expected-route-count" class="kpn-label">but found</span>
        <span>{{ actualRouteCount() }}</span>
      </div>
    }
  `,
  imports: [ExpectedRouteCountOkComponent, ExpectedRouteCountNokComponent],
})
export class ExpectedRouteCountComponent {
  readonly data = input.required<IntegrityData>();

  readonly routeType = computed(() => this.data().routeType);
  readonly routeScope = computed(() => this.data().routeScope);
  readonly expectedRouteCount = computed(() => this.data().expected);
  readonly actualRouteCount = computed(() => this.data().actual);
  readonly expectedRouteCountOk = computed(
    () => this.expectedRouteCount() == this.actualRouteCount()
  );
}
