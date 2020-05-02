import {ChangeDetectionStrategy} from "@angular/core";
import {AfterViewInit, Component, ElementRef, Input, OnChanges, SimpleChanges, ViewChild} from "@angular/core";
import {PageService} from "../page.service";

@Component({
  selector: "kpn-page-header",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="header">
      <h1 #title>
        <ng-content></ng-content>
      </h1>
      <kpn-doc-link *ngIf="subject" [subject]="subject"></kpn-doc-link>
    </div>
  `,
  styles: [`

    .header {
      display: flex;
    }

    .header h1 {
      display: inline-block;
      flex: 1;
    }

  `]
})
export class PageHeaderComponent implements AfterViewInit, OnChanges {

  @Input() subject: string;
  @Input() pageTitle: string;

  @ViewChild("title", { read: ElementRef, static: true }) renderedTitle: ElementRef;

  constructor(private pageService: PageService) {
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes["pageTitle"]) {
      this.updatePageTitle();
    }
  }

  ngAfterViewInit(): void {
    this.updatePageTitle();
  }

  private updatePageTitle(): void {
    if (this.pageTitle || this.pageTitle === null) {
      this.pageService.setTitle(this.pageTitle);
    } else {
      const titleFromPage = this.renderedTitle.nativeElement.textContent.trim();
      this.pageService.setTitle(titleFromPage);
    }
  }

}
