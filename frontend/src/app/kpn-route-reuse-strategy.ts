import { ActivatedRouteSnapshot } from '@angular/router';
import { BaseRouteReuseStrategy } from '@angular/router';

export class KpnRouteReuseStrategy extends BaseRouteReuseStrategy {
  override shouldReuseRoute(future: ActivatedRouteSnapshot, curr: ActivatedRouteSnapshot): boolean {
    return false;
  }
}
