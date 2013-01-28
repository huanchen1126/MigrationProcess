package org.cmu.ds2013s;

import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DefaultLoadBalancerStrategy implements LoadBalancerStrategy {
  private static final Log logger = LogFactory.getLog(DefaultLoadBalancerStrategy.class);
  
  public final static double BALANCE_THREASHOLD = 0.2;

  @Override
  public List<MigrationTask> getLoadBalanceTasks(Map<String, SlaveMeta> slaves) {
    List<MigrationTask> result = new ArrayList<MigrationTask>();
    
    synchronized (slaves) {
      // 1. get a set of SlaveMeta copies
      List<SlaveMeta> metas = new ArrayList<SlaveMeta>();
      metas.addAll(slaves.values());
      
      // 2. get the number of slaves
      int len = metas.size();
      
      // 3. get the total workload of the whole system
      int loadsum = 0;
      for(SlaveMeta slave : metas) {
        logger.info("Slave " + slave.getIp() + " has " + slave.getWorkload() + " workloads. Balancing");
        
        loadsum += slave.getWorkload();
      }
      
      // 4. compute the average workload for each slave
      int averageload = loadsum/len;
      
      // 5. sort meta
      Collections.sort(metas, new Comparator<SlaveMeta>() {

        @Override
        public int compare(SlaveMeta arg0, SlaveMeta arg1) {
          return arg0.getWorkload() - arg1.getWorkload();
        }
        
      });
      
      // 6. balance those slaves whose workload is far away than average
      //    in BALANCE_THREASHOLD percent.
      int low = 0;
      int high = len - 1;
      while(low < high) {
        if ((averageload - metas.get(low).getWorkload()) <= averageload * BALANCE_THREASHOLD
                && (metas.get(low).getWorkload() - averageload) >= averageload * BALANCE_THREASHOLD) {
          break;
        }else{
          result.add(new MigrationTask(metas.get(high), metas.get(low), 1));
          
          low++;
          high--;
        }
      }
    }
    
    return result;
  }

}
