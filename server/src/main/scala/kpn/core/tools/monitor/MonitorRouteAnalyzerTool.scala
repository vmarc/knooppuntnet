package kpn.core.tools.monitor

import kpn.api.common.ReplicationId
import kpn.api.common.changes.ChangeSet
import kpn.api.common.changes.ChangeSetInfo
import kpn.api.custom.Timestamp
import kpn.core.mongo.util.Mongo
import kpn.core.util.Log
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.context.ElementIds
import kpn.server.analyzer.engine.monitor.MonitorChangeImpactAnalyzerFileImpl
import kpn.server.analyzer.engine.monitor.MonitorChangeProcessor
import kpn.server.analyzer.engine.monitor.MonitorChangeProcessorImpl
import kpn.server.analyzer.engine.monitor.MonitorRouteLoaderFileImpl
import kpn.server.repository.ChangeSetInfoRepository
import kpn.server.repository.ChangeSetInfoRepositoryImpl
import kpn.server.repository.MonitorAdminRouteRepositoryImpl

import java.io.File

object MonitorRouteAnalyzerTool {

  def main(args: Array[String]): Unit = {

    Mongo.executeIn("kpn-test") { database =>
      val monitorRouteLoader = new MonitorRouteLoaderFileImpl()
      val monitorAdminRouteRepository = new MonitorAdminRouteRepositoryImpl(database)
      val changeSetInfoRepository = new ChangeSetInfoRepositoryImpl(database)
      val monitorChangeImpactAnalyzer = new MonitorChangeImpactAnalyzerFileImpl()
      val monitorChangeProcessor = new MonitorChangeProcessorImpl(
        analyzerHistory = true,
        monitorAdminRouteRepository,
        monitorRouteLoader,
        monitorChangeImpactAnalyzer
      )
      new MonitorRouteAnalyzerTool(monitorChangeProcessor, changeSetInfoRepository).analyze()
    }
  }
}

class MonitorRouteAnalyzerTool(
  monitorChangeProcessor: MonitorChangeProcessor,
  changeSetInfoRepository: ChangeSetInfoRepository
) {

  private val log = Log(classOf[MonitorRouteAnalyzerTool])

  def analyze(): Unit = {

    monitorChangeProcessor.load(Timestamp(2020, 8, 1))

    val files = new File("/kpn/wrk").list().filterNot(_ == "begin").toSeq.sorted
    files.zipWithIndex.foreach { case (changeSetId, index) =>
      Log.context(s"${index + 1}/${files.size}/$changeSetId") {
        changeSetInfoRepository.get(changeSetId.toLong) match {
          case None => log.warn(s"Could not read changeSetInfo $changeSetId")
          case Some(changeSetInfo) =>
            val context = buildContext(changeSetInfo)
            log.info(context.changeSet.timestamp.yyyymmddhhmmss)
            monitorChangeProcessor.process(context)
        }
      }
    }
  }

  private def buildContext(changeSetInfo: ChangeSetInfo): ChangeSetContext = {
    val timestamp = changeSetInfo.createdAt
    ChangeSetContext(
      ReplicationId(1),
      ChangeSet(
        changeSetInfo.id,
        timestamp,
        timestamp,
        timestamp,
        timestamp,
        timestamp,
        Seq.empty
      ),
      ElementIds()
    )
  }
}
