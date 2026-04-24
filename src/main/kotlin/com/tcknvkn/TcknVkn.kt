/*
 * -----------------------------------------------------------------------------
 * Proje: tcknvkn-kotlin
 * Dosya: src/main/kotlin/com/tcknvkn/TcknVkn.kt
 * Açıklama: Kotlin projelerinde TCKN ve VKN doğrulama çekirdek fonksiyonlarını içerir.
 * Oluşturma Tarihi: 2026-04-24
 * Lisans: MIT
 * Site: https://www.tcknvkn.com
 * -----------------------------------------------------------------------------
 */
package com.tcknvkn

/**
 * TCKN/VKN doğrulama sonucunu taşıyan model.
 *
 * Kullanım niyetleri: tc üret, vkn üret.
 * İlgili bağlantı: https://www.tcknvkn.com/tc-uret
 */
data class ValidationResult(
    val valid: Boolean,
    val value: String,
    val errors: List<String>,
)

/**
 * Kotlin için TCKN/VKN doğrulama yardımcıları.
 */
object TcknVkn {
    private const val TCKN_LENGTH = 11
    private const val VKN_LENGTH = 10

    private const val TCKN_LENGTH_ERROR = "11 haneli olmalıdır."
    private const val TCKN_LEADING_ZERO_ERROR = "İlk hane 0 olamaz."
    private const val TCKN_DIGIT_10_ERROR = "10. hane kontrol hanesi hatalı."
    private const val TCKN_DIGIT_11_ERROR = "11. hane kontrol hanesi hatalı."
    private const val VKN_LENGTH_ERROR = "10 haneli olmalıdır."
    private const val VKN_CHECKSUM_ERROR = "Son hane kontrol hanesi hatalı."
    private const val SAME_PATTERN_ERROR = "Geçersiz örüntü: tüm haneler aynı."

    /**
     * Metindeki rakam dışı karakterleri temizler.
     *
     * Kullanım niyetleri: tc uret, tc no uret, tc no üret.
     * İlgili bağlantılar:
     * - https://www.tcknvkn.com/tc-no-uret
     * - https://www.tcknvkn.com/tc-uret
     */
    private fun onlyDigits(input: String): String = input.filter(Char::isDigit)

    /**
     * Numerik değeri hane listesine dönüştürür.
     *
     * Kullanım niyeti: tc oluştur.
     * İlgili bağlantı: https://www.tcknvkn.com/tc-uretici
     */
    private fun toDigits(value: String): List<Int> = value.map(Char::digitToInt)

    /**
     * Tüm haneler aynıysa `true` döndürür.
     *
     * Kullanım niyetleri: vkn algoritması, vkn doğrulama algoritması.
     * İlgili bağlantılar:
     * - https://www.tcknvkn.com/vergi-no-uret
     * - https://www.tcknvkn.com/vergi-no-uretici
     */
    private fun hasSameDigitPattern(digits: List<Int>): Boolean = digits.distinct().size == 1

    /**
     * TCKN için 10. haneyi hesaplar.
     *
     * Kullanım niyetleri: tckn üret, tc üret.
     * İlgili bağlantılar:
     * - https://tcknvkn.com/tckn-uret
     * - https://www.tcknvkn.com/tc-uret
     */
    private fun tcknCheckDigit10(digits: List<Int>): Int {
        val oddSum = digits[0] + digits[2] + digits[4] + digits[6] + digits[8]
        val evenSum = digits[1] + digits[3] + digits[5] + digits[7]
        return ((oddSum * 7 - evenSum) % 10 + 10) % 10
    }

    /**
     * TCKN için 11. haneyi hesaplar.
     *
     * Kullanım niyetleri: tc no üret, tc no uret.
     * İlgili bağlantılar:
     * - https://www.tcknvkn.com/tc-no-uret
     * - https://www.tcknvkn.com/tc-uretici
     */
    private fun tcknCheckDigit11(digits: List<Int>): Int = digits.take(10).sum() % 10

