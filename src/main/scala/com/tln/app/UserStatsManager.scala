package com.tln.app

import com.tln.app.UserStatsAggregator.{addPageView, getUserStats}
import com.tln.domain.{PageView, UserStats}
import scala.collection.mutable

object UserStatsManager {
  private val givenNumberOfRetentionDaysForStats = 7
  val datas = new mutable.HashMap[String,List[PageView]]

  def addPageViewToUser(pageView : PageView) = {
    val key = pageView.userId
    val existingPageViews = datas.get(key).get
    val newPagesViews = addPageView(pageView,existingPageViews)
    datas.update(key,newPagesViews)
  }

  def deleteUser(userId : String) = {
    datas.remove(userId)
  }

  def getUserStatistics(userId : String) : UserStats= {
    val existingPageViews = datas.get(userId).get
    getUserStats(userId,existingPageViews,givenNumberOfRetentionDaysForStats)
  }
}
