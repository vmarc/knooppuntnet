import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'kpn-json',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- eslint-disable @angular-eslint/template/i18n -->
    <br />
    <br />
    <button (click)="toggleVisible()">JSON</button>
    <div *ngIf="visible">
      <br />
      <pre
        >{{ contents }}
</pre
      >
    </div>
    <br />
    <br />
  `,
})
export class JsonComponent implements OnInit {
  @Input() object: any;
  contents = '';
  visible = false;

  ngOnInit(): void {
    this.contents = JSON.stringify(this.object, null, 2);
  }

  toggleVisible(): void {
    this.visible = !this.visible;
  }
}
