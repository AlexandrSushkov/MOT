package dev.nelson.mot.main.presentations.payment_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxrelay2.Relay
import dev.nelson.mot.main.R
import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.databinding.ItemPaymentBinding
import dev.nelson.mot.main.util.DateUtils
import dev.nelson.mot.main.util.constant.NetworkConstants
import dev.nelson.mot.main.util.toFormattedDate

class PaymentListAdapter(
    private val onPaymentEntityItemClickPublisher: Relay<Payment>,
    private val onSwipeToDeleteAction: Relay<Payment>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val paymentItemModelList = mutableListOf<PaymentListItemModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val v = layoutInflater.inflate(viewType, parent, false)
        return when (viewType) {
            R.layout.item_payment -> PaymentItemViewHolder(ItemPaymentBinding.inflate(layoutInflater, parent, false), onPaymentEntityItemClickPublisher)
            R.layout.item_header -> HeaderViewHolder(v)
            R.layout.item_footer -> FooterViewHolder(v)
//            else -> LoadingViewholder(v)
            else -> FooterViewHolder(v)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = paymentItemModelList[position]
        when (holder) {
            is HeaderViewHolder -> holder.bind(item as PaymentListItemModel.Header)
            is FooterViewHolder -> holder.bind(item as PaymentListItemModel.Footer)
            is PaymentItemViewHolder -> holder.bind(item as PaymentListItemModel.PaymentItemModel)
//            is LetterViewHolder -> holder.bind(item as MoviesListItemModel.Letter)
        }
    }

    fun setData(newPayments: List<PaymentListItemModel>) {
        val diffCallback = PaymentListDiffCallback(paymentItemModelList, newPayments)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        paymentItemModelList.clear()
        paymentItemModelList.addAll(newPayments)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getItemViewType(position: Int) = when (paymentItemModelList[position]) {
        is PaymentListItemModel.PaymentItemModel -> R.layout.item_payment
        is PaymentListItemModel.Header -> R.layout.item_header
        is PaymentListItemModel.Footer -> R.layout.item_footer
    }

    override fun getItemCount(): Int = paymentItemModelList.size

    fun deleteItem(position: Int) {
        if (paymentItemModelList[position] is PaymentListItemModel.PaymentItemModel) {
            val payment = (paymentItemModelList[position] as PaymentListItemModel.PaymentItemModel).payment
            onSwipeToDeleteAction.accept(payment)
        }
    }

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(header: PaymentListItemModel.Header) {}
    }

    class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(footer: PaymentListItemModel.Footer) {}
    }

//    class LetterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//        fun bind(letter: PaymentListItemModel.Letter) {
//            val letterView = itemView.findViewById<TextView>(R.id.letter)
//            letterView.text = letter.letter.letter
//        }
//    }

    companion object {
        class PaymentItemViewHolder(
            private val itemPaymentBinding: ItemPaymentBinding,
            private val onPaymentEntityItemClickPublisher: Relay<Payment>
        ) : RecyclerView.ViewHolder(itemPaymentBinding.root) {

            fun bind(paymentItemModel: PaymentListItemModel.PaymentItemModel) {
                itemView.setOnClickListener { onPaymentEntityItemClickPublisher.accept(paymentItemModel.payment) }
                itemPaymentBinding.payment = paymentItemModel.payment
                paymentItemModel.payment.dateInMills?.let {
                    val date = DateUtils.createDateFromMills(it)
                    itemPaymentBinding.date.text = date.toFormattedDate(NetworkConstants.DATE_FORMAT)
                }


            }
        }

        class PaymentListDiffCallback(
            private val oldList: List<PaymentListItemModel>,
            private val newList: List<PaymentListItemModel>
        ) : DiffUtil.Callback() {

            override fun getOldListSize(): Int = oldList.size

            override fun getNewListSize(): Int = newList.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return if (oldList[oldItemPosition] is PaymentListItemModel.PaymentItemModel && newList[newItemPosition] is PaymentListItemModel.PaymentItemModel) {
                    (oldList[oldItemPosition] as PaymentListItemModel.PaymentItemModel).payment.id ==
                        (newList[newItemPosition] as PaymentListItemModel.PaymentItemModel).payment.id
                } else {
                    false
                }
            }

            override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {
                return if (oldList[oldPosition] is PaymentListItemModel.PaymentItemModel && newList[newPosition] is PaymentListItemModel.PaymentItemModel) {
                    //no need to check id, if id's is not the same this is different items
                    // also id's check is covered in areItemsTheSame() method
                    val (name, cost, _, date, dateInMills, category) = (oldList[oldPosition] as PaymentListItemModel.PaymentItemModel).payment
                    val (name1, cost1, _, date1, dateInMills1, category1) = (newList[newPosition] as PaymentListItemModel.PaymentItemModel).payment

                    return name == name1
                        && cost == cost1
                        && date == date1
                        && dateInMills == dateInMills1
                        && category == category1
                } else {
                    false
                }

            }
        }
    }

}
