package kpn.core.tools.operation

import kpn.api.common.ReplicationId
import kpn.api.custom.Timestamp
import kpn.core.replicate.ReplicationStateRepository
import kpn.core.tools.status.StatusRepository
import kpn.core.util.UnitTest
import org.scalamock.scalatest.MockFactory

class SystemStatusTest extends UnitTest with MockFactory {

  private val webServerProcessLines =
    """UID          PID    PPID  C STIME TTY          TIME CMD
      |marcv     194121       1  7  2022 ?        27-02:22:57 mongod --config /kpn/conf/mongod.conf
      |root      989188       1  0  2022 ?        00:31:30 sendmail: MTA: accepting connections
      |root     1153344       1  0  2022 ?        00:00:04 nginx: master process /usr/sbin/nginx
      |www-data 1165658 1153344  0 Sep28 ?        00:18:41 nginx: worker process
      |www-data 1165659 1153344  0 Sep28 ?        00:00:03 nginx: worker process
      |www-data 1165660 1153344  0 Sep28 ?        00:00:26 nginx: worker process
      |root     2153578       1  0 10:46 ?        00:00:00 /usr/libexec/fwupd/fwupd
      |marcv    3559983       1  9 Sep01 ?        4-07:01:02 /kpn/java/bin/java -Dname=server-experimental -Dlog4j.configurationFile=/kpn/scripts/conf/server-experiment
      |marcv    3560279       1  9 Sep01 ?        4-06:06:41 /kpn/java/bin/java -Dname=server -Dlog4j.configurationFile=/kpn/scripts/conf/server-log.xml -Xms512M -Xmx2G
      |marcv    3560740       1  0 Sep01 ?        04:23:05 /kpn/java/bin/java -Dname=server-mail -Dlog4j.configurationFile=/kpn/scripts/conf/server-mail-log.xml -Xms512
      |root     3629171       2  0 Jul04 ?        00:00:01 [loop5]
      |root     3777265       1  0 Jul06 ?        00:15:50 php-fpm: master process (/etc/php/7.4/fpm/php-fpm.conf)
      |www-data 3777285 3777265  0 Jul06 ?        00:00:00 php-fpm: pool www
      |www-data 3777286 3777265  0 Jul06 ?        00:00:00 php-fpm: pool www
      |systemd+ 3992706       1  0 Mar08 ?        00:02:08 /lib/systemd/systemd-networkd
      |systemd+ 3992748       1  0 Mar08 ?        00:02:06 /lib/systemd/systemd-resolved
      |root     3992751       1  0 Mar08 ?        00:32:40 /lib/systemd/systemd-journald
      |systemd+ 3992848       1  0 Mar08 ?        00:02:07 /lib/systemd/systemd-timesyncd
      |root     3993833       1  0 Mar08 ?        01:03:27 /lib/systemd/systemd-udevd
      |""".stripMargin.split("\n").toList


  private val analysisServerProcessLines =
    """UID          PID    PPID  C STIME TTY          TIME CMD
      |root           1       0  0 Oct07 ?        00:00:05 /sbin/init
      |root         688       1  0 Oct07 ?        00:00:00 nginx: master process /usr/sbin/nginx -g daemon on; master_process on;
      |www-data     689     688  0 Oct07 ?        00:00:00 nginx: worker process
      |www-data     690     688  0 Oct07 ?        00:00:00 nginx: worker process
      |www-data     691     688  0 Oct07 ?        00:00:00 nginx: worker process
      |root         738       1  0 Oct07 ?        00:02:45 /usr/bin/dockerd -H fd:// --containerd=/run/containerd/containerd.sock
      |marcv        930       1  0 Oct07 ?        00:00:00 /lib/systemd/systemd --user
      |marcv       1092       1  0 Oct07 ?        00:40:24 /kpn/overpass/bin/dispatcher --osm-base --attic --db-dir=/kpn/database --space=1000000000000 --time=100000000
      |marcv       1135       1  0 Oct07 ?        00:27:45 /kpn/java/bin/java -Dname=replicator -Dlog4j.configurationFile=/kpn/scripts/conf/replicator-log.xml -Dcom.sun
      |marcv       1193       1  0 Oct07 ?        00:19:57 /kpn/java/bin/java -Dname=updater -Dlog4j.configurationFile=/kpn/scripts/conf/updater-log.xml -Dcom.sun.manag
      |root        2078       1  0 Oct07 ?        00:00:00 sudo -H -u server nohup /kpn/java/bin/java -Dname=server -Dlog4j.configurationFile=/kpn/scripts/conf/server-l
      |root        2079    2078  0 Oct07 pts/1    00:00:00 sudo -H -u server nohup /kpn/java/bin/java -Dname=server -Dlog4j.configurationFile=/kpn/scripts/conf/server-l
      |server      2080    2079 35 Oct07 pts/1    3-19:24:30 /kpn/java/bin/java -Dname=server -Dlog4j.configurationFile=/kpn/scripts/conf/server-log.xml -Xms512M -Xmx6G
      |root      431802     738  0 Oct14 ?        00:00:00 /usr/bin/docker-proxy -proto tcp -host-ip 0.0.0.0 -host-port 32769 -container-ip 172.19.0.2 -container-port 5
      |root      431809     738  0 Oct14 ?        00:00:00 /usr/bin/docker-proxy -proto tcp -host-ip :: -host-port 32769 -container-ip 172.19.0.2 -container-port 5432
      |root      431822       1  0 Oct14 ?        00:00:11 /usr/bin/containerd-shim-runc-v2 -namespace moby -id 32e9b6354638af0eb906af292bca8632045f6ece17ba9f99ed782698
      |marcv     450825       1  0 Oct14 ?        00:12:15 lsyncd /kpn/conf/lsyncd.conf
      |marcv     699601    1193 95 09:36 ?        00:00:40 /kpn/overpass/bin/update_from_dir --osc-dir=/kpn/tmp --version=2023-10-18T07\:36\:15Z --keep-attic --flush-si
      |marcv     699619  699594  0 09:37 pts/2    00:00:00 ps -ef
      |""".stripMargin.split("\n").toList


