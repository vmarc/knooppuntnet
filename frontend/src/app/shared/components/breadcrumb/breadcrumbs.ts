import { BreadcrumbItem } from '@app/shared/components/breadcrumb/breadcrumb-item';

export class Breadcrumbs {
  static readonly homeLabel: string = $localize`:@@breadcrumb.home:Home`;
  static readonly analysisLabel: string = $localize`:@@breadcrumb.analysis:Analysis`;
  static readonly changesLabel: string = $localize`:@@breadcrumb.changes:Changes`;
  static readonly networkLabel: string = $localize`:@@breadcrumb.network:Network`;
  static readonly nodeChangesLabel: string = $localize`:@@breadcrumb.node-changes:Node changes`;
  static readonly routeChangesLabel: string = $localize`:@@breadcrumb.route-changes:Route changes`;
  static readonly groupChangesLabel: string = $localize`:@@breadcrumb.group-changes:Group changes`;
  static readonly overviewLabel: string = $localize`:@@breadcrumb.overview:Overview`;
  static readonly routeLabel: string = $localize`:@@breadcrumb.route:Route`;
  static readonly routeMapLabel: string = $localize`:@@breadcrumb.route-map:Route map`;
  static readonly monitorLabel: string = $localize`:@@breadcrumb.monitor:Monitor`;
  static readonly monitorGroupLabel: string = $localize`:@@breadcrumb.monitor.group:Group`;
  static readonly settingsLabel: string = $localize`:@@breadcrumb.settings:Settings`;
  static readonly statusLabel: string = $localize`:@@breadcrumb.status:Status`;
  static readonly logAnalysisLabel: string = $localize`:@@breadcrumb.log-analysis:Log analysis`;
  static readonly replicationLabel: string = $localize`:@@breadcrumb.replication:Replication`;
  static readonly systemLabel: string = $localize`:@@breadcrumb.system:System`;
  static readonly monitorRouteLabel: string = $localize`:@@breadcrumb.monitor.route:Route`;
  static readonly monitorRouteGpxLabel: string = $localize`:@@breadcrumb.monitor.route.gpx:gpx`;

  static readonly home: BreadcrumbItem = {
    routerLink: '/',
    label: Breadcrumbs.homeLabel,
  };

  static readonly analysis: BreadcrumbItem = {
    routerLink: '/analysis',
    label: Breadcrumbs.analysisLabel,
  };

  static readonly monitor: BreadcrumbItem = {
    routerLink: '/monitor',
    label: Breadcrumbs.monitorLabel,
  };

  static readonly monitorRoutes: BreadcrumbItem = {
    routerLink: '/monitor/routes',
    label: Breadcrumbs.monitorLabel,
  };

  static readonly status: BreadcrumbItem = {
    routerLink: '/status',
    label: Breadcrumbs.statusLabel,
  };
}
