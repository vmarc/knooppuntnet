// TODO SIGNAL

// if (action.payload.routerState.url.includes('/analysis/changes')) {
//   const queryParams = action.payload.routerState.root.queryParams;
//   const pageSize = +queryParams['pageSize'];
//   const impact = queryParams['impact'];
//   let strategy: AnalysisStrategy;
//   if ('network' === queryParams['strategy']) {
//     strategy = AnalysisStrategy.network;
//   } else if ('location' === queryParams['strategy']) {
//     strategy = AnalysisStrategy.location;
//   }
//   return {
//     ...state,
//     strategy: strategy ?? state.strategy,
//     pageSize: pageSize ?? state.pageSize,
//     impact: impact ?? state.impact,
//   };
// } else {
//   const params = Util.paramsIn(action.payload.routerState.root);
//   const networkType = params.get('networkType');
//   if (networkType) {
//     return { ...state, networkType };
//   }
//   return state;
// }
