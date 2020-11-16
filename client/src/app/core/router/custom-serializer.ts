import {Injectable} from '@angular/core';
import {RouterStateSnapshot} from '@angular/router';
import {RouterStateSerializer} from '@ngrx/router-store';
import {RouterStateUrl} from './router.state';

@Injectable()
export class CustomSerializer implements RouterStateSerializer<RouterStateUrl> {

  serialize(routerState: RouterStateSnapshot): RouterStateUrl {

    console.log('CustomSerializer');

    let route = routerState.root;

    while (route.firstChild) {
      route = route.firstChild;
    }

    console.log('CustomSerializer.route=' + JSON.stringify(route, null ,2));

    const {url, root: {queryParams}} = routerState;
    const {params} = route;

    return {url, params, queryParams};
  }
}
