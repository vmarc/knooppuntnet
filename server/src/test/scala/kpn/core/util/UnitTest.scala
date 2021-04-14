package kpn.core.util

import com.softwaremill.diffx.scalatest.DiffMatcher
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import com.softwaremill.diffx.generic.DiffDerivation
import org.scalatest.OptionValues

abstract class UnitTest extends AnyFunSuite with OptionValues with Matchers with DiffMatcher with DiffDerivation {
}
