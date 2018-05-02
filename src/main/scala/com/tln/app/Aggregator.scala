package com.tln.app


import com.github.nscala_time.time.Imports._
import com.tln.domain.{PageView, PageViewedDay, UserStats}

object Aggregator {

  def addPageView(pageView: PageView, userPageViews : List[PageView]) : List[PageView] = {
    pageView :: userPageViews
  }

  def getOnlyAvailablePageView(userPageViews : List[PageView], numberOfDays : Int) : List[PageView] = {
    val now =  DateTime.now
    userPageViews.filter(pageView => isPageViewAvailableForStat(DateTime.parse(pageView.timestamp),now,numberOfDays))
  }

  def isPageViewAvailableForStat(pageViewTimestamp: DateTime, referenceTimestamp : DateTime, numberOfDays : Int) : Boolean = {
    pageViewTimestamp >= (referenceTimestamp - numberOfDays.day)
  }

  def getNumberPagesViewed(userPageViews : List[PageView]) : Int = {
    userPageViews.length
  }

  def getMostViewedPage(userPageViews : List[PageView]) : String = {
    //What happen if two page has the same number of visits
    userPageViews
      .groupBy(_.pageName)
      .maxBy(_._2.size)._1
  }

  def getNumberOfActiveDays(userPageViews : List[PageView]) : Int = {
    userPageViews
      .map(pageView => {
        val dateparsed = DateTime.parse(pageView.timestamp)
        PageViewedDay(dateparsed.getDayOfMonth, dateparsed.getDayOfWeek, dateparsed.getDayOfYear)
      })
      .distinct
      .size
  }
  def getUserStats(userId : String,pageViews : List[PageView],numberOfDaysForStats: Int) : UserStats = {
    val pageViewsAvailableForStats = getOnlyAvailablePageView(pageViews, numberOfDaysForStats)
    val numberPagesViewed = getNumberPagesViewed(pageViewsAvailableForStats)
    val mostViewedPage = getMostViewedPage(pageViewsAvailableForStats)
    val numberOfActiveDays = getNumberOfActiveDays(pageViewsAvailableForStats)

    UserStats(userId, numberPagesViewed, numberOfActiveDays, mostViewedPage)
  }
}
