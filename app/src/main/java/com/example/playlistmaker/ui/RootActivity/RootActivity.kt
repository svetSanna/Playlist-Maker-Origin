package com.example.playlistmaker.ui.RootActivity

//import androidx.navigation.findNavController
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityRootBinding


class RootActivity : AppCompatActivity() {
    private var _binding: ActivityRootBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Привязываем вёрстку к экрану
        _binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.rootFragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.newPlaylistFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                    binding.line.visibility = View.GONE
                }
                R.id.mediaFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                    binding.line.visibility = View.GONE
                }
                R.id.playlistFragment->{
                    binding.bottomNavigationView.visibility = View.GONE
                    binding.line.visibility = View.GONE
                }
                R.id.editPlaylistFragment->{
                    binding.bottomNavigationView.visibility = View.GONE
                    binding.line.visibility = View.GONE
                }
                else -> {
                    binding.bottomNavigationView.visibility = View.VISIBLE
                    binding.line.visibility = View.VISIBLE
                }
            }
        }
    }

   /* override fun onNewIntent(intent: Intent){
        super.onNewIntent(intent)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.rootFragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        intent.data?.let {_ -> navController.handleDeepLink(intent)}
    }*/
}
