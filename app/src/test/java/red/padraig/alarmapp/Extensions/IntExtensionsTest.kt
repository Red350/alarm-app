package red.padraig.alarmapp.Extensions

import org.junit.Assert
import org.junit.Test

class IntExtensionsTest {
    @Test
    fun testFromMinutesToMillis() {
        Assert.assertEquals(300000, 5.fromMinutesToMillis())
        Assert.assertEquals(60000, 1.fromMinutesToMillis())
        Assert.assertEquals(0, 0.fromMinutesToMillis())
    }

    @Test
    fun fromHourToMills() {
        Assert.assertEquals(18000000, 5.fromHoursToMills())
        Assert.assertEquals(3600000, 1.fromHoursToMills())
        Assert.assertEquals(0, 0.fromHoursToMills())
    }

    @Test
    fun testFBinaryToDaysArray() {
        Assert.assertArrayEquals(booleanArrayOf(false, false, false, false, false, false, false), 0.fromBinaryToDaysArray())
        Assert.assertArrayEquals(booleanArrayOf(false, false, false, false, false, false, true), 1.fromBinaryToDaysArray())
        Assert.assertArrayEquals(booleanArrayOf(true, false, true, false, true, false, false), 84.fromBinaryToDaysArray())
        Assert.assertArrayEquals(booleanArrayOf(true, true, true, true, true, true, true), 127.fromBinaryToDaysArray())
    }

    @Test(expected = RuntimeException::class)
    fun testFromBinaryToDaysArray_ThrowsException_WhenNumberTooBig() {
        128.fromBinaryToDaysArray()
    }

    @Test(expected = RuntimeException::class)
    fun testFromBinaryToDaysArray_ThrowsException_WhenNumberIsNegative() {
        (-1).fromBinaryToDaysArray()
    }

}