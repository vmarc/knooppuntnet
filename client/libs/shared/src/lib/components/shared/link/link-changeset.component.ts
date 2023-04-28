import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'kpn-link-changeset',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <a
      [routerLink]="
        '/analysis/changeset/' + changeSetId + '/' + replicationNumber
      "
      >{{ changeSetId }}</a
    >
  `,
  standalone: true,
  imports: [RouterLink],
})
export class LinkChangesetComponent {
  @Input() changeSetId: number;
  @Input() replicationNumber: number;
}