  test("web server process status") {

    val processReporter = stub[ProcessReporter]
    val statusRepository = stub[StatusRepository]
    val replicationStateRepository = stub[ReplicationStateRepository]

    (() => processReporter.processes).when().returns(webServerProcessLines)

    val status = new SystemStatus(processReporter, statusRepository, replicationStateRepository).status(web = true)

    val expected =
      """Name                 Status   PID  Start   Elapsed
        |server               OK    560279  Sep01   4-06:06:41
        |server-experimental  OK    559983  Sep01   4-07:01:02
        |server-mail          OK    560740  Sep01   04:23:05
        |nginx                OK    153344   2022   00:00:04
        |mongod               OK    194121   2022   27-02:22:57""".stripMargin

    status should equal(expected)
  }

  test("analysis server process and replication status") {

    val processReporter = stub[ProcessReporter]
    val statusRepository = stub[StatusRepository]
    val replicationStateRepository = stub[ReplicationStateRepository]

    (() => processReporter.processes).when().returns(analysisServerProcessLines)

    (() => statusRepository.replicatorStatus).when().returns(Some(ReplicationId(1, 1, 4)))
    (() => statusRepository.updaterStatus).when().returns(Some(ReplicationId(1, 1, 3)))
    (() => statusRepository.analysisStatus1).when().returns(Some(ReplicationId(1, 1, 7)))
    (() => statusRepository.analysisStatus2).when().returns(Some(ReplicationId(1, 1, 8)))
    (() => statusRepository.analysisStatus3).when().returns(Some(ReplicationId(1, 1, 9)))

    (replicationStateRepository.read _).when(ReplicationId(1, 1, 1)).returns(Timestamp(2015, 8, 11, 1, 2, 3))
    (replicationStateRepository.read _).when(ReplicationId(1, 1, 2)).returns(Timestamp(2015, 8, 11, 2, 2, 3))
    (replicationStateRepository.read _).when(ReplicationId(1, 1, 3)).returns(Timestamp(2015, 8, 11, 3, 2, 3))
    (replicationStateRepository.read _).when(ReplicationId(1, 1, 4)).returns(Timestamp(2015, 8, 11, 4, 2, 3))
    (replicationStateRepository.read _).when(ReplicationId(1, 1, 7)).returns(Timestamp(2015, 8, 11, 7, 2, 3))
    (replicationStateRepository.read _).when(ReplicationId(1, 1, 8)).returns(Timestamp(2015, 8, 11, 8, 2, 3))
    (replicationStateRepository.read _).when(ReplicationId(1, 1, 9)).returns(Timestamp(2015, 8, 11, 9, 2, 3))

    val status = new SystemStatus(processReporter, statusRepository, replicationStateRepository).status(web = false)

    val expected =
      """Name                 Status   PID  Start   Elapsed
        |main-dispatcher      OK      1092  Oct07   00:40:24
        |replicator           OK      1135  Oct07   00:27:45
        |updater              OK      1193  Oct07   00:19:57
        |server               OK      2080  Oct07   3-19:24:30
        |lsyncd               OK    450825  Oct14   00:12:15
        |nginx                OK       688  Oct07   00:00:00
        |update_from_dir      OK    699601  09:36   00:00:40
        |
        |replication 001/001/004 2015-08-11 06:02:03
        |update      001/001/003 2015-08-11 05:02:03
        |analysis    001/001/007 2015-08-11 09:02:03""".stripMargin

    status should equal(expected)
  }
}
