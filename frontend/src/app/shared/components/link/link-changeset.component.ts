import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'ui-link-changeset',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <a [routerLink]="'/analysis/changeset/' + changeSetId() + '/' + replicationNumber()">{{
      changeSetId()
    }}</a>
  `,
  imports: [RouterLink],
})
export class LinkChangesetComponent {
  readonly changeSetId = input.required<number>();
  readonly replicationNumber = input.required<number>();
}
