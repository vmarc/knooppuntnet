import {Injectable} from "@angular/core";
import {Resolve, Router} from "@angular/router";
import {Route} from "../model";
import {NetworkService, PDFService, RouteDetailsService} from "../service";
import {TranslateService} from "@ngx-translate/core";
import {catchError} from "rxjs/operators";
import {EMPTY} from "rxjs";

@Injectable()
export class PdfResolver implements Resolve<any> {

  route: Route;
  currentLanguage: string;
  currentType: string;

  constructor(private routeDetailsService: RouteDetailsService,
              private router: Router,
              private netWorkService: NetworkService,
              private translateService: TranslateService,
              private pdfService: PDFService) {
  }

  resolve() {
    this.currentLanguage = this.translateService.currentLang;
    this.netWorkService.networkObservable.subscribe(response => this.currentType = response);
    this.routeDetailsService.routeObservable.subscribe(response => this.route = response);

    return this.pdfService.downloadPDF(this.currentLanguage, this.currentType, this.route).pipe(
      catchError(() => {
        this.router.navigate(["/"]);
        return EMPTY;
      }));
  }
}
