import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {finalize} from "rxjs/operators";
import {SpinnerService} from "./spinner.service";

@Injectable()
export class SpinnerInterceptor implements HttpInterceptor {

  constructor(private spinnerService: SpinnerService) {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const action = "http-request";
    this.spinnerService.start(action);
    return next.handle(request).pipe(finalize(() => this.spinnerService.end(action)));
  }
}