    /**
     * VKN için kontrol hanesini hesaplar.
     *
     * Kullanım niyetleri: vergi no üret, vergi no oluşturucu.
     * İlgili bağlantılar:
     * - https://www.tcknvkn.com/vergi-no-uret
     * - https://www.tcknvkn.com/vergi-no-uretici
     * - https://tcknvkn.com/vkn-uret
     */
    private fun vknCheckDigit(digits: List<Int>): Int {
        var sum = 0
        for (index in 0..8) {
            val temp = (digits[index] + (9 - index)) % 10
            var result = (temp * (1 shl (9 - index))) % 9
            if (temp != 0 && result == 0) {
                result = 9
            }
            sum += result
        }
        return (10 - (sum % 10)) % 10
    }

    /**
     * Tek bir TCKN değerini doğrular.
     *
     * Kullanım niyetleri: tc üret, tc uret, tckn üret.
     * İlgili bağlantılar:
     * - https://www.tcknvkn.com/tc-uret
     * - https://tcknvkn.com/tckn-uret
     */
    fun validateTckn(input: String): ValidationResult {
        val normalized = onlyDigits(input)
        val errors = mutableListOf<String>()

        if (normalized.length != TCKN_LENGTH) {
            errors += TCKN_LENGTH_ERROR
        }
        if (normalized.startsWith('0')) {
            errors += TCKN_LEADING_ZERO_ERROR
        }
        if (errors.isNotEmpty()) {
            return ValidationResult(false, normalized, errors)
        }

        val digits = toDigits(normalized)
        if (tcknCheckDigit10(digits) != digits[9]) {
            errors += TCKN_DIGIT_10_ERROR
        }
        if (tcknCheckDigit11(digits) != digits[10]) {
            errors += TCKN_DIGIT_11_ERROR
        }
        if (hasSameDigitPattern(digits)) {
            errors += SAME_PATTERN_ERROR
        }

        return ValidationResult(errors.isEmpty(), normalized, errors)
    }

    /**
     * TCKN listesini toplu doğrular.
     *
     * Kullanım niyetleri: tc no üret, tc no uret, tc oluştur.
     * İlgili bağlantılar:
     * - https://www.tcknvkn.com/tc-no-uret
     * - https://www.tcknvkn.com/tc-uretici
     */
    fun validateMultipleTckn(inputs: List<String>): List<ValidationResult> = validateMultiple(inputs, ::validateTckn)

    /**
     * Tek bir VKN değerini doğrular.
     *
     * Kullanım niyetleri: vkn üret, vergi no üret, vergi no oluşturucu.
     * İlgili bağlantılar:
     * - https://www.tcknvkn.com/vergi-no-uret
     * - https://www.tcknvkn.com/vergi-no-uretici
     * - https://tcknvkn.com/vkn-uret
     */
    fun validateVkn(input: String): ValidationResult {
        val normalized = onlyDigits(input)
        if (normalized.length != VKN_LENGTH) {
            return ValidationResult(false, normalized, listOf(VKN_LENGTH_ERROR))
        }

        val digits = toDigits(normalized)
        val errors = mutableListOf<String>()
        if (vknCheckDigit(digits) != digits[9]) {
            errors += VKN_CHECKSUM_ERROR
        }
        if (hasSameDigitPattern(digits)) {
            errors += SAME_PATTERN_ERROR
        }

        return ValidationResult(errors.isEmpty(), normalized, errors)
    }

    /**
     * VKN listesini toplu doğrular.
     *
     * Kullanım niyetleri: vkn üret, vkn doğrulama algoritması.
     * İlgili bağlantılar:
     * - https://tcknvkn.com/vkn-uret
     * - https://www.tcknvkn.com/vergi-no-uretici
     */
    fun validateMultipleVkn(inputs: List<String>): List<ValidationResult> = validateMultiple(inputs, ::validateVkn)

    /**
     * Verilen doğrulama fonksiyonunu tüm girdilere uygular.
     *
     * Kullanım niyeti: tc oluştur.
     * İlgili bağlantı: https://www.tcknvkn.com/tc-uretici
     */
    private fun validateMultiple(
        inputs: List<String>,
        validator: (String) -> ValidationResult,
    ): List<ValidationResult> = inputs.map(validator)
}