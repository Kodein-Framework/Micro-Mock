package tests

import data.Data
import data.Direction
import data.SomeDirection
import data.SubData
import foo.Bar
import org.kodein.micromock.Fake
import org.kodein.micromock.Mock
import org.kodein.micromock.Mocker
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class BarTests {

    @set:Mock
    lateinit var bar: Bar

    @set:Fake
    lateinit var data: Data

    val mocker = Mocker()

    @BeforeTest
    fun setUp() {
        mocker.reset()
        injectMocks(mocker)
    }

    @Test
    fun testBar() {
        mocker.on { bar.rwString = isAny() } returns Unit
        mocker.on { bar.callback(isAny()) } returns Unit

        bar.rwString = "Salomon"
        bar.callback { 42 }

        mocker.verify {
            bar.rwString = "Salomon"
            bar.callback(isAny())
        }
    }

    @Test
    fun testData() {
        assertEquals(
            Data(
                SubData("", 0),
                SubData("", 0),
                null,
                SomeDirection(Direction.LEFT)
            ),
            data
        )
    }
}
