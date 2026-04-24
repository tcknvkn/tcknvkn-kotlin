/*
 * -----------------------------------------------------------------------------
 * Proje: tcknvkn-kotlin
 * Dosya: src/test/kotlin/com/tcknvkn/TcknVknTest.kt
 * Açıklama: TCKN ve VKN doğrulama fonksiyonları için birim test senaryolarını içerir.
 * Oluşturma Tarihi: 2026-04-24
 * Lisans: MIT
 * Site: https://www.tcknvkn.com
 * -----------------------------------------------------------------------------
 */
package com.tcknvkn

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TcknVknTest {
    @Test
    fun validateTckn_gectiginde_sonuc_valid_olur() {
        val result = TcknVkn.validateTckn("10000000146")

        assertTrue(result.valid)
        assertEquals("10000000146", result.value)
        assertTrue(result.errors.isEmpty())
    }

    @Test
    fun validateTckn_formatli_girdi_normalize_edilir() {
        val result = TcknVkn.validateTckn("100-000 00146")

        assertTrue(result.valid)
        assertEquals("10000000146", result.value)
    }

    @Test
    fun validateTckn_gecersiz_uzunluk_hatasi_doner() {
        val result = TcknVkn.validateTckn("12345")

        assertFalse(result.valid)
        assertTrue(result.errors.contains("11 haneli olmalıdır."))
    }

    @Test
    fun validateTckn_ilk_hane_sifir_hatasi_doner() {
        val result = TcknVkn.validateTckn("01234567890")

        assertFalse(result.valid)
        assertTrue(result.errors.contains("İlk hane 0 olamaz."))
    }

    @Test
    fun validateTckn_checksum_hatalari_yakalanir() {
        val wrongDigit10 = TcknVkn.validateTckn("10000000156")
        val wrongDigit11 = TcknVkn.validateTckn("10000000145")

        assertFalse(wrongDigit10.valid)
        assertTrue(wrongDigit10.errors.contains("10. hane kontrol hanesi hatalı."))
        assertFalse(wrongDigit11.valid)
        assertTrue(wrongDigit11.errors.contains("11. hane kontrol hanesi hatalı."))
    }

    @Test
    fun validateTckn_ayni_haneler_oruntusu_reddedilir() {
        val result = TcknVkn.validateTckn("11111111111")

        assertFalse(result.valid)
        assertTrue(result.errors.contains("Geçersiz örüntü: tüm haneler aynı."))
    }

    @Test
    fun validateMultipleTckn_sira_korunur() {
        val results = TcknVkn.validateMultipleTckn(
            listOf("10000000146", "10000000145", "11111111111", "100-000 00146"),
        )

        assertEquals(4, results.size)
        assertTrue(results[0].valid)
        assertFalse(results[1].valid)
        assertFalse(results[2].valid)
        assertTrue(results[3].valid)
    }

    @Test
    fun validateVkn_gectiginde_sonuc_valid_olur() {
        val result = TcknVkn.validateVkn("1000036109")

        assertTrue(result.valid)
        assertEquals("1000036109", result.value)
        assertTrue(result.errors.isEmpty())
    }

    @Test
    fun validateVkn_formatli_girdi_normalize_edilir() {
        val result = TcknVkn.validateVkn("100-003-6109")

        assertTrue(result.valid)
        assertEquals("1000036109", result.value)
    }

    @Test
    fun validateVkn_gecersiz_uzunluk_hatasi_doner() {
        val result = TcknVkn.validateVkn("1234")

        assertFalse(result.valid)
        assertTrue(result.errors.contains("10 haneli olmalıdır."))
    }

    @Test
    fun validateVkn_checksum_hatasi_yakalanir() {
        val result = TcknVkn.validateVkn("1000036108")

        assertFalse(result.valid)
        assertTrue(result.errors.contains("Son hane kontrol hanesi hatalı."))
    }

    @Test
    fun validateVkn_ayni_haneler_oruntusu_reddedilir() {
        val result = TcknVkn.validateVkn("1111111111")

        assertFalse(result.valid)
        assertTrue(result.errors.contains("Geçersiz örüntü: tüm haneler aynı."))
    }

    @Test
    fun validateMultipleVkn_sira_korunur() {
        val results = TcknVkn.validateMultipleVkn(
            listOf("1000036109", "1000036108", "1111111111", "100-003-6109"),
        )

        assertEquals(4, results.size)
        assertTrue(results[0].valid)
        assertFalse(results[1].valid)
        assertFalse(results[2].valid)
        assertTrue(results[3].valid)
    }
}