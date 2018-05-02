package com.tln.app

import com.tln.domain.{PageView, UserStats}
import scala.collection.mutable

object UserStatsManager {
  private val NumberOfDaysForStats = 7
  val datas : mutable.HashMap[String,List[PageView]] = new mutable.HashMap[String,List[PageView]]

  def addPageViewToUser(pageView : PageView) = {
    val key = pageView.userId
    val existingPageViews = datas.get(key).get
    val newPagesViews = Aggregator.addPageView(pageView,existingPageViews)
    datas.update(key,newPagesViews)
  }

  def deleteUser(userId : String) = {
    datas.remove(userId)
  }

  def getUserStatistics(userId : String) : UserStats= {
    val existingPageViews = datas.get(userId).get
    Aggregator.getUserStats(userId,existingPageViews,NumberOfDaysForStats)
  }

}
