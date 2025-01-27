package com.example.buynow.presentation.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.buynow.presentation.adapter.CarDItemClickAdapter
import com.example.buynow.presentation.adapter.CardAdapter
import com.example.buynow.R
import com.example.buynow.data.local.room.Card.CardEntity
import com.example.buynow.data.local.room.Card.CardViewModel
import com.example.buynow.databinding.CardAddBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.widget.Button
import android.widget.EditText
import com.example.buynow.data.CardType

class PaymentMethodActivity : AppCompatActivity(), CarDItemClickAdapter {

    private lateinit var cardRec: RecyclerView
    private lateinit var cardAdapter: CardAdapter
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var bottomSheetView: View
    private lateinit var cardViewModel: CardViewModel
    private lateinit var item: ArrayList<CardEntity>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_method)

        cardRec = findViewById(R.id.cardRecView_paymentMethodPage)
        val backIv_PaymentMethodsPage = findViewById<ImageView>(R.id.backIv_PaymentMethodsPage)
        val addCard_PaymentMethodPage = findViewById<FloatingActionButton>(R.id.addCard_PaymentMethodPage)

        item = arrayListOf()
        cardViewModel = ViewModelProvider(this).get(CardViewModel::class.java)

        // Initialize RecyclerView
        cardRec.layoutManager = LinearLayoutManager(this)
        cardAdapter = CardAdapter(this, this)
        cardRec.adapter = cardAdapter

        // Set up the back button
        backIv_PaymentMethodsPage.setOnClickListener {
            onBackPressed()
        }

        // Setup the BottomSheetDialog
        bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        val cardAddBinding: CardAddBottomSheetBinding = CardAddBottomSheetBinding.inflate(LayoutInflater.from(this))
        bottomSheetView = cardAddBinding.root

        // Show Bottom Sheet when the add card button is clicked
        addCard_PaymentMethodPage.setOnClickListener {
            showBottomSheet()
        }

        // Load card data
        getRecData()
    }

    private fun getRecData() {
        cardViewModel.allCards.observe(this) { list ->
            list?.let {
                cardAdapter.updateList(it)
                item.clear()
                item.addAll(it)
            }
        }
    }

    private fun showBottomSheet() {
        // Clear the fields in the Bottom Sheet
        bottomSheetView.findViewById<EditText>(R.id.nameEt_cardAddBottomSheet).text.clear()
        bottomSheetView.findViewById<EditText>(R.id.cardNumber_cardAddBottomSheet).text.clear()
        bottomSheetView.findViewById<EditText>(R.id.exp_cardAddBottomSheet).text.clear()
        bottomSheetView.findViewById<EditText>(R.id.cvv_cardAddBottomSheet).text.clear()

        // Set click listener for the add card button
        bottomSheetView.findViewById<Button>(R.id.addCardBtn_cardAddBottomSheet).setOnClickListener {
            saveData()
        }

        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }

    private fun saveData() {
        val holderName = bottomSheetView.findViewById<EditText>(R.id.nameEt_cardAddBottomSheet).text.toString()
        val cardNumber = bottomSheetView.findViewById<EditText>(R.id.cardNumber_cardAddBottomSheet).text.toString()
        val exp = bottomSheetView.findViewById<EditText>(R.id.exp_cardAddBottomSheet).text.toString()
        val cvv = bottomSheetView.findViewById<EditText>(R.id.cvv_cardAddBottomSheet).text.toString()

        // Determine card brand based on card number
        val cardBrand = CardType.detect(cardNumber)

        // Validate card number and insert the card data
        if (isValid(cardNumber.toLongOrNull())) {
            cardViewModel.insert(CardEntity(holderName, cardNumber, exp, cvv, cardBrand))
            toast("New Card Added")
            bottomSheetDialog.dismiss()
        } else {
            toast("Enter Valid Card.")
        }
    }

    // Method to check if the card number is valid (implement this according to your logic)
    private fun isValid(cardNumber: Long?): Boolean {
        // Add your validation logic here
        return cardNumber != null && cardNumber.toString().length in 13..19 // Example validation
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onItemDeleteClick(cardEntity: CardEntity) {
        cardViewModel.deleteCart(cardEntity)
        toast("Card Removed")
    }

    override fun onItemUpdateClick(cardEntity: CardEntity) {
        cardViewModel.updateCart(cardEntity)
        toast("Card Updated")
    }
}
