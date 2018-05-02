package com.tln.domain

case class UserStats(userId : String,
                     numberPagesViewedLast7days : Int,
                     numberOfDaysActiveLast7days : Int,
                     mostViewedPageLast7days : String)
