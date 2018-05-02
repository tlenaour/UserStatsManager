package com.tln.app

import com.tln.domain.{PageView, UserStats}
import org.joda.time.{DateTime, DateTimeUtils, LocalDateTime}
import org.scalatest.{BeforeAndAfterAll, GivenWhenThen, Matchers, WordSpec}

class AggregatorTest extends WordSpec with GivenWhenThen with Matchers with BeforeAndAfterAll{

  //Forcing System time to 27/04/2018 for test
  override def beforeAll(): Unit = DateTimeUtils.setCurrentMillisFixed(1524835170000L)

  "addPageView" should {
    "return a new list with new element" in {
      Given("a List of PageView and a PageView")
        val givenListOfPageView = PageView("019mr8mf4r","Pricing Page","2012-12-03T00:30:12.984Z") ::
          PageView("019mr8mf4r","Blog Page","2012-12-01T00:30:12.984Z") :: Nil
        val givenPageView = PageView("019mr8mf4r","Administration Page","2012-12-02T00:30:12.984Z")

      When("addPageViewAndSortByTimestampDesc")
        val result = Aggregator.addPageView(givenPageView, givenListOfPageView)

      Then("The list with the new element and all element sorted by timestamp is returned")
        val expected = PageView("019mr8mf4r","Administration Page","2012-12-02T00:30:12.984Z")::
          PageView("019mr8mf4r","Pricing Page","2012-12-03T00:30:12.984Z") ::
          PageView("019mr8mf4r","Blog Page","2012-12-01T00:30:12.984Z") :: Nil
        result should have size 3
        result shouldBe (expected)
    }
  }

  "isPageViewAvailableForStat" should {
    val nowTest = new LocalDateTime().toDateTime()
    "return false to know if PageView is available for the stat computing" in {
      Given("a DateTime")
      val givenDateTime = DateTime.parse("2012-12-02T00:30:12.984Z")
      val givenNumberOfRetentionDays = 7

      When("isAvailableForStat")
      val result = Aggregator.isPageViewAvailableForStat(givenDateTime,nowTest, givenNumberOfRetentionDays)

      Then("false is returned for old view")
      result shouldBe (false)
    }
    "return true to know if PageView is available for the stat computing" in {
      Given("a DateTime")
      val givenDateTime = DateTime.parse("2018-04-26T00:30:12.984Z")
      val givenNumberOfRetentionDays = 7
      When("isAvailableForStat with 7 days")
      val result = Aggregator.isPageViewAvailableForStat(givenDateTime,nowTest, givenNumberOfRetentionDays)

      Then("true is returned for available view")
      result shouldBe (true)
    }
  }

  "keepOnlyAvailablePageView" should {
    "return an empty list of PageView for stats" in {
      Given("a list of PageView")
      val givenPageViewList = PageView("019mr8mf4r","Blog Page","2012-12-01T00:30:12.984Z") ::
        PageView("019mr8mf4r","Administration Page","2012-12-02T00:30:12.984Z") ::
        PageView("019mr8mf4r","Pricing Page","2012-12-03T00:30:12.984Z") :: Nil
      val givenNumberOfRetentionDays = 7

      When("isAvailableForStat")
      val result = Aggregator.getOnlyAvailablePageView(givenPageViewList, givenNumberOfRetentionDays)

      Then("the list is empty because PageViews are too old")
      result shouldBe empty
    }
    "return the list of only available PageView for stats" in {
      Given("a list of PageView")
      val givenPageViewList = PageView("019mr8mf4r","Blog Page","2012-12-01T00:30:12.984Z") ::
        PageView("019mr8mf4r","Pricing Page","2018-04-23T00:30:12.984Z") ::
        PageView("019mr8mf4r","Administration Page","2018-04-20T00:30:12.984Z") ::
        PageView("019mr8mf4r","Pricing Page","2018-04-26T00:30:12.984Z") :: Nil
      val givenNumberOfRetentionDays = 7

      When("isAvailableForStat")
      val result = Aggregator.getOnlyAvailablePageView(givenPageViewList, givenNumberOfRetentionDays)

      Then("the list is empty because PageViews are too old")
      result should have size 2
      result should contain only(PageView("019mr8mf4r","Pricing Page","2018-04-23T00:30:12.984Z"),PageView("019mr8mf4r","Pricing Page","2018-04-26T00:30:12.984Z"))
    }
  }

