package io.kaitai.struct.languages

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers._

/**
  * Expected values are the Python source between the `"""` delimiters. `Q` is
  * one quote; note that `s"""..."""` still turns `\\` into one backslash.
  */
class PythonCompiler$Test extends AnyFunSpec {
  import PythonCompiler.escapeDocstring

  private val Q = "\""

  describe("PythonCompiler.escapeDocstring") {
    it("leaves plain text alone") {
      escapeDocstring("") shouldEqual ""
      escapeDocstring("Magic \"PK\" signature.\nSecond line.") shouldEqual
        "Magic \"PK\" signature.\nSecond line."
    }

    it("doubles backslashes") {
      escapeDocstring("""Matches \d+ in C:\path\n""") shouldEqual """Matches \\d+ in C:\\path\\n"""
      escapeDocstring("""ends with \""") shouldEqual """ends with \\"""
    }

    it("escapes CR and NUL") {
      escapeDocstring("a\r\nb\u0000c") shouldEqual "a\\r\nb\\x00c"
    }

    it("leaves a leading quote and a pair of quotes alone") {
      escapeDocstring(s"${Q}start") shouldEqual s"${Q}start"
      escapeDocstring(s"`$Q$Q` (empty string)") shouldEqual s"`$Q$Q` (empty string)"
    }

    it("escapes the first quote of every triple quote") {
      escapeDocstring(s"a $Q$Q$Q b") shouldEqual s"""a \\$Q$Q$Q b"""
      escapeDocstring(s"a $Q$Q$Q$Q b") shouldEqual s"""a \\$Q\\$Q$Q$Q b"""
      escapeDocstring(s"a $Q$Q$Q\nb") shouldEqual s"""a \\$Q$Q$Q\nb"""
    }

    it("escapes every quote of a trailing run") {
      escapeDocstring(s"x $Q") shouldEqual s"""x \\$Q"""
      escapeDocstring(s"x $Q$Q") shouldEqual s"""x \\$Q\\$Q"""
    }

    it("escapes a backslash followed by quotes") {
      escapeDocstring(s"""\\$Q$Q$Q y""") shouldEqual s"""\\\\\\$Q$Q$Q y"""
    }
  }
}
