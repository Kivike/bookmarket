package fi.oulu.bookmarket2020

import android.content.Intent
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView

class NavFragment : Fragment(), NavigationView.OnNavigationItemSelectedListener
{

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.nav_collection -> {
                val intent = Intent(context, CollectionActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_marketplace -> {
                val intent = Intent(context, MarketplaceActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_logout -> {
                Session(context!!).setLoggedInUser(null)
                val intent = Intent(context, LoginActivity::class.java)
                startActivity(intent)
            }
            else -> Toast.makeText(context, "Not yet implemented", Toast.LENGTH_SHORT).show()
        }
        return true
    }
}