package org.cmu.ds2013s;

import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class is the default load balance strategy for the master node.
 * 
 * When doing load balance, this strategy first sort all slaves in terms
 * of their workload. Then find many slave pairs. In each node pair, one
 * node's workload is more than the average workload, while the other
 * one's workload is less than the average workload. For each migration
 * pair, just migrate 1 task each time.
 * 
 * In this way, the load balance procedure is gradual and smoothly, rather
 * than leveling all slaves' workloads into average immediately.
 */

public class DefaultLoadBalancerStrategy implements LoadBalancerStrategy {
  private static final Log logger = LogFactory.getLog(DefaultLoadBalancerStrategy.class);

  public final static double BALANCE_THREASHOLD = 0.2;

  @Override
  public List<MigrationTask> getLoadBalanceTasks(List<SlaveMeta> metas) {
    List<MigrationTask> result = new ArrayList<MigrationTask>();

    // 1. get the number of slaves
    int len = metas.size();
    if (len == 0)
      return result;

    // 2. get the total workload of the whole system
    int loadsum = 0;
    for (SlaveMeta slave : metas) {
      loadsum += slave.getWorkload();
    }

    // 3. compute the average workload for each slave
    double averageload = (double)loadsum / (double)len;

    // 4. sort meta
    Collections.sort(metas, new Comparator<SlaveMeta>() {

      @Override
      public int compare(SlaveMeta arg0, SlaveMeta arg1) {
        return arg0.getWorkload() - arg1.getWorkload();
      }

    });

    // 5. balance those slaves whose workload is far away than average
    // in BALANCE_THREASHOLD percent.
    int low = 0;
    int high = len - 1;
    while (low < high) {
      if ((metas.get(high).getWorkload() - metas.get(low).getWorkload() <= 2)
              || ((averageload - (double) metas.get(low).getWorkload()) <= averageload * BALANCE_THREASHOLD 
                  && ((double) metas.get(high).getWorkload() - averageload) <= averageload * BALANCE_THREASHOLD)) {
        break;
      } else {
        result.add(new MigrationTask(metas.get(high), metas.get(low), 1));

        low++;
        high--;
      }
    }

    return result;
  }

}
