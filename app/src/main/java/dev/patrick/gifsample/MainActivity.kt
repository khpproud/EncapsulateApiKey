/*
 * Created by Peter_Proud
 */

package dev.patrick.gifsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.TextView
import com.giphy.sdk.core.models.Media
import com.giphy.sdk.core.models.enums.RenditionType
import com.giphy.sdk.ui.GPHContentType
import com.giphy.sdk.ui.Giphy
import com.giphy.sdk.ui.views.GPHMediaView
import com.giphy.sdk.ui.views.GiphyDialogFragment
import dev.patrick.gifsample.databinding.ActivityMainBinding

/**
 * This app is very shorthand demo by followings:
 *
 * 1. Secure your API key through non-tracking file.
 * (Add `giphy.properties` at `.gitignore`)
 *
 * 2. Save encoded api key string into `BuildConfig` via Base64 encoding.
 * (See `app/build.gradle`)
 *
 * 3. In JNI cpp file, decode it to original api key string.
 * (See 'app/src/main/cpp/native-lib.cpp`)
 *
 * 4. Lastly, obtains the original api key by JNI method.
 *
 * Disclaimer: If you want to more securely, adjust the cryptography algorithm onto input and output.
 */
class MainActivity : AppCompatActivity(), GiphyUtils.GiphySelectedCallback {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Example of a call to a native method
//        findViewById<TextView>(R.id.sample_text).text = GiphyUtils.getAPIKey()

        GiphyUtils.configure(this)
    }

    override fun onGifSelected(media: Media, selectedContentType: GPHContentType) {
        val mediaView = GPHMediaView(this)
        mediaView.setMedia(media, RenditionType.downsized)
        binding.root.addView(mediaView)
    }
}