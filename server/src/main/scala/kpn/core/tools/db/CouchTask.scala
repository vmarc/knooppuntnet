package kpn.core.tools.db

case class CouchTask(
  node: String,
  pid: String,
  changes_pending: Long,
  checkpoint_interval: Long,
  checkpointed_source_seq: String,
  continuous: Boolean,
  database: String,
  doc_id: String,
  doc_write_failures: Long,
  docs_read: Long,
  docs_written: Long,
  missing_revisions_found: Long,
  replication_id: String,
  revisions_checked: Long,
  source: String,
  source_seq: String,
  started_on: Long,
  target: String,
  through_seq: String,
  `type`: String,
  updated_on: Long,
  user: String
)
