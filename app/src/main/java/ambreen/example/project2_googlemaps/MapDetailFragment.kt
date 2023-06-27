package ambreen.example.project2_googlemaps

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MapDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MapDetailFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var dataViewModel: DataViewModel
    private lateinit var lat: TextView
    private lateinit var long: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    fun display(view: View?){
     //   dataViewModel.lat.observe(this, Observer{
     //       lat.text = "Longitude : ${it.toString()}"
     //   })
     //   dataViewModel.long.observe(this, Observer{
     //       long.text = "Longitude : ${it.toString()}"
     //   })

        // Gets the data from the passed bundle
        val bundle = arguments
        lat.text ="Latitude : ${bundle!!.getDouble("latitude").toString()}"
        long.text ="Longitude : ${bundle!!.getDouble("longitude").toString()}"
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map_detail, container, false)

        // dataViewModel = ViewModelProvider(requireActivity())[DataViewModel::class.java]
         lat = view.findViewById(R.id.tvLatitude)
         long = view.findViewById(R.id.tvLongitude)

        val click: Button = view.findViewById<Button>(R.id.btnDisplay)
        click.setOnClickListener{
           display(view)
        }
        return view
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_map_detail, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MapDetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MapDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
        }
}