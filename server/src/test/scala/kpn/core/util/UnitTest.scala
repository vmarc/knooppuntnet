package kpn.core.util

import com.softwaremill.diffx.generic.AutoDerivation
import com.softwaremill.diffx.scalatest.DiffMatcher
import org.scalatest.OptionValues
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

abstract class UnitTest extends AnyFunSuite with OptionValues with Matchers with DiffMatcher with AutoDerivation {
}
