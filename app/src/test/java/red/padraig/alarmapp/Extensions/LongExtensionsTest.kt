package red.padraig.alarmapp.Extensions

import org.junit.Assert
import org.junit.Test

class LongExtensionsTest {

    @Test
    fun testGetHours() {
        Assert.assertEquals(5, 18000213L.getHours())
        Assert.assertEquals(1, 3600000L.getHours())
        Assert.assertEquals(0, 3599999L.getHours())
    }

    @Test
    fun testGetMinutes() {
        Assert.assertEquals(5, 300543L.getMinutes())
        Assert.assertEquals(1, 60000L.getMinutes())
        Assert.assertEquals(0, 59999L.getMinutes())
    }

    @Test
    fun testToAlarmString() {
        Assert.assertEquals("13:47", 49620000L.toAlarmString())
    }

    @Test
    fun testToAlarmString_PrependsZeroes() {
        Assert.assertEquals("05:07", 18420000L.toAlarmString())
    }

    @Test(expected = RuntimeException::class)
    fun testToAlarmString_ThrowsExceptionWhenTimeIsNegative() {
        (-1L).toAlarmString()
    }

}