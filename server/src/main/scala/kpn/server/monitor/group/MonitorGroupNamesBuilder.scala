package kpn.server.monitor.group

import kpn.server.monitor.repository.MonitorGroupRepository
import org.springframework.stereotype.Component

@Component
class MonitorGroupNamesBuilder(monitorGroupRepository: MonitorGroupRepository) {

  def build(): Seq[String] = {
    monitorGroupRepository.groups().map(_.name)
  }
}
