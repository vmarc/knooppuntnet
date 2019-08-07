import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {AnalysisSidebarComponent} from "./analysis-sidebar.component";
import {AnalysisBePageComponent} from "./pages/analysis/analysis-be-page.component";
import {AnalysisDePageComponent} from "./pages/analysis/analysis-de-page.component";
import {AnalysisNlPageComponent} from "./pages/analysis/analysis-nl-page.component";
import {AnalysisPageComponent} from "./pages/analysis/analysis-page.component";
import {ChangesPageComponent} from "./pages/changes/changes-page.component";
import {ChangeSetPageComponent} from "./pages/changeset/_change-set-page.component";
import {FactsPageComponent} from "./pages/facts/_facts-page.component";
import {NetworkChangesPageComponent} from "./pages/network/changes/_network-changes-page.component";
import {NetworkDetailsPageComponent} from "./pages/network/details/_network-details-page.component";
import {NetworkFactsPageComponent} from "./pages/network/facts/_network-facts-page.component";
import {NetworkMapPageComponent} from "./pages/network/map/_network-map-page.component";
import {NetworkNodesPageComponent} from "./pages/network/nodes/_network-nodes-page.component";
import {NetworkRoutesPageComponent} from "./pages/network/routes/_network-routes-page.component";
import {NodeChangesPageComponent} from "./pages/node/changes/_node-changes-page.component";
import {NodeDetailsPageComponent} from "./pages/node/details/_node-details-page.component";
import {NodeMapPageComponent} from "./pages/node/map/_node-map-page.component";
import {OverviewPageComponent} from "./pages/overview/_overview-page.component";
import {RouteChangesPageComponent} from "./pages/route/changes/_route-changes-page.component";
import {RoutePageComponent} from "./pages/route/details/_route-page.component";
import {RouteMapPageComponent} from "./pages/route/map/_route-map-page.component";
import {SubsetChangesPageComponent} from "./pages/subset/changes/_subset-changes-page.component";
import {SubsetFactDetailsPageComponent} from "./pages/subset/fact-details/_subset-fact-details-page.component";
import {SubsetFactsPageComponent} from "./pages/subset/facts/_subset-facts-page.component";
import {SubsetNetworksPageComponent} from "./pages/subset/networks/_subset-networks-page.component";
import {SubsetOrphanNodesPageComponent} from "./pages/subset/orphan-nodes/_subset-orphan-nodes-page.component";
import {SubsetOrphanRoutesPageComponent} from "./pages/subset/orphan-routes/_subset-orphan-routes-page.component";
import {NetworkRoutesSidebarComponent} from "./pages/network/routes/network-routes-sidebar.component";
import {NetworkNodesSidebarComponent} from "./pages/network/nodes/network-nodes-sidebar.component";

