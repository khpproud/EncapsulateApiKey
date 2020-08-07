/*
 * Created by Peter_Proud
 */

package dev.patrick.gifsample

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.giphy.sdk.core.models.Media
import com.giphy.sdk.ui.GPHContentType
import com.giphy.sdk.ui.GPHSettings
import com.giphy.sdk.ui.Giphy
import com.giphy.sdk.ui.themes.GPHTheme
import com.giphy.sdk.ui.themes.GridType
import com.giphy.sdk.ui.views.GiphyDialogFragment

object GiphyUtils : LifecycleEventObserver {

    const val TAG = "GiphyUtils"

    // Used to load the 'native-lib' library on application startup.
    init {
        System.loadLibrary("native-lib")
    }

    private var gifsDialog: GiphyDialogFragment? = null

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun getAPIKey(): String

    fun configure(activity: AppCompatActivity) {
        activity.lifecycle.addObserver(this)

        Giphy.configure(activity, getAPIKey())

//        GiphyDialogFragment.newInstance().show(activity.supportFragmentManager, "giphy_dialog")

        val settings = GPHSettings(gridType = GridType.carousel, theme = GPHTheme.Dark, useBlurredBackground = true)

        gifsDialog = GiphyDialogFragment.newInstance(settings)
        gifsDialog?.gifSelectionListener = object : GiphyDialogFragment.GifSelectionListener {
            override fun didSearchTerm(term: String) {
                Log.d(TAG, "You did select: $term")
            }

            override fun onDismissed(selectedContentType: GPHContentType) {
                Log.d(TAG, "GiphyDialog is dismissed without selected")
            }

            override fun onGifSelected(
                media: Media,
                searchTerm: String?,
                selectedContentType: GPHContentType
            ) {
                Log.d(TAG, "Your taped gif: ${media.title}")
                (activity as GiphySelectedCallback).onGifSelected(media, selectedContentType)
            }
        }

        gifsDialog?.show(activity.supportFragmentManager, "giphy_dialog")
    }

    internal interface GiphySelectedCallback {
        fun onGifSelected(media: Media, selectedContentType: GPHContentType)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_DESTROY) {
            gifsDialog = null
            source.lifecycle.removeObserver(this)
        }
    }
}