import { inject } from "@angular/core";
import { OnDestroy } from "@angular/core";
import { OnInit } from "@angular/core";
import { Component } from "@angular/core";
import { PageComponent } from "./page.component";
import { Page1Service } from "./page1.service";

@Component({
  selector: "kpn-page-1",
  template: `
    <kpn-page title="Page1">
      <p>page one</p>
      @if (page(); as page) {
        {{ page }}
      }
    </kpn-page>
  `,
  providers: [Page1Service],
  standalone: true,
  imports: [PageComponent],
})
export class Page1Component implements OnInit, OnDestroy {
  private readonly service = inject(Page1Service);
  readonly page = this.service.page;

  constructor() {
    console.log(`Page1Component.constructor()`);
  }

  ngOnInit(): void {
    console.log(`Page1Component.ngOnInit()`);
  }

  ngOnDestroy(): void {
    console.log(`Page1Component.ngOnDestroy()`);
  }

}
