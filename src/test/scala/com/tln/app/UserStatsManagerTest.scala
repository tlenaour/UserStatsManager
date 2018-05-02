package com.tln.app

import com.tln.app.UserStatsManager.{addPageViewToUser, deleteUser, getUserStatistics}
import com.tln.domain.{PageView, UserStats}
import org.joda.time.DateTimeUtils
import org.scalatest.{BeforeAndAfterAll, GivenWhenThen, Matchers, WordSpec}

class UserStatsManagerTest extends WordSpec with GivenWhenThen with Matchers with BeforeAndAfterAll{

  val testDatas = UserStatsManager.datas

  override def beforeAll(): Unit = {
    //Forcing System time to 27/04/2018 for test
    DateTimeUtils.setCurrentMillisFixed(1524835170000L)

    //Build the datas for test purpose
    val givenUserId1 = "019mr8mf4r"
    val givenPageViewList1 = PageView("019mr8mf4r","Blog Page","2012-11-01T00:30:12.984Z")::
      PageView("019mr8mf4r","Administration Page","2018-04-20T00:34:12.984Z")::
      PageView("019mr8mf4r","Pricing Page","2018-04-21T00:30:12.984Z")::
      PageView("019mr8mf4r","Blog Page","2018-04-23T00:30:12.984Z")::
      PageView("019mr8mf4r","Blog Page","2018-04-26T00:31:12.984Z")::
      PageView("019mr8mf4r","Blog Page","2018-04-28T00:31:12.984Z")::
      PageView("019mr8mf4r","Administration Page","2018-04-27T01:30:12.984Z")::
      PageView("019mr8mf4r","Administration Page","2018-04-27T00:30:12.984Z"):: Nil

    val givenUserId2 = "128ns9ng5s"
    val givenPageViewList2 = PageView("128ns9ng5s","Blog Page","2012-11-01T00:30:12.984Z")::
      PageView("128ns9ng5s","Administration Page","2018-04-20T00:34:12.984Z"):: Nil

    testDatas.put(givenUserId1,givenPageViewList1)
    testDatas.put(givenUserId2,givenPageViewList2)
  }

  "addPageViewToUser" should {
    "append a new PageView to the list of existing PageView" in {
      Given("a new PageView")
      val givenNewPageView = PageView("128ns9ng5s","Blog Page","2012-11-01T00:30:12.984Z")

      When("addPageViewToUser")
      addPageViewToUser(givenNewPageView)

      Then("The new PageView is appened to the user")
      testDatas should have size 2
      testDatas.get("128ns9ng5s") shouldBe Some(PageView("128ns9ng5s","Blog Page","2012-11-01T00:30:12.984Z") ::
        PageView("128ns9ng5s","Blog Page","2012-11-01T00:30:12.984Z")::
        PageView("128ns9ng5s","Administration Page","2018-04-20T00:34:12.984Z"):: Nil)
      testDatas.get("019mr8mf4r") shouldBe Some(PageView("019mr8mf4r","Blog Page","2012-11-01T00:30:12.984Z")::
        PageView("019mr8mf4r","Administration Page","2018-04-20T00:34:12.984Z")::
        PageView("019mr8mf4r","Pricing Page","2018-04-21T00:30:12.984Z")::
        PageView("019mr8mf4r","Blog Page","2018-04-23T00:30:12.984Z")::
        PageView("019mr8mf4r","Blog Page","2018-04-26T00:31:12.984Z")::
        PageView("019mr8mf4r","Blog Page","2018-04-28T00:31:12.984Z")::
        PageView("019mr8mf4r","Administration Page","2018-04-27T01:30:12.984Z")::
        PageView("019mr8mf4r","Administration Page","2018-04-27T00:30:12.984Z"):: Nil)
    }
  }

  "deleteUserData" should {
    "delete all the data from a given user" in {
      Given("a userId")
      val givenUserId = "128ns9ng5s"

      When("deleteUser")
      deleteUser(givenUserId)

      Then("The user is removed from datas")
      testDatas should have size 1
      testDatas.get("128ns9ng5s") shouldBe None
      testDatas.get("019mr8mf4r") shouldBe Some(PageView("019mr8mf4r","Blog Page","2012-11-01T00:30:12.984Z")::
        PageView("019mr8mf4r","Administration Page","2018-04-20T00:34:12.984Z")::
        PageView("019mr8mf4r","Pricing Page","2018-04-21T00:30:12.984Z")::
        PageView("019mr8mf4r","Blog Page","2018-04-23T00:30:12.984Z")::
        PageView("019mr8mf4r","Blog Page","2018-04-26T00:31:12.984Z")::
        PageView("019mr8mf4r","Blog Page","2018-04-28T00:31:12.984Z")::
        PageView("019mr8mf4r","Administration Page","2018-04-27T01:30:12.984Z")::
        PageView("019mr8mf4r","Administration Page","2018-04-27T00:30:12.984Z"):: Nil)
    }
  }

  "getUserStatistics" should {
    "compute statistics for a given user" in {
      Given("a userId")
      val givenUserId = "019mr8mf4r"

      When("getUserStatistics")
      val result = getUserStatistics(givenUserId)

      Then("The statistics are returned")
      result shouldBe UserStats("019mr8mf4r",6,5,"Blog Page")
    }
  }
}
