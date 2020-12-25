import {routerNavigationAction} from '@ngrx/router-store';
import {createReducer} from '@ngrx/store';
import {on} from '@ngrx/store';
import {actionMonitorChangesPageLoaded} from './monitor.actions';
import {actionMonitorGroupChangesPageLoaded} from './monitor.actions';
import {actionMonitorGroupPageLoaded} from './monitor.actions';
import {actionMonitorNavigateGroup} from './monitor.actions';
import {actionLongdistanceRouteMapOsmRelationVisible} from './monitor.actions';
import {actionLongdistanceRouteMapNokVisible} from './monitor.actions';
import {actionLongdistanceRouteMapOkVisible} from './monitor.actions';
import {actionLongdistanceRouteMapReferenceVisible} from './monitor.actions';
import {actionLongdistanceRouteMapMode} from './monitor.actions';
import {actionLongdistanceRouteChangeLoaded} from './monitor.actions';
import {actionLongdistanceRouteChangesLoaded} from './monitor.actions';
import {actionLongdistanceRouteMapLoaded} from './monitor.actions';
import {actionLongdistanceRouteDetailsLoaded} from './monitor.actions';
import {actionLongdistanceRoutesLoaded} from './monitor.actions';
import {actionMonitorGroupUpdateLoaded} from './monitor.actions';
import {actionMonitorGroupDeleteLoaded} from './monitor.actions';
import {actionMonitorGroupsPageLoaded} from './monitor.actions';
import {actionMonitorAdmin} from './monitor.actions';
import {actionMonitorRouteChangeLoaded} from './monitor.actions';
import {actionMonitorRouteMapReferenceVisible} from './monitor.actions';
import {actionMonitorRouteMapOkVisible} from './monitor.actions';
import {actionMonitorRouteMapOsmRelationVisible} from './monitor.actions';
import {actionMonitorRouteMapNokVisible} from './monitor.actions';
import {actionMonitorRouteMapMode} from './monitor.actions';
import {actionMonitorRoutesLoaded} from './monitor.actions';
import {actionMonitorRouteChangesLoaded} from './monitor.actions';
import {actionMonitorRouteMapLoaded} from './monitor.actions';
import {actionMonitorRouteDetailsLoaded} from './monitor.actions';
import {initialState} from './monitor.state';

