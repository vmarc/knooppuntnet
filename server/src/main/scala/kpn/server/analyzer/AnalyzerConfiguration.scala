package kpn.server.analyzer

import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.OverpassQueryExecutorImpl
import kpn.core.tools.config.Dirs
import kpn.core.tools.status.StatusRepository
import kpn.core.tools.status.StatusRepositoryImpl
import kpn.server.analyzer.engine.changes.OsmChangeRepository
import kpn.server.analyzer.engine.changes.changes.ChangeSetInfoApi
import kpn.server.analyzer.engine.changes.changes.ChangeSetInfoApiImpl
import kpn.server.analyzer.engine.context.AnalysisContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AnalyzerConfiguration {

  @Bean
  def dirs: Dirs = Dirs()

  @Bean
  def statusRepository: StatusRepository = new StatusRepositoryImpl(dirs)

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
    new ChangeSetInfoApiImpl(dirs.changeSets)
  }

  @Bean
  def analysisContext: AnalysisContext = {
    new AnalysisContext()
  }

}