const routes: Routes = [
  {
    path: "",
    children: [
      {
        path: "",
        component: AnalysisPageComponent
      },
      {
        path: "",
        component: AnalysisSidebarComponent,
        outlet: "sidebar"
      }
    ]
  },
  {
    path: "nl",
    children: [
      {
        path: "",
        component: AnalysisNlPageComponent
      },
      {
        path: "",
        component: AnalysisSidebarComponent,
        outlet: "sidebar"
      }
    ]
  },
  {
    path: "be",
    children: [
      {
        path: "",
        component: AnalysisBePageComponent
      },
      {
        path: "",
        component: AnalysisSidebarComponent,
        outlet: "sidebar"
      }
    ]
  },
  {
    path: "de",
    children: [
      {
        path: "",
        component: AnalysisDePageComponent
      },
      {
        path: "",
        component: AnalysisSidebarComponent,
        outlet: "sidebar"
      }
    ]
  },
  {
    path: "changeset/:changeSetId/:replicationNumber",
    children: [
      {
        path: "",
        component: ChangeSetPageComponent
      },
      {
        path: "",
        component: AnalysisSidebarComponent,
        outlet: "sidebar"
      }
    ]
  },
  {
    path: "changes",
    children: [
      {
        path: "",
        component: ChangesPageComponent
      },
      {
        path: "",
        component: AnalysisSidebarComponent,
        outlet: "sidebar"
      }
    ]
  },
  {
    path: "network-changes/:networkId",
    children: [
      {
        path: "",
        component: NetworkChangesPageComponent
      },
      {
        path: "",
        component: AnalysisSidebarComponent,
        outlet: "sidebar"
      }
    ]
  },
  {
    path: "network-details/:networkId",
    children: [
      {
        path: "",
        component: NetworkDetailsPageComponent
      },
      {
        path: "",
        component: AnalysisSidebarComponent,
        outlet: "sidebar"
      }
    ]
  },
  {
    path: "network-facts/:networkId",
    children: [
      {
        path: "",
        component: NetworkFactsPageComponent
      },
      {
        path: "",
        component: AnalysisSidebarComponent,
        outlet: "sidebar"
      }
    ]
  },
  {
    path: "network-map/:networkId",
    children: [
      {
        path: "",
        component: NetworkMapPageComponent
      },
      {
        path: "",
        component: AnalysisSidebarComponent,
        outlet: "sidebar"
      }
    ]
  },
  {
    path: "network-nodes/:networkId",
    children: [
      {
        path: "",
        component: NetworkNodesPageComponent
      },
      {
        path: "",
        component: NetworkNodesSidebarComponent,
        outlet: "sidebar"
      }
    ]
  },
  {
    path: "network-routes/:networkId",
    children: [
      {
        path: "",
        component: NetworkRoutesPageComponent
      },
      {
        path: "",
        component: NetworkRoutesSidebarComponent,
        outlet: "sidebar"
      }
    ]
  },
  {
    path: "node/:nodeId",
    children: [
      {
        path: "",
        component: NodeDetailsPageComponent
      },
      {
        path: "",
        component: AnalysisSidebarComponent,
        outlet: "sidebar"
      }
    ]
  },
  {
    path: "node/:nodeId/map",
    children: [
      {
        path: "",
        component: NodeMapPageComponent
      },
      {
        path: "",
        component: AnalysisSidebarComponent,
        outlet: "sidebar"
      }
    ]
  },
  {
    path: "node/:nodeId/changes",
    children: [
      {
        path: "",
        component: NodeChangesPageComponent
      },
      {
        path: "",
        component: AnalysisSidebarComponent,
        outlet: "sidebar"
      }
    ]
  },
  {
    path: "overview",
    children: [
      {
        path: "",
        component: OverviewPageComponent
      },
      {
        path: "",
        component: AnalysisSidebarComponent,
        outlet: "sidebar"
      }
    ]
  },
  {
    path: "route/:routeId",
    children: [
      {
        path: "",
        component: RoutePageComponent
      },
      {
        path: "",
        component: AnalysisSidebarComponent,
        outlet: "sidebar"
      }
    ]
  },
  {
    path: "route/:routeId/map",
    children: [
      {
        path: "",
        component: RouteMapPageComponent
      },
      {
        path: "",
        component: AnalysisSidebarComponent,
        outlet: "sidebar"
      }
    ]
  },
  {
    path: "route/:routeId/changes",
    children: [
      {
        path: "",
        component: RouteChangesPageComponent
      },
      {
        path: "",
        component: AnalysisSidebarComponent,
        outlet: "sidebar"
      }
    ]
  },
  {
    path: ":country/:networkType/changes",
    children: [
      {
        path: "",
        component: SubsetChangesPageComponent
      },
      {
        path: "",
        component: AnalysisSidebarComponent,
        outlet: "sidebar"
      }
    ]
  },
  {
    path: ":country/:networkType/facts",
    children: [
      {
        path: "",
        component: SubsetFactsPageComponent
      },
      {
        path: "",
        component: AnalysisSidebarComponent,
        outlet: "sidebar"
      }
    ]
  },
  {
    path: ":country/:networkType/networks",
    children: [
      {
        path: "",
        component: SubsetNetworksPageComponent
      },
      {
        path: "",
        component: AnalysisSidebarComponent,
        outlet: "sidebar"
      }
    ]
  },
  {
    path: ":country/:networkType/orphan-nodes",
    children: [
      {
        path: "",
        component: SubsetOrphanNodesPageComponent
      },
      {
        path: "",
        component: AnalysisSidebarComponent,
        outlet: "sidebar"
      }
    ]
  },
  {
    path: ":country/:networkType/orphan-routes",
    children: [
      {
        path: "",
        component: SubsetOrphanRoutesPageComponent
      },
      {
        path: "",
        component: AnalysisSidebarComponent,
        outlet: "sidebar"
      }
    ]
  },
  {
    path: ":country/:networkType/:fact",
    children: [
      {
        path: "",
        component: SubsetFactDetailsPageComponent
      },
      {
        path: "",
        component: AnalysisSidebarComponent,
        outlet: "sidebar"
      }
    ]
  },
  {
    path: "facts",
    children: [
      {
        path: "",
        component: FactsPageComponent
      },
      {
        path: "",
        component: AnalysisSidebarComponent,
        outlet: "sidebar"
      }
    ]
  }
];

@NgModule({
  imports: [
    RouterModule.forChild(routes)
  ],
  exports: [
    RouterModule
  ]
})
export class AnalysisRoutingModule {
}
