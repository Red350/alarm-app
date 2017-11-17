package red.padraig.alarmapp.Extensions

import org.junit.Assert
import org.junit.Test

/**
 * Created by Red on 17/11/2017.
 */
class BooleanArrayExtensionsKtTest {

    @Test
    fun testFromDaysArraytoBinary() {
        Assert.assertEquals(0, booleanArrayOf(false, false, false, false, false, false, false).fromDaysArraytoBinary())
        Assert.assertEquals(1, booleanArrayOf(false, false, false, false, false, false, true).fromDaysArraytoBinary())
        Assert.assertEquals(84, booleanArrayOf(true, false, true, false, true, false, false).fromDaysArraytoBinary())
        Assert.assertEquals(127, booleanArrayOf(true, true, true, true, true, true, true).fromDaysArraytoBinary())
    }

}