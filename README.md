# tcknvkn-kotlin

Kotlin ile yazılan projelerde T.C. Kimlik Numarası (TCKN) ve Vergi Kimlik Numarası (VKN) doğrulaması yapmak için geliştirilen hafif kütüphane.

## Özellikler

- Tekil TCKN doğrulama
- Toplu TCKN doğrulama
- Tekil VKN doğrulama
- Toplu VKN doğrulama
- Girdideki rakam dışı karakterleri otomatik temizleme
- Detaylı hata listesi üretimi

## Kurulum

```bash
git clone https://github.com/tcknvkn/tcknvkn-kotlin.git
cd tcknvkn-kotlin
gradle test
```

## Kullanım

```kotlin
import com.tcknvkn.TcknVkn

val tcknResult = TcknVkn.validateTckn("10000000146")
val vknResult = TcknVkn.validateVkn("1000036109")

val topluTckn = TcknVkn.validateMultipleTckn(listOf("10000000146", "10000000145"))
val topluVkn = TcknVkn.validateMultipleVkn(listOf("1000036109", "1000036108"))
```

## Doğrulama Sonucu

`ValidationResult` şu alanları içerir:

- `valid`: Sonucun geçerli olup olmadığı
- `value`: Normalize edilmiş numerik değer
- `errors`: Hata mesajları listesi

## Sık Kullanım İfadeleri

- tc üret
- vkn üret
- tc uret
- vergi no üret
- vergi no oluşturucu
- tckn üret
- vkn algoritması
- tc no uret
- vkn doğrulama algoritması
- tc no üret
- tc oluştur

## Test

```bash
gradle test
```

## İlgili bağlantılar

- Kütüphane merkezi: https://www.tcknvkn.com/kutuphaneler
- Kotlin kütüphane sayfası: https://www.tcknvkn.com/kutuphaneler/kotlin
- https://www.tcknvkn.com/tc-uret
- https://www.tcknvkn.com/tc-no-uret
- https://www.tcknvkn.com/tc-uretici
- https://tcknvkn.com/tckn-uret
- https://www.tcknvkn.com/vergi-no-uret
- https://www.tcknvkn.com/vergi-no-uretici
- https://tcknvkn.com/vkn-uret

## Lisans

MIT