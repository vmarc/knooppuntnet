package kpn.server.analyzer

import java.io.File

import akka.actor.ActorSystem
import kpn.core.db.couch.OldDatabase
import kpn.core.db.views.AnalyzerDesign
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.OverpassQueryExecutorImpl
import kpn.core.tools.config.Dirs
import kpn.core.tools.status.StatusRepository
import kpn.core.tools.status.StatusRepositoryImpl
import kpn.server.analyzer.engine.AnalysisContext
import kpn.server.analyzer.engine.CouchIndexer
import kpn.server.analyzer.engine.analysis.location.LocationConfiguration
import kpn.server.analyzer.engine.changes.OsmChangeRepository
import kpn.server.analyzer.engine.changes.changes.ChangeSetInfoApi
import kpn.server.analyzer.engine.changes.changes.ChangeSetInfoApiImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AnalyzerConfiguration {

  @Autowired
  var oldAnalysisDatabase: OldDatabase = _

  @Autowired
  var system: ActorSystem = _

  @Bean
  def dirs = Dirs()

  @Bean
  def statusRepository: StatusRepository = new StatusRepositoryImpl(dirs)

  @Bean
  def analysisDatabaseIndexer: CouchIndexer = new CouchIndexer(
    oldAnalysisDatabase, AnalyzerDesign
  )

  @Bean
  def overpassQueryExecutor: OverpassQueryExecutor = {
    new OverpassQueryExecutorImpl()
  }

  @Bean
  def osmChangeRepository: OsmChangeRepository = {
    new OsmChangeRepository(dirs.replicate)
  }

  @Bean
  def ChangeSetInfoApi: ChangeSetInfoApi = {
    new ChangeSetInfoApiImpl(dirs.changeSets, system)
  }

  @Bean
  def analysisContext: AnalysisContext = {
    new AnalysisContext()
  }

//  @Bean
//  def locationConfiguration: LocationConfiguration = {
//
//  }

}