  "getNumberPagesViewed" should {
    "return the number of pages viewed" in {
      Given("a list of PageView")
      val givenPageViewList = PageView("019mr8mf4r","Blog Page","2012-12-01T00:30:12.984Z") ::
        PageView("019mr8mf4r","Administration Page","2012-12-02T00:30:12.984Z") ::
        PageView("019mr8mf4r","Pricing Page","2012-12-03T00:30:12.984Z") :: Nil

      When("getNumberPagesViewed")
      val result = Aggregator.getNumberPagesViewed(givenPageViewList)

      Then("3 is returned")
      result shouldBe 3
    }
  }

  "getMostViewedPage" should {
    "return the number of pages viewed" in {
      Given("a list of PageView")
      val givenPageViewList = PageView("019mr8mf4r","Blog Page","2012-11-01T00:30:12.984Z") ::
        PageView("019mr8mf4r","Administration Page","2012-12-02T00:30:12.984Z") ::
        PageView("019mr8mf4r","Pricing Page","2012-12-03T00:30:12.984Z") ::
        PageView("019mr8mf4r","Blog Page","2012-11-01T00:30:12.984Z") ::
        PageView("019mr8mf4r","Administration Page","2012-12-02T00:30:12.984Z") ::
        PageView("019mr8mf4r","Administration Page","2012-12-02T00:30:12.984Z") :: Nil

      When("getNumberPagesViewed")
      val result = Aggregator.getMostViewedPage(givenPageViewList)

      Then("the max page administration is returned")
      result shouldBe "Administration Page"
    }
  }

  "getNumberOfActiveDays" should {
    "return the number of pages viewed" in {
      Given("a list of PageView")
      val givenPageViewList = PageView("019mr8mf4r","Blog Page","2012-11-01T00:30:12.984Z")::
        PageView("019mr8mf4r","Administration Page","2012-12-02T00:34:12.984Z")::
        PageView("019mr8mf4r","Pricing Page","2012-12-02T00:30:12.984Z")::
        PageView("019mr8mf4r","Blog Page","2012-11-05T00:30:12.984Z")::
        PageView("019mr8mf4r","Blog Page","2012-11-01T00:31:12.984Z")::
        PageView("019mr8mf4r","Administration Page","2012-12-02T01:30:12.984Z")::
        PageView("019mr8mf4r","Administration Page","2018-04-27T00:30:12.984Z"):: Nil

      When("getNumberOfActiveDays")
      val result = Aggregator.getNumberOfActiveDays(givenPageViewList)

      Then("the max page administration is returned")
      result shouldBe 4
    }
  }

  "getUserStats" should {
    "return the stat from a list of PageView" in {
      Given("a list of PageView and a userId")
      val givenNumberOfRetentionDays = 7
      val givenUserId = "019mr8mf4r"
      val givenPageViewList = PageView("019mr8mf4r","Blog Page","2012-11-01T00:30:12.984Z")::
        PageView("019mr8mf4r","Administration Page","2018-04-20T00:34:12.984Z")::
        PageView("019mr8mf4r","Pricing Page","2018-04-21T00:30:12.984Z")::
        PageView("019mr8mf4r","Blog Page","2018-04-23T00:30:12.984Z")::
        PageView("019mr8mf4r","Blog Page","2018-04-26T00:31:12.984Z")::
        PageView("019mr8mf4r","Blog Page","2018-04-28T00:31:12.984Z")::
        PageView("019mr8mf4r","Administration Page","2018-04-27T01:30:12.984Z")::
        PageView("019mr8mf4r","Administration Page","2018-04-27T00:30:12.984Z"):: Nil

      When("getUserStats")
      val result = Aggregator.getUserStats(givenUserId, givenPageViewList,givenNumberOfRetentionDays)

      Then("the stats are returned")
      result shouldBe UserStats("019mr8mf4r",6,5,"Blog Page")
    }
  }
}
