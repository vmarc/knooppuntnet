import {Component, Input} from "@angular/core";
import {RouteChangeInfo} from "../../../kpn/shared/route/route-change-info";

@Component({
  selector: "kpn-route-change-detail",
  template: `

    <kpn-route-diff [diffs]="routeChangeInfo.diffs"></kpn-route-diff>
    
    
    <!--
		  <.div(
	
			renderNodes(routeChangeInfo, "Added network node", "Toegevoegd knooppunt", routeChangeInfo.addedNodes),
			renderNodes(routeChangeInfo, "Deleted network node", "Verwijderd knooppunt", routeChangeInfo.deletedNodes),
			renderNodes(routeChangeInfo, "Unchanged network node", "Onveranderd knooppunt", routeChangeInfo.commonNodes),
	
			TagMod.when(routeChangeInfo.addedWayIds.nonEmpty) {
			  UiDetail(nls("Added ways", "Toegevoegde wegen") + "=" + routeChangeInfo.addedWayIds.mkString(", "))
			},
			TagMod.when(routeChangeInfo.deletedWayIds.nonEmpty) {
			  UiDetail(nls("Deleted ways", "Verwijderde wegen") + "=" + routeChangeInfo.deletedWayIds.mkString(", "))
			},
			TagMod.when(routeChangeInfo.geometryDiff.isEmpty) {
			  UiDetail(nls("No geometry change", "Geometrie niet veranderd"))
			},
			routeChangeInfo.geometryDiff.whenDefined { geometryDiff =>
			  UiDetail(
				UiSmallMap(
				  new RouteHistoryMap(
					"map-" + key,
					routeChangeInfo.nodes,
					routeChangeInfo.bounds,
					geometryDiff
				  )
				)
			  )
			},
			routeRemovedWays(routeChangeInfo.removedWays),
			routeAddedWays(routeChangeInfo.addedWays),
			routeUpdatedWays(routeChangeInfo.updatedWays)
		  )
	
	
	-->  `
})
export class RouteChangeDetailComponent {

  @Input() routeChangeInfo: RouteChangeInfo;


}
