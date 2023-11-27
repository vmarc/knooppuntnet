import { OnInit } from '@angular/core';
import { Input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';

@Component({
  selector: 'kpn-symbol-hiker',
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
          <!-- head -->
          <ellipse
            style="color-rendering:auto;color:#000000;isolation:auto;mix-blend-mode:normal;shape-rendering:auto;solid-color:#000000;image-rendering:auto"
            fill-rule="evenodd"
            rx="1.6058"
            ry="1.31515"
            cy="1037.75"
            cx="8.52318"
            [attr.fill]="color"
          />

          <!-- backpack -->
          <rect
            stroke-linejoin="round"
            height="4.02925"
            [attr.stroke]="color"
            stroke-width="1.10218"
            [attr.fill]="color"
            style="color-rendering:auto;color:#000000;isolation:auto;mix-blend-mode:normal;shape-rendering:auto;solid-color:#000000;image-rendering:auto"
            fill-rule="evenodd"
            transform="matrix(.982526 .186127 -.253287 .967391 0 0)"
            width="1.94005"
            y="1022.33"
            x="266.895"
          />
          <g [attr.stroke]="color" stroke-linecap="round" fill="none">
            <!-- stick -->
            <path d="m13.7698 1041.13-2.46386 10.7764" stroke-width=".611824" />

            <!-- arm -->
            <path
              stroke-linejoin="round"
              d="m12.2234 1043.68-2.59491-0.6011-0.812548-2.4044"
              stroke-width="1.22365px"
            />

            <!-- body -->
            <g>
              <path d="m3.99305 1051.34 2.51628-8.0072" stroke-width="1.83547" />
              <path d="m9.33184 1051.32-2.28038-8.0072" stroke-width="1.83547" />
              <path d="m7.84611 1040.87-0.996027 3.4561" stroke-width="3.05912" />
            </g>
          </g>
        </g>
      </svg>
    </div>
  `,
  standalone: true,
})
export class SymbolHikerComponent implements OnInit {
  @Input({ required: false }) color = '#000000';
  @Input({ required: false }) width = 50;
  @Input({ required: false }) height = 50;

  scaledWidth = 0;
  scaledHeight = 0;

  box = '';

  ngOnInit(): void {
    this.scaledWidth = (this.width * 2) / 3;
    this.scaledHeight = (this.height * 2) / 3;
    const x = this.width / 6;
    const y = this.height / 6;
    this.box = `padding-left: ${x}px; padding-top: ${y}px;`;
  }
}
