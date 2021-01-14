package kpn.api.common.status

case class LogPage(

  timestamp: ActionTimestamp,

  periodType: String,
  periodTitle: String,
  previous: String,
  next: String,

  tile: BarChart,
  tileRobot: BarChart,
  api: BarChart,
  apiRobot: BarChart,
  analysis: BarChart,
  analysisRobot: BarChart,
  robot: BarChart,
  nonRobot: BarChart
)
