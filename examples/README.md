# Örnekler

Bu dizin, `tcknvkn-kotlin` için örnek kullanım notlarını içerir.

## Hızlı örnek

```kotlin
import com.tcknvkn.TcknVkn

fun main() {
    val tckn = TcknVkn.validateTckn("10000000146")
    val vkn = TcknVkn.validateVkn("1000036109")

    println(tckn)
    println(vkn)
}
```