package com.example.inventoryapp.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.inventoryapp.R
import com.example.inventoryapp.database.Flow
import com.example.inventoryapp.database.Product
import com.example.inventoryapp.databinding.FragmentStockActionBinding
import com.example.inventoryapp.klModel.FlowViewModel
import com.example.inventoryapp.klModel.ProductViewModel
import com.example.inventoryapp.toBitmap
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class StockActionFragment : Fragment() {
    private lateinit var binding: FragmentStockActionBinding
    private val pVM:ProductViewModel by activityViewModels()
    private val fVM:FlowViewModel by activityViewModels()
    private val barcode by lazy { requireArguments().getString("barcode", "N/A")!!}
    private val oriStock by lazy { requireArguments().getInt("stockQuantity", 0)!!}
    private val operation by lazy { requireArguments().getString("operation", "N/A")!!}
    private val nav by lazy{findNavController()}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        var finalValue:Int = oriStock
        binding = FragmentStockActionBinding.inflate(inflater, container, false)
        reset()
        if(operation == "increase") {
            binding.qtyTo.doOnTextChanged { text, start, before, count ->
                if (binding.qtyTo.text.isNotEmpty()) {
                    finalValue = oriStock + text.toString().toInt()
                    binding.result.text = "$oriStock + ${text.toString()} = $finalValue"
                    binding.btnStock.isEnabled = true
                }
                else{
                    binding.result.text = ""
                    binding.btnStock.isEnabled = false
                }
            }
        }
        else{
            binding.qtyTo.doOnTextChanged { text, start, before, count ->
                if (binding.qtyTo.text.isNotEmpty()) {
                    finalValue = oriStock - text.toString().toInt()
                    binding.result.text = "$oriStock - ${text.toString()} = ${oriStock - text.toString().toInt()}"
                    binding.btnStock.isEnabled = true
                }
                else{
                    binding.result.text = ""
                    binding.btnStock.isEnabled = false
                }
            }
        }
        binding.btnStock.setOnClickListener { submit(finalValue) }

        return binding.root
    }

    private fun submit(value:Int) {
        val id = (fVM.getLastFlowId()?:0) + 1
        val format = SimpleDateFormat("dd/MM/yyyy hh:mm:ss a", Locale.getDefault())
        val recordDate = format.format(Date())

        if(value < 0) {
            AlertDialog.Builder(context)
                .setIcon(R.drawable.ic_error)
                .setTitle("Error")
                .setMessage("Stocks to decrease exceeded the current quantity")
                .setPositiveButton("Close", null)
                .show()
            return
        }
        val p = Product(barcode = barcode, stockQty = value)
        val f = Flow(flowId = id.toString(), pBarcode = barcode, operation = operation, qtyBefore = oriStock,
                    binding.qtyTo.text.toString().toInt(),value, recordDate)

        pVM.moveStock(p)
        fVM.set(f)
        Toast.makeText(context,"Stock updated", Toast.LENGTH_LONG).show()
        nav.navigateUp()
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
        when(operation){
            "increase" -> {
                binding.opTitle.text = "Stock Increment"
                binding.opTitle.setTextColor(Color.parseColor("#0FED2D"))
            }
            "decrease" -> {
                binding.opTitle.text = "Stock Reduction"
                binding.opTitle.setTextColor(Color.parseColor("#C01529"))
            }
        }
        binding.pName.text = p.name
        binding.currQty.text = p.stockQty.toString()
        binding.lblQtyTo.text = "Quantity\nto $operation:"
        // TODO: Load photo and date
        binding.pImage.setImageBitmap(p.photo.toBitmap())

        binding.qtyTo.requestFocus()


    }
}