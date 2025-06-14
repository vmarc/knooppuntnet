import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'ui-link-node',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <a
      [routerLink]="link()"
      [state]="{ nodeName: nodeName() }"
      title="Open node page"
      i18n-title="@@link-node.title"
    >
      {{ nodeName() }}
    </a>
  `,
  imports: [RouterLink],
})
export class LinkNodeComponent {
  readonly nodeId = input.required<number>();
  readonly nodeName = input.required<string>();
  protected readonly link = computed(() => '/analysis/node/' + this.nodeId());
}
