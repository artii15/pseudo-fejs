package na.przypale.fitter.connectors

import com.datastax.driver.core.{Cluster, Session}

object SessionConnector {

  def connect(cluster: Cluster) = (keyspace: String) => (operations: Session => Unit) => {
    val session = cluster.connect(keyspace)
    operations(session)
    session.close()
  }

}