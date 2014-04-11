package org.pentaho.hadoop.shim.mapr31.authorization;

import java.util.HashMap;
import java.util.Map;

import org.pentaho.hadoop.shim.spi.HadoopShim;
import org.pentaho.hadoop.shim.spi.PentahoHadoopShim;
import org.pentaho.hadoop.shim.spi.PigShim;
import org.pentaho.hadoop.shim.spi.SnappyShim;
import org.pentaho.hadoop.shim.spi.SqoopShim;
import org.pentaho.hbase.shim.mapr31.wrapper.HBaseShimInterface;
import org.pentaho.oozie.shim.api.OozieClientFactory;

public class NoOpHadoopAuthorizationService implements HadoopAuthorizationService {
  private final Map<Class<?>, PentahoHadoopShim> shimMap;

  public NoOpHadoopAuthorizationService() {
    shimMap = new HashMap<Class<?>, PentahoHadoopShim>();
    shimMap.put( HadoopShim.class, new org.pentaho.hadoop.shim.mapr31.HadoopShim() );
    shimMap.put( PigShim.class, new org.pentaho.hadoop.shim.mapr31.PigShim() );
    shimMap.put( SnappyShim.class, new org.pentaho.hadoop.shim.mapr31.SnappyShim() );
    shimMap.put( SqoopShim.class, new org.pentaho.hadoop.shim.common.CommonSqoopShim() );
    shimMap.put( HBaseShimInterface.class, new org.pentaho.hbase.shim.mapr31.MapRHBaseShim() );
    try {
      shimMap.put( OozieClientFactory.class, (PentahoHadoopShim) Class.forName(
          "org.pentaho.di.job.entries.oozie.OozieClientFactoryImpl" ).newInstance() );
    } catch ( Exception e ) {
      throw new RuntimeException( "Unable to create oozie client factory", e );
    }
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public synchronized <T extends PentahoHadoopShim> T getShim( Class<T> clazz ) {
    return (T) shimMap.get( clazz );
  }
}
