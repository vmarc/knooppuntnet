import { NgIf } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Subset } from '@api/custom';
import { Stat } from '../domain/stat';

@Component({
  selector: 'kpn-overview-value',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <a *ngIf="hasLink()" [routerLink]="link()">{{ value() }}</a>
    <span *ngIf="!hasLink()">{{ value() }}</span>
  `,
  styles: `
    :host {
      display: contents;
    }
  `,
  standalone: true,
  imports: [NgIf, RouterLink],
})
export class OverviewValueComponent {
  @Input() stat: Stat;
  @Input() subset: Subset;

  hasLink() {
    return this.stat.configuration.linkFunction !== null;
  }

  value(): string {
    return this.stat.value(this.subset);
  }

  link() {
    return (
      '/analysis/' +
      this.stat.configuration.linkFunction(
        this.stat.configuration.fact,
        this.subset
      )
    );
  }
}
