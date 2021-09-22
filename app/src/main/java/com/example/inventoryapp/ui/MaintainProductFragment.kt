package com.example.inventoryapp.ui

import android.R
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.inventoryapp.*
import com.example.inventoryapp.databinding.FragmentMaintainProductBinding
import com.example.inventoryapp.database.Product
import com.example.inventoryapp.klModel.ProductViewModel
import com.example.inventoryapp.viewmodel.SupplierViewModel
import kotlinx.coroutines.launch

class MaintainProductFragment : DialogFragment() {

    private lateinit var binding: FragmentMaintainProductBinding
    private val pVM: ProductViewModel by activityViewModels()
    private val sVM: SupplierViewModel by activityViewModels()
    private val barcode by lazy { requireArguments().getString("barcode", "N/A")!!}
    private val name by lazy { requireArguments().getString("name", "N/A")!!}
    private val price by lazy { requireArguments().getDouble("price", 0.00)}
    private val supplier by lazy { requireArguments().getString("supplier", "")}
    private val nav by lazy {findNavController()}
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            binding.imgProductEdit.setImageURI(it.data?.data)
        }
    }
    private val str1:String = "Discard"
    private val str2:String = "Back"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentMaintainProductBinding.inflate(inflater,container,false)

        reset()

        binding.edtPName.doOnTextChanged { text, _, _, _ ->
            if(text.toString() != name) {
                binding.btnSaveEdit.isEnabled = true
                binding.btnDiscard.text = str1
            }
            else {
                binding.btnSaveEdit.isEnabled = false
                binding.btnDiscard.text = str2
            }

        }

        binding.edtPPrice.doOnTextChanged { text, _, _, _ ->
            if(text.toString() != price.toString()) {
                binding.btnSaveEdit.isEnabled = true
                binding.btnDiscard.text = str1
            }
            else{binding.btnSaveEdit.isEnabled = false
                binding.btnDiscard.text = str2
            }

        }

        binding.spinSupplier.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(binding.spinSupplier.selectedItem.toString() != supplier)
                binding.btnSaveEdit.isEnabled = true
            }
        }

        binding.imgProductEdit.setOnClickListener{select()}
        binding.btnSaveEdit.setOnClickListener {editProduct()}
        binding.btnDiscard.setOnClickListener{dismiss()}

        return binding.root
    }


    private fun select() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        launcher.launch(intent)
    }

    private fun reset() {

        val p = pVM.get(barcode)

        if (p == null){
            nav.navigateUp()
            return
        }
        load(p)


    }

    @SuppressLint("SetTextI18n")
    private fun load(p: Product) {
        binding.edtPName.setText(p.name)
        binding.edtPPrice.setText("%.2f".format(p.price))

        // TODO: Load photo and date
        binding.imgProductEdit.setImageBitmap(p.photo.toBitmap())

        binding.edtPName.requestFocus()
        val arrayAdapter = ArrayAdapter<String>(requireContext(), R.layout.simple_spinner_item)
        arrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.spinSupplier.adapter = arrayAdapter

        when(p.category){
            "Food" -> binding.radBtnFood.isChecked = true
            "Beverage" -> binding.radBtnBeverage.isChecked = true
        }

        // TODO(18): Load categories data into spinner -> launch block
        lifecycleScope.launch {

            val suppliers = sVM.getSuppliers()
            if (suppliers != null) {
                for(s in suppliers) {
                    arrayAdapter.add(s.name)
                }
            }
            val suppPosition = arrayAdapter.getPosition(p.supplier)
            binding.spinSupplier.setSelection(suppPosition)
        }

    }

    private fun editProduct(){
        val suppSelected = binding.spinSupplier.selectedItem.toString()

        val cate:String = when {
            binding.radBtnFood.isChecked -> "Food"
            binding.radBtnBeverage.isChecked -> "Beverage"
            else -> ""
        }

        val p = Product(barcode = barcode,
            name = binding.edtPName.text.toString().trim(),
            price = binding.edtPPrice.text.toString().toDoubleOrNull()?:0.00,
            category = cate,supplier = suppSelected,
            photo = binding.imgProductEdit.cropToBlob(300,300))

        val err = pVM.validate(p,false)
        if (err != "") {
            errorDialog(err)
            return
        }

        pVM.set(p)
        Toast.makeText(context,"Product edited",Toast.LENGTH_SHORT).show()
        nav.navigateUp()

    }
}