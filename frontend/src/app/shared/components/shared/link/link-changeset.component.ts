import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'kpn-link-changeset',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <a [routerLink]="'/analysis/changeset/' + changeSetId() + '/' + replicationNumber()">{{
      changeSetId()
    }}</a>
  `,
  standalone: true,
  imports: [RouterLink],
})
export class LinkChangesetComponent {
  changeSetId = input.required<number>();
  replicationNumber = input<number | undefined>();
}
