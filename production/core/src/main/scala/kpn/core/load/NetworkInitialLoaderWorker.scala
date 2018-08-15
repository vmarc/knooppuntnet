package kpn.core.load

import scala.concurrent.Future

trait NetworkInitialLoaderWorker {
  def load(command: NetworkInitialLoad): Future[Unit]
}
