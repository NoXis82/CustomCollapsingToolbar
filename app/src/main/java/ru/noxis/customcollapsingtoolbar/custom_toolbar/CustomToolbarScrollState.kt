package ru.noxis.customcollapsingtoolbar.custom_toolbar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue


/**
 * Объект состояния, который может быть поднят для управления и наблюдения за состоянием верхней панели приложения.
 * Состояние считывается и обновляется реализацией [CustomToolbarScrollBehavior].
 *
 * В большинстве случаев это состояние будет создано с помощью [rememberToolbarScrollBehavior].
 *
 * @param initialHeightOffsetLimit начальное значение для [CustomToolbarScrollState.heightOffsetLimit]
 * @param initialHeightOffset начальное значение для [CustomToolbarScrollState.heightOffset]
 * @param initialContentOffset начальное значение для [CustomToolbarScrollState.contentOffset]
 */
@Stable
class CustomToolbarScrollState(
    initialHeightOffsetLimit: Float,
    initialHeightOffset: Float,
    initialContentOffset: Float,
) {

    companion object {
        val Saver: Saver<CustomToolbarScrollState, *> = listSaver(
            save = { listOf(it.heightOffsetLimit, it.heightOffset, it.contentOffset) },
            restore = {
                CustomToolbarScrollState(
                    initialHeightOffsetLimit = it[0],
                    initialHeightOffset = it[1],
                    initialContentOffset = it[2]
                )
            }
        )
    }

    private var _heightOffset = mutableFloatStateOf(initialHeightOffset)

    /**
     * Ограничение смещения высоты верхней панели приложения в пикселях,
     * которое представляет собой предел, до которого верхняя панель может свернуться.
     *
     * Используйте это ограничение для принудительного изменения значения [heightOffset] при его обновлении.
     */
    var heightOffsetLimit by mutableFloatStateOf(initialHeightOffsetLimit)

    /**
     * Текущее смещение высоты верхней панели приложений в пикселях. Это смещение высоты применяется к фиксированной
     * высоте панели приложений для управления высотой отображения при прокрутке содержимого.
     *
     * Обновления значения [heightOffset] принудительно изменяются между нулем и [heightOffsetLimit]
     */
    var heightOffset: Float
        get() = _heightOffset.floatValue
        set(newOffset) {
            _heightOffset.floatValue = newOffset.coerceIn(
                minimumValue = heightOffsetLimit,
                maximumValue = 0f
            )
        }

    /**
     * Общее смещение содержимого, прокручиваемого под верхней панелью приложения.
     *
     * Это значение обновляется [CustomToolbarScrollBehavior] каждый раз, когда вложенное соединение прокрутки
     * потребляет события прокрутки. Обычная реализация обновляет значение как сумму всех
     * [NestedScrollConnection.onPostScroll] `consumed.y` значений.
     */
    var contentOffset by mutableFloatStateOf(initialContentOffset)

    /**
     * Значение, представляющее процент высоты свернутой панели приложения.
     *
     * Значение `0.0` представляет полностью развернутую панель, а `1.0` - полностью свернутую (вычисляется
     * как [heightOffset] / [heightOffsetLimit])
     */
    val collapsedFraction: Float
        get() = if (heightOffsetLimit != 0f) {
            heightOffset / heightOffsetLimit
        } else {
            0f
        }

}

@Composable
internal fun rememberToolbarScrollState(
    initialHeightOffsetLimit: Float = -Float.MAX_VALUE,
    initialHeightOffset: Float = 0f,
    initialContentOffset: Float = 0f
) = rememberSaveable(saver = CustomToolbarScrollState.Saver) {
    CustomToolbarScrollState(
        initialHeightOffsetLimit,
        initialHeightOffset,
        initialContentOffset
    )
}