package com.technoetic.xplanner.charts;

import java.util.Hashtable;

import com.technoetic.xplanner.db.IterationStatisticsQuery;

public class TaskTypeActualHoursData extends XplannerPieChartData{
  protected Hashtable getData(IterationStatisticsQuery statistics)
   {
      return statistics.getTaskActualHoursByType();
   }
}

