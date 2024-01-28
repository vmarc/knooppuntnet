import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';

@Component({
  selector: 'kpn-symbol-wheel',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div [style]="box">
      <svg
        xmlns="http://www.w3.org/2000/svg"
        [attr.width]="scaledWidth"
        [attr.height]="scaledHeight"
        viewBox="0 0 16 16"
      >
        <g transform="translate(0 -1036.36)">
          <circle
            style="color-rendering:auto;color():#000000;isolation:auto;mix-blend-mode:normal;shape-rendering:auto;solid-color():#000000;image-rendering:auto"
            cx="8.03898"
            [attr.stroke]="color()"
            cy="1044.36"
            r="6.96455"
            stroke-width="1.30268"
            fill="none"
          />
          <circle
            style="color-rendering:auto;color():#000000;isolation:auto;mix-blend-mode:normal;shape-rendering:auto;solid-color():#000000;image-rendering:auto"
            cx="8"
            [attr.stroke]="color()"
            cy="1044.36"
            r="1.2141"
            fill="none"
          />
          <g fill-rule="evenodd" [attr.fill]="color()">
            <rect
              style="color-rendering:auto;color():#000000;isolation:auto;mix-blend-mode:normal;shape-rendering:auto;solid-color():#000000;image-rendering:auto"
              height="1.21769"
              width="4.93484"
              y="1043.77"
              x="1.66631"
            />
            <rect
              style="color-rendering:auto;color():#000000;isolation:auto;mix-blend-mode:normal;shape-rendering:auto;solid-color():#000000;image-rendering:auto"
              height="1.21769"
              width="4.93484"
              y="1043.75"
              x="9.51719"
            />
          </g>
          <g fill-rule="evenodd" transform="rotate(60.0279 8.03861 1044.32)" [attr.fill]="color()">
            <rect
              style="color-rendering:auto;color():#000000;isolation:auto;mix-blend-mode:normal;shape-rendering:auto;solid-color():#000000;image-rendering:auto"
              height="1.21769"
              width="4.93484"
              y="1043.77"
              x="1.66631"
            />
            <rect
              style="color-rendering:auto;color():#000000;isolation:auto;mix-blend-mode:normal;shape-rendering:auto;solid-color():#000000;image-rendering:auto"
              height="1.21769"
              width="4.93484"
              y="1043.75"
              x="9.51719"
            />
          </g>
          <g fill-rule="evenodd" transform="rotate(119.973 8.03261 1044.35)" [attr.fill]="color()">
            <rect
              style="color-rendering:auto;color():#000000;isolation:auto;mix-blend-mode:normal;shape-rendering:auto;solid-color():#000000;image-rendering:auto"
              height="1.21769"
              width="4.93484"
              y="1043.77"
              x="1.66631"
            />
            <rect
              style="color-rendering:auto;color():#000000;isolation:auto;mix-blend-mode:normal;shape-rendering:auto;solid-color():#000000;image-rendering:auto"
              height="1.21769"
              width="4.93484"
              y="1043.75"
              x="9.51719"
            />
          </g>
        </g>
      </svg>
    </div>
  `,
  standalone: true,
})
export class SymbolWheelComponent implements OnInit {
  color = input('#000000');
  width = input(50);
  height = input(50);

  scaledWidth = 0;
  scaledHeight = 0;

  box = '';

  ngOnInit(): void {
    this.scaledWidth = (this.width() * 2) / 3;
    this.scaledHeight = (this.height() * 2) / 3;
    const x = this.width() / 6;
    const y = this.height() / 6;
    this.box = `padding-left: ${x}px; padding-top: ${y}px;`;
  }
}
