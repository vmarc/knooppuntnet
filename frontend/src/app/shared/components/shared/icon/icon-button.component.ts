import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'kpn-icon-button',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <a [routerLink]="routerLink">
      <div class="wrapper">
        <mat-icon [svgIcon]="icon" class="icon"></mat-icon>
      </div>
      <div class="text">
        {{ title }}
      </div>
    </a>
  `,
  styles: `
    :host {
      display: inline-block;
      margin: 15px;
    }

    .wrapper {
      display: inline-block;
      border-color: grey;
      border-radius: 50%;
      border-style: solid;
      border-width: 3px;
      width: 100px;
      height: 100px;
      background: #f8f8f8;
    }

    .wrapper:hover {
      background-color: #e8ebee;
    }

    .text {
      width: 100px;
      margin-top: 5px;
      text-align: center;
    }

    .icon {
      position: relative;
      top: 25px;
      left: 25px;
      width: 50px;
      height: 50px;
      border: none;
      color: grey;
    }
  `,
  standalone: true,
  imports: [RouterLink, MatIconModule],
})
export class IconButtonComponent {
  @Input({ required: true }) title: string;
  @Input({ required: true }) routerLink: string;
  @Input({ required: true }) icon: string;
}
