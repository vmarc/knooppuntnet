package kpn.server.analyzer

import kpn.core.overpass.CachingOverpassQueryExecutor
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.OverpassQueryExecutorImpl
import kpn.core.overpass.OverpassQueryExecutorRemoteImpl
import kpn.core.tools.config.Dirs
import kpn.core.tools.status.StatusRepository
import kpn.core.tools.status.StatusRepositoryImpl
import kpn.server.analyzer.engine.changes.OsmChangeRepository
import kpn.server.analyzer.engine.changes.changes.ChangeSetInfoApi
import kpn.server.analyzer.engine.changes.changes.ChangeSetInfoApiImpl
import kpn.server.analyzer.engine.context.AnalysisContext
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

import java.io.File

@Configuration
class AnalyzerConfiguration {

  @Bean
  def dirs: Dirs = Dirs()

  @Bean
  def statusRepository: StatusRepository = new StatusRepositoryImpl(dirs)

  @Bean
  def nonCachingOverpassQueryExecutor(@Value("${app.overpass.remote:false}") remote: Boolean): OverpassQueryExecutor = {
    if (remote) {
      new OverpassQueryExecutorRemoteImpl()
    }
    else {
      new OverpassQueryExecutorImpl()
    }
  }

  @Bean
  def cachingOverpassQueryExecutor(@Value("${app.overpass.remote:false}") remote: Boolean): OverpassQueryExecutor = {
    new CachingOverpassQueryExecutor(
      new File("/kpn/cache"),
      nonCachingOverpassQueryExecutor(remote)
    )
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
