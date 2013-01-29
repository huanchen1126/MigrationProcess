package org.cmu.ds2013s;

import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
    int averageload = loadsum / len;

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
      if ((averageload - metas.get(low).getWorkload()) <= averageload * BALANCE_THREASHOLD
              && (metas.get(high).getWorkload() - averageload) <= averageload * BALANCE_THREASHOLD) {
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
