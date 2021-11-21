package com.lx.ohmyjuus.point

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lx.ohmyjuus.MainActivity
import com.lx.ohmyjuus.databinding.FragmentPointuseBinding
import kotlinx.android.synthetic.main.fragment_pointuse.*

class PointUseFragment : Fragment() {

    private var _binding: FragmentPointuseBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View? {
        _binding = FragmentPointuseBinding.inflate(inflater, container, false)

        pigButton.setOnClickListener {
            val intent = Intent(getActivity(), DonateActivity::class.java)
            startActivity(intent)
        }

        clothShop.setOnClickListener {
            val intent = Intent(getActivity(), ShopdetailActivity::class.java)
            startActivity(intent)
        }

        plasticShop.setOnClickListener {
            val intent = Intent(getActivity(), MainActivity::class.java)
            startActivity(intent)
        }

        glassShop.setOnClickListener {
            val intent = Intent(getActivity(), MainActivity::class.java)
            startActivity(intent)
        }

        cafeShop.setOnClickListener {
            val intent = Intent(getActivity(), MainActivity::class.java)
            startActivity(intent)
        }

        cultureShop.setOnClickListener {
            val intent = Intent(getActivity(), MainActivity::class.java)
            startActivity(intent)
        }

        ecoShop.setOnClickListener {
            val intent = Intent(getActivity(), MainActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}