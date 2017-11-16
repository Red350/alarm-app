package red.padraig.alarmapp.ui.activities

import android.app.Activity
import android.os.Bundle
import io.reactivex.disposables.CompositeDisposable
import red.padraig.alarmapp.database.dao.AlarmDAO

// Base activity that all other activities inherit from
// Allows easier listener subscription and database control
abstract class BaseActivity : Activity() {

    protected val TAG = this::class.simpleName+"TAG"

    protected val disposables = CompositeDisposable()
    protected lateinit var alarmDAO: AlarmDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        alarmDAO = AlarmDAO(applicationContext)
    }

    override fun onResume() {
        super.onResume()
        initialiseListeners()
        initialiseSubscriptions()
    }

    override fun onPause() {
        super.onPause()
        clearListeners()
        disposables.clear() // Clear all of the subscribed disposables
    }

    override fun onDestroy() {
        super.onDestroy()
        // TODO: this may cause problems with one activity closing the connection after another has opened it
        // alarmDAO.close()
    }

    protected abstract fun initialiseListeners()

    protected abstract fun clearListeners()

    protected abstract fun initialiseSubscriptions()
}