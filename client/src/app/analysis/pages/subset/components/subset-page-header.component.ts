import {Component, ElementRef, Input, SimpleChanges, ViewChild} from "@angular/core";
import {Util} from "../../../../components/shared/util";
import {Subset} from "../../../../kpn/shared/subset";
import {SubsetInfo} from "../../../../kpn/shared/subset/subset-info";
import {SubsetCacheService} from "../../../../services/subset-cache.service";

@Component({
  selector: "kpn-subset-page-header",
  template: `
    <kpn-page-header [pageTitle]="subsetPageTitle" [subject]="'subset-' + pageName + '-page'">
      <kpn-subset-name #title [subset]="subset"></kpn-subset-name>
    </kpn-page-header>

  `
})
export class SubsetPageHeaderComponent {

  @Input() subset: Subset;
  @Input() pageName: string;
  @Input() pageTitle: string;

  subsetPageTitle: string = "";

  @ViewChild("title", {read: ElementRef}) renderedTitle: ElementRef;

  constructor(private subsetCacheService: SubsetCacheService) {
  }

  countryLink(): string {
    return "/analysis/" + Util.safeGet(() => this.subset.country.domain);
  }

  link(targetPageName: string) {
    if (this.subset != null) {
      return `/analysis/${this.subset.country.domain}/${this.subset.networkType.name}/${targetPageName}`;
    }
    return "/";
  }

  networkCount() {
    return Util.safeGet(() => this.subsetInfo().networkCount);
  }

  factCount() {
    return Util.safeGet(() => this.subsetInfo().factCount);
  }

  orphanNodeCount() {
    return Util.safeGet(() => this.subsetInfo().orphanNodeCount);
  }

  orphanRouteCount() {
    return Util.safeGet(() => this.subsetInfo().orphanRouteCount);
  }

  subsetInfo(): SubsetInfo {
    return this.subsetCacheService.getSubsetInfo(this.subset.key());
  }

  ngAfterViewInit(): void {
    this.updatePageTitle();
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes["pageTitle"]) {
      this.updatePageTitle();
    }
  }

  private updatePageTitle() {
    const titleFromPage = this.renderedTitle.nativeElement.textContent.trim();
    this.subsetPageTitle =  `${titleFromPage} | ${this.pageTitle}`;
  }

}
