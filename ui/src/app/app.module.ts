import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LayoutModule} from '@angular/cdk/layout';


import {AppComponent} from './app.component';
import {AppService} from './app.service';
import {HttpClientModule} from '@angular/common/http';
import {Page1Component} from './pages/page1/page1.component';
import {Page2Component} from './pages/page2/page2.component';
import {Page3Component} from './pages/page3/page3.component';
import {RoutePageComponent} from './pages/route/_page/route-page.component';
import {AuthenticatePageComponent} from './pages/authenticate/_page/authenticate-page.component';
import {LoginPageComponent} from './pages/login/_page/login-page.component';
import {LogoutPageComponent} from './pages/logout/_page/logout-page.component';
import {HomePageComponent} from './pages/home/_page/home-page.component';
import {NotFoundPageComponent} from './pages/not-found/_page/not-found-page.component';
import {NodePageComponent} from './pages/node/_page/node-page.component';
import {ChangesPageComponent} from './pages/changes/_page/changes-page.component';
import {SubsetOrphanNodesPageComponent} from './pages/subset-orphan-nodes/_page/subset-orphan-nodes-page.component';
import {SubsetOrphanRoutesPageComponent} from './pages/subset-orphan-routes/_page/subset-orphan-routes-page.component';
import {SubsetChangesPageComponent} from './pages/subset-changes/_page/subset-changes-page.component';
import {SubsetNetworksPageComponent} from './pages/subset-networks/_page/subset-networks-page.component';
import {SubsetFactsPageComponent} from './pages/subset-facts/_page/subset-facts-page.component';
import {SubsetFactDetailsPageComponent} from './pages/subset-fact-details/_page/subset-fact-details-page.component';
import {MapPageComponent} from './pages/map/_page/map-page.component';
import {ChangeSetPageComponent} from './pages/changeset/_page/change-set-page.component';
import {NetworkNodesPageComponent} from './pages/network-nodes/_page/network-nodes-page.component';
import {NetworkRoutesPageComponent} from './pages/network-routes/_page/network-routes-page.component';
import {NetworkDetailsPageComponent} from './pages/network-details/_page/network-details-page.component';
import {NetworkChangesPageComponent} from './pages/network-changes/_page/network-changes-page.component';
import {NetworkMapPageComponent} from './pages/network-map/_page/network-map-page.component';
import {NetworkFactsPageComponent} from './pages/network-facts/_page/network-facts-page.component';
import {OverviewPageComponent} from './pages/overview/_page/overview-page.component';
import {AboutPageComponent} from './pages/about/_page/about-page.component';
import {GlossaryPageComponent} from './pages/glossary/_page/glossary-page.component';
import {LinksPageComponent} from './pages/links/_page/links-page.component';
import {KpnMaterialModule} from "./material/kpn-material.module";
import {PageComponent} from './components/page/page.component';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";

const routes: Routes = [
  {
    path: 'about',
    component: AboutPageComponent
  },
  {
    path: 'authenticate',
    component: AuthenticatePageComponent
  },
  {
    path: 'changeset',
    component: ChangeSetPageComponent
  },
  {
    path: 'changes',
    component: ChangesPageComponent
  },
  {
    path: 'glossary',
    component: GlossaryPageComponent
  },
  {
    path: 'home',
    component: HomePageComponent
  },
  {
    path: 'links',
    component: LinksPageComponent
  },
  {
    path: 'login',
    component: LoginPageComponent
  },
  {
    path: 'logout',
    component: LogoutPageComponent
  },
  {
    path: 'map',
    component: MapPageComponent
  },
  {
    path: 'network-changes',
    component: NetworkChangesPageComponent
  },
  {
    path: 'network-details',
    component: NetworkDetailsPageComponent
  },
  {
    path: 'network-facts',
    component: NetworkFactsPageComponent
  },
  {
    path: 'network-map',
    component: NetworkMapPageComponent
  },
  {
    path: 'network-nodes',
    component: NetworkNodesPageComponent
  },
  {
    path: 'network-routes',
    component: NetworkRoutesPageComponent
  },
  {
    path: 'node',
    component: NodePageComponent
  },
  {
    path: 'not-found',
    component: NotFoundPageComponent
  },
  {
    path: 'overview',
    component: OverviewPageComponent
  },
  {
    path: 'route',
    component: RoutePageComponent
  },
  {
    path: 'changes/nl/rwn',
    component: SubsetChangesPageComponent
  },
  {
    path: 'RouteBroken/nl/rwn',
    component: SubsetFactDetailsPageComponent
  },
  {
    path: 'facts/nl/rwn',
    component: SubsetFactsPageComponent
  },
  {
    path: 'networks/nl/rwn',
    component: SubsetNetworksPageComponent
  },
  {
    path: 'orphan-nodes/nl/rwn',
    component: SubsetOrphanNodesPageComponent
  },
  {
    path: 'orphan-routes/nl/rwn',
    component: SubsetOrphanRoutesPageComponent
  },
  {
    path: '**',
    redirectTo: '/not-found',
    pathMatch: 'full'
  }

];

@NgModule({
  declarations: [
    AppComponent,
    Page1Component,
    Page2Component,
    Page3Component,
    AboutPageComponent,
    AuthenticatePageComponent,
    ChangeSetPageComponent,
    ChangesPageComponent,
    GlossaryPageComponent,
    HomePageComponent,
    LinksPageComponent,
    LoginPageComponent,
    LogoutPageComponent,
    MapPageComponent,
    NetworkChangesPageComponent,
    NetworkDetailsPageComponent,
    NetworkFactsPageComponent,
    NetworkMapPageComponent,
    NetworkNodesPageComponent,
    NetworkRoutesPageComponent,
    NodePageComponent,
    NotFoundPageComponent,
    OverviewPageComponent,
    RoutePageComponent,
    SubsetChangesPageComponent,
    SubsetFactDetailsPageComponent,
    SubsetFactsPageComponent,
    SubsetNetworksPageComponent,
    SubsetOrphanNodesPageComponent,
    SubsetOrphanRoutesPageComponent,
    PageComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    LayoutModule,
    KpnMaterialModule,
    HttpClientModule,
    RouterModule.forRoot(routes)
  ],
  providers: [AppService],
  bootstrap: [AppComponent]
})
export class AppModule {
}
