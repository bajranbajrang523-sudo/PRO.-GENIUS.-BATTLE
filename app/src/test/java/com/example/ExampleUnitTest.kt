package com.example

import com.example.data.model.UserProfileEntity
import com.example.util.SecurityUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun freeFireUid_numericValidation() {
    val validUid = "8934102941"
    val invalidUidAlpha = "89341FF941"
    val invalidUidSpecial = "89341-941"

    assertTrue(validUid.all { it.isDigit() })
    assertFalse(invalidUidAlpha.all { it.isDigit() })
    assertFalse(invalidUidSpecial.all { it.isDigit() })
  }

  @Test
  fun freeFireUid_lengthValidation() {
    val normalUid = "8934102941"
    val tooShort = "123"
    val tooLong = "123456789012345678"

    assertTrue(normalUid.length in 5..15)
    assertFalse(tooShort.length in 5..15)
    assertFalse(tooLong.length in 5..15)
  }

  @Test
  fun userProfile_updateCredentials() {
    val initialProfile = UserProfileEntity()
    val updated = initialProfile.copy(
      inGameName = "SniperKing_PK",
      inGameUid = "9876543210"
    )

    assertEquals("SniperKing_PK", updated.inGameName)
    assertEquals("9876543210", updated.inGameUid)
  }

  @Test
  fun inputSanitization_preventsXssInProfile() {
    val maliciousIgn = "<script>alert('hack')</script>ProGamer"
    val cleaned = SecurityUtils.sanitizeInput(maliciousIgn)

    assertFalse(cleaned.contains("<script>"))
    assertTrue(cleaned.contains("&lt;script&gt;"))
  }
}
