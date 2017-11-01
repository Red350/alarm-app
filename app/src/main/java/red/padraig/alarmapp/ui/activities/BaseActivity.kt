package red.padraig.alarmapp.ui.activities

import android.app.Activity

/**
 * Created by Red on 01/11/2017.
 */
abstract class BaseActivity : Activity() {

    override fun onResume() {
        super.onResume()
        initialiseListeners()
    }

    override fun onPause() {
        clearListeners()
        super.onPause()
    }

    protected abstract fun initialiseListeners()

    protected abstract fun clearListeners()

}