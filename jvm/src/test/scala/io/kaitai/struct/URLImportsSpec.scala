package io.kaitai.struct

import io.kaitai.struct.JavaMain.CLIConfig
import io.kaitai.struct.format.KSVersion
import io.kaitai.struct.formats.JavaKSYParser
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import java.io._

class URLImportsSpec extends AnyFunSuite with Matchers {
  // required, because this class is the sole entry point and this test needs version info
  KSVersion.current = Version.version

  val DEFAULT_CONFIG = CLIConfig()

  test("URL import from GitHub") {
    val testFile = "jvm/src/test/resources/url_import_test.ksy"
    val (specsOpt, problems) = JavaKSYParser.localFileToSpecs(testFile, DEFAULT_CONFIG)
    
    // Check that there were no errors
    problems.filter(_.severity != io.kaitai.struct.problems.ProblemSeverity.Warning) shouldBe empty
    
    // Check that specs were successfully loaded
    specsOpt shouldBe defined
    
    val specs = specsOpt.get
    
    // Check that the main spec was loaded
    specs.contains("url_import_test") shouldBe true
    
    // Check that the imported spec (microsoft_pe) was also loaded
    specs.contains("microsoft_pe") shouldBe true
  }
}