export const monitorReducer = createReducer(
  initialState,
  on(
    actionMonitorAdmin,
    (state, {admin}) => ({
      ...state,
      admin,
    })
  ),
  on(
    routerNavigationAction,
    (state, action) => ({
      ...state,
      details: null,
      changes: null,
      routeChangePage: null,
      map: null,
      mapMode: null,
      routeGroups: null,

      changesPage: null,
      groupsPage: null,
      groupPage: null,
      adminGroupPage: null
    })
  ),
  on(
    actionMonitorChangesPageLoaded,
    (state, {response}) => ({
      ...state,
      changesPage: response
    })
  ),
  on(
    actionMonitorGroupsPageLoaded,
    (state, {response}) => ({
      ...state,
      groupsPage: response
    })
  ),
  on(
    actionMonitorGroupPageLoaded,
    (state, {response}) => ({
      ...state,
      groupName: response?.result?.groupName ?? state.groupName,
      groupDescription: response?.result?.groupDescription ?? state.groupDescription,
      groupPage: response
    })
  ),
  on(
    actionMonitorGroupChangesPageLoaded,
    (state, {response}) => ({
      ...state,
      groupName: response?.result?.groupName ?? state.groupName,
      groupDescription: response?.result?.groupDescription ?? state.groupDescription,
      groupChangesPage: response
    })
  ),
  on(
    actionMonitorNavigateGroup,
    (state, {groupName, groupDescription}) => ({
      ...state,
      groupName,
      groupDescription
    })
  ),
  on(
    actionMonitorGroupDeleteLoaded,
    (state, {response}) => ({
      ...state,
      adminGroupPage: response
    })
  ),
  on(
    actionMonitorGroupUpdateLoaded,
    (state, {response}) => ({
      ...state,
      adminGroupPage: response
    })
  ),
  on(
    actionMonitorRoutesLoaded,
    (state, {response}) => ({
      ...state,
      routes: response
    })
  ),
  on(
    actionMonitorRouteDetailsLoaded,
    (state, {response}) => {
      const routeId = response.result?.routeId ?? state.routeId;
      const routeName = response.result?.routeName ?? state.routeName;
      const groupName = response.result?.groupName ?? state.groupName;
      const groupDescription = response.result?.groupDescription ?? state.groupDescription;
      return {
        ...state,
        routeId,
        routeName,
        groupName,
        groupDescription,
        details: response
      };
    }
  ),
  on(
    actionMonitorRouteMapLoaded,
    (state, {response}) => {

      const routeId = response.result?.routeId ?? state.routeId;
      const routeName = response.result?.routeName ?? state.routeName;
      const groupName = response.result?.groupName ?? state.groupName;
      const groupDescription = response.result?.groupDescription ?? state.groupDescription;
      const mapGpxVisible = false;
      const mapGpxOkVisible = !!(response.result?.okGeometry);
      const mapGpxNokVisible = (response.result?.nokSegments?.length ?? 0) > 0;
      const mapOsmRelationVisible = (response.result?.osmSegments?.length ?? 0) > 0;

      return {
        ...state,
        routeId,
        routeName,
        groupName,
        groupDescription,
        mapGpxVisible,
        mapGpxOkVisible,
        mapGpxNokVisible,
        mapOsmRelationVisible,
        mapMode: 'comparison',
        map: response
      };
    }
  ),
  on(
    actionMonitorRouteChangesLoaded,
    (state, {response}) => {
      const routeId = response.result?.routeId ?? state.routeId;
      const routeName = response.result?.routeName ?? state.routeName;
      const groupName = response.result?.groupName ?? state.groupName;
      const groupDescription = response.result?.groupDescription ?? state.groupDescription;
      return {
        ...state,
        routeId,
        routeName,
        groupName,
        groupDescription,
        changes: response
      };
    }
  ),
  on(
    actionMonitorRouteChangeLoaded,
    (state, {response}) => {
      const routeId = response.result?.key.elementId ?? state.routeId;
      const routeName = 'ROUTE-NAME'; // response.result?.name ?? state.routeName;
      return {
        ...state,
        routeId,
        routeName,
        routeChangePage: response
      };
    }
  ),
  on(
    actionMonitorRouteMapMode,
    (state, {mode}) => {

      const mapGpxVisible = false;
      let mapGpxOkVisible = false;
      let mapGpxNokVisible = false;
      let mapOsmRelationVisible = false;
      if (mode === 'comparison') {
        mapGpxOkVisible = !!(state.map?.result?.reference.geometry);
        mapGpxNokVisible = (state.map.result?.nokSegments?.length ?? 0) > 0;
        mapOsmRelationVisible = (state.map.result?.osmSegments?.length ?? 0) > 0;
      } else if (mode === 'osm-segments') {
        mapOsmRelationVisible = true;
      }

      return {
        ...state,
        mapGpxVisible,
        mapGpxOkVisible,
        mapGpxNokVisible,
        mapOsmRelationVisible,
        mapMode: mode
      };
    }
  ),
  on(
    actionMonitorRouteMapReferenceVisible,
    (state, {visible}) => ({
      ...state,
      mapGpxVisible: visible
    })
  ),
  on(
    actionMonitorRouteMapOkVisible,
    (state, {visible}) => ({
      ...state,
      mapGpxOkVisible: visible
    })
  ),
  on(
    actionMonitorRouteMapNokVisible,
    (state, {visible}) => ({
      ...state,
      mapGpxNokVisible: visible
    })
  ),
  on(
    actionMonitorRouteMapOsmRelationVisible,
    (state, {visible}) => ({
      ...state,
      mapOsmRelationVisible: visible
    })
  ),
  on(
    actionLongdistanceRoutesLoaded,
    (state, {response}) => {
      return {
        ...state,
        longdistanceRoutesPage: response
      };
    }
  ),
  on(
    actionLongdistanceRouteDetailsLoaded,
    (state, {response}) => {
      const longdistanceRouteId = response.result?.id ?? state.routeId;
      const longdistanceRouteName = response.result?.name ?? state.routeName;
      return {
        ...state,
        longdistanceRouteId,
        longdistanceRouteName,
        longdistanceRouteDetailsPage: response
      };
    }
  ),
  on(
    actionLongdistanceRouteMapLoaded,
    (state, {response}) => {

      const longdistanceRouteId = response.result?.id ?? state.routeId;
      const longdistanceRouteName = response.result?.name ?? state.routeName;
      const longdistanceRouteMapGpxVisible = false;
      const longdistanceRouteMapGpxOkVisible = !!(response.result?.okGeometry);
      const longdistanceRouteMapGpxNokVisible = (response.result?.nokSegments?.length ?? 0) > 0;
      const longdistanceRouteMapOsmRelationVisible = (response.result?.osmSegments?.length ?? 0) > 0;

      return {
        ...state,
        longdistanceRouteId,
        longdistanceRouteName,
        longdistanceRouteMapGpxVisible,
        longdistanceRouteMapGpxOkVisible,
        longdistanceRouteMapGpxNokVisible,
        longdistanceRouteMapOsmRelationVisible,
        longdistanceRouteMapMode: 'comparison',
        longdistanceRouteMapPage: response
      };
    }
  ),
  on(
    actionLongdistanceRouteChangesLoaded,
    (state, {response}) => {
      const longdistanceRouteId = response.result?.id ?? state.routeId;
      const longdistanceRouteName = response.result?.name ?? state.routeName;
      return {
        ...state,
        longdistanceRouteId,
        longdistanceRouteName,
        longdistanceRouteChangesPage: response
      };
    }
  ),
  on(
    actionLongdistanceRouteChangeLoaded,
    (state, {response}) => {
      const longdistanceRouteId = response.result?.id ?? state.routeId;
      const longdistanceRouteName = response.result?.name ?? state.routeName;
      return {
        ...state,
        longdistanceRouteId,
        longdistanceRouteName,
        longdistanceRouteChangePage: response
      };
    }
  ),
  on(
    actionLongdistanceRouteMapMode,
    (state, {mode}) => {

      const longdistanceRouteMapGpxVisible = false;
      let longdistanceRouteMapGpxOkVisible = false;
      let longdistanceRouteMapGpxNokVisible = false;
      let longdistanceRouteMapOsmRelationVisible = false;
      if (mode === 'comparison') {
        longdistanceRouteMapGpxOkVisible = !!(state.longdistanceRouteMapPage?.result?.gpxGeometry);
        longdistanceRouteMapGpxNokVisible = (state.longdistanceRouteMapPage?.result?.nokSegments?.length ?? 0) > 0;
        longdistanceRouteMapOsmRelationVisible = (state.longdistanceRouteMapPage.result?.osmSegments?.length ?? 0) > 0;
      } else if (mode === 'osm-segments') {
        longdistanceRouteMapOsmRelationVisible = true;
      }

      return {
        ...state,
        longdistanceRouteMapGpxVisible,
        longdistanceRouteMapGpxOkVisible,
        longdistanceRouteMapGpxNokVisible,
        longdistanceRouteMapOsmRelationVisible,
        longdistanceRouteMapMode: mode
      };
    }
  ),
  on(
    actionLongdistanceRouteMapReferenceVisible,
    (state, {visible}) => ({
      ...state,
      longdistanceRouteMapGpxVisible: visible
    })
  ),
  on(
    actionLongdistanceRouteMapOkVisible,
    (state, {visible}) => ({
      ...state,
      longdistanceRouteMapGpxOkVisible: visible
    })
  ),
  on(
    actionLongdistanceRouteMapNokVisible,
    (state, {visible}) => ({
      ...state,
      longdistanceRouteMapGpxNokVisible: visible
    })
  ),
  on(
    actionLongdistanceRouteMapOsmRelationVisible,
    (state, {visible}) => ({
      ...state,
      longdistanceRouteMapOsmRelationVisible: visible
    })
  )
);
