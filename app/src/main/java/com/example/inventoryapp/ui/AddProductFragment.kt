package com.example.inventoryapp.ui
import android.R
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.inventoryapp.*
import com.example.inventoryapp.database.Product
import com.example.inventoryapp.database.SUPPLIERS
import com.example.inventoryapp.databinding.FragmentAddProductBinding
import com.example.inventoryapp.klModel.ProductViewModel
import com.example.inventoryapp.viewmodel.SupplierViewModel
import kotlinx.coroutines.launch


class AddProductFragment : Fragment() {
    private val pVM: ProductViewModel by activityViewModels()
    private val sVM: SupplierViewModel by activityViewModels()
    private lateinit var binding: FragmentAddProductBinding
    private val nav by lazy{findNavController()}
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            binding.addImage.setImageURI(it.data?.data)
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentAddProductBinding.inflate(inflater, container, false)
        binding.addImage.setOnClickListener { select() }
        binding.btnAdd.setOnClickListener { addProduct() }

        val arrayAdapter = ArrayAdapter<String>(requireContext(), R.layout.simple_spinner_item)
        arrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.spinSupplier.adapter = arrayAdapter

        // TODO(18): Load categories data into spinner -> launch block
        lifecycleScope.launch {

            val suppliers = sVM.getSuppliers()
            if (suppliers != null) {
                for(s in suppliers) {
                    arrayAdapter.add(s.name)
                }
            }
        }


        return binding.root
    }

    private fun select() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        launcher.launch(intent)
    }

    private fun addProduct(){
        val barcode = (1000000000..9999999999).random()
        val suppSelected = binding.spinSupplier.selectedItem.toString()

        val cate:String = if(binding.radBtnFood.isChecked)
            "Food"
        else if(binding.radBtnBeverage.isChecked)
            "Beverage"
        else
            ""

        val p = Product(barcode = barcode.toString(),
            name = binding.addName.text.toString().trim(),
            price = binding.addPrice.text.toString().toDoubleOrNull()?:0.00,
            stockQty = binding.addQty.text.toString().toIntOrNull()?:0,
            category = cate,supplier = suppSelected,photo = binding.addImage.cropToBlob(300,300))

        val err = pVM.validate(p)
        if (err != "") {
            errorDialog(err)
            return
        }

        pVM.set(p)
        Toast.makeText(context,"Added a new product",Toast.LENGTH_SHORT).show()
        nav.navigateUp()
    }

}