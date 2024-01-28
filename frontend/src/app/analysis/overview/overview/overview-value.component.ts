import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Subset } from '@api/custom';
import { Stat } from '../domain/stat';

@Component({
  selector: 'kpn-overview-value',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (hasLink()) {
      <a [routerLink]="link()">{{ value() }}</a>
    } @else {
      <span>{{ value() }}</span>
    }
  `,
  styles: `
    :host {
      display: contents;
    }
  `,
  standalone: true,
  imports: [RouterLink],
})
export class OverviewValueComponent {
  stat = input<Stat | undefined>();
  subset = input<Subset | undefined>();

  hasLink() {
    return this.stat().configuration.linkFunction !== null;
  }

  value(): string {
    return this.stat().value(this.subset());
  }

  link() {
    return (
      '/analysis/' +
      this.stat().configuration.linkFunction(this.stat().configuration.fact, this.subset())
    );
  }
}
