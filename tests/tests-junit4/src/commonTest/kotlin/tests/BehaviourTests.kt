package tests

import foo.Bar
import foo.Foo
import foo.MockBar
import foo.MockFoo
import org.kodein.mock.Mocker
import org.kodein.mock.UsesMocks
import kotlin.test.*


@UsesMocks(Foo::class, Bar::class)
class BehaviourTests {

    val mocker = Mocker()

    @BeforeTest
    fun setUp() {
        mocker.reset()
    }

    @Test
    fun lambdaCaptureAtDefinition() {
        val bar = MockBar(mocker)
        val captures = ArrayList<(String) -> Int>()
        mocker.every { bar.callback(isAny(captures)) } returns Unit

        var lambdaValue: String? = null
        bar.callback { lambdaValue = it ; 42 }

        assertNull(lambdaValue)
        captures.single().invoke("Some String")
        assertEquals("Some String", lambdaValue)
    }

    @Test
    fun lambdaCaptureAtVerification() {
        val bar = MockBar(mocker)
        mocker.every { bar.callback(isAny()) } returns Unit

        var lambdaValue: String? = null
        bar.callback { lambdaValue = it ; 42 }

        val captures = ArrayList<(String) -> Int>()
        mocker.verify { bar.callback(isAny(captures)) }

        assertNull(lambdaValue)
        captures.single().invoke("Some String")
        assertEquals("Some String", lambdaValue)
    }

    @Test
    fun changeBehaviour() {
        val foo = MockFoo<Bar>(mocker)
        val onNewInt = mocker.every { foo.newInt() }
        onNewInt returns 21
        assertEquals(21, foo.newInt())
        onNewInt returns 42
        assertEquals(42, foo.newInt())
    }

    @Test
    fun throws() {
        val bar = MockBar(mocker)
        mocker.every { bar.doNothing() } runs { error("This is a test!") }

        val ex = assertFailsWith<IllegalStateException> { bar.doNothing() }
        assertEquals("This is a test!", ex.message)
    }

    @Test
    fun returnsNull() {
        val foo = MockFoo<Bar>(mocker)
        mocker.every { foo.newStringNullable() } returns null

        assertNull(foo.newStringNullable())

        mocker.verify { assertNull(foo.newStringNullable()) }
    }
}
