package kpn.api.common.status

case class SystemStatusPage(
  periodType: String,
  periodTitle: String,
  previous: String,
  next: String,

  backendDiskSpaceUsed: BarChart,
  backendDiskSpaceAvailable: BarChart,
  backendDiskSpaceOverpass: BarChart,

  analysisDocCount: BarChart,
  analysisDiskSize: BarChart,
  analysisDiskSizeExternal: BarChart,
  analysisDataSize: BarChart,

  changesDocCount: BarChart,
  changesDiskSize: BarChart,
  changesDiskSizeExternal: BarChart,
  changesDataSize: BarChart,
)
