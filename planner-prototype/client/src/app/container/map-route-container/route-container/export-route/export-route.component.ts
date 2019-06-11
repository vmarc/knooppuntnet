import {Component, OnInit} from "@angular/core";
import {Route, RouteState} from "../../../../model";
import {TranslateService} from "@ngx-translate/core";
import {GpxService, NetworkService, RouteDetailsService, RouteStateService} from "../../../../service";

import {Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: "app-export-route",
  templateUrl: "./export-route.component.html",
  styleUrls: ["./export-route.component.scss"]
})
export class ExportRouteComponent implements OnInit {

  routeState: RouteState;
  currentRoute: Route;

  constructor(private translate: TranslateService,
              private netWorkService: NetworkService,
              private routeStateService: RouteStateService,
              private translateService: TranslateService,
              private toastr: ToastrService,
              private router: Router,
              private gpxService: GpxService,
              private routeDetailsService: RouteDetailsService) {
  }

  ngOnInit() {
    this.routeDetailsService.routeStateObservable.subscribe(response => this.routeState = response);
    this.routeDetailsService.routeObservable.subscribe(response => this.currentRoute = response);
  }

  downloadPDF() {
    if (this.currentRoute && this.routeState.selectedRoute.selectedNodesByUser.length >= 2) {
      this.router.navigate(["/knooppuntnet/export/pdf"]);
    } else {
      this.translate.get("NO_ROUTE_SELECTED").subscribe(response => this.toastr.error(response));
    }
  }

  downloadGPX() {
    if (this.currentRoute && this.routeState.selectedRoute.selectedNodesByUser.length >= 2) {
      this.gpxService.downloadGPX(this.currentRoute).subscribe(response => {
        const fileName = "knooppuntnet.gpx";
        const objectUrl = URL.createObjectURL(response);
        const a = document.createElement("a") as HTMLAnchorElement;

        a.href = objectUrl;
        a.download = fileName;
        document.body.appendChild(a);
        a.click();

        document.body.removeChild(a);
        URL.revokeObjectURL(objectUrl);
      }, () => {
        this.translate.get("GPX_SERVICE").subscribe(response => this.toastr.error(response));
      });
    } else {
      this.translate.get("NO_ROUTE_SELECTED").subscribe(response => this.toastr.error(response));
    }
  }

  downloadCompact() {
    if (this.currentRoute && this.routeState.selectedRoute.selectedNodesByUser.length >= 2) {
      this.router.navigate(["/knooppuntnet/export/compact"]);
    } else {
      this.translate.get("NO_ROUTE_SELECTED").subscribe(response => this.toastr.error(response));
    }
  }

  homePage() {
    this.router.navigate(["/knooppuntnet"]);
  }
}
