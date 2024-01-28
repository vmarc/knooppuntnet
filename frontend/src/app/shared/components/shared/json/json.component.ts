import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { input } from '@angular/core';

@Component({
  selector: 'kpn-json',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- eslint-disable @angular-eslint/template/i18n -->
    <br />
    <br />
    <button (click)="toggleVisible()">JSON</button>
    @if (visible) {
      <div>
        <br />
        <pre
          >{{ contents }}
          </pre
        >
      </div>
    }
    <br />
    <br />
  `,
  standalone: true,
  imports: [],
})
export class JsonComponent implements OnInit {
  object = input.required<any>();
  contents = '';
  visible = false;

  ngOnInit(): void {
    this.contents = JSON.stringify(this.object(), null, 2);
  }

  toggleVisible(): void {
    this.visible = !this.visible;
  }
}
