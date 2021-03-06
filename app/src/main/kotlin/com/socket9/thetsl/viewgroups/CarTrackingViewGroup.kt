package com.socket9.thetsl.viewgroups

import android.annotation.TargetApi
import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.socket9.thetsl.R
import com.socket9.thetsl.models.Model
import kotlinx.android.synthetic.main.viewgroup_service.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.subjects.PublishSubject

class CarTrackingViewGroup : BaseCustomViewGroup, AnkoLogger {

    /** Variable zone **/
    lateinit private var viewContainer: View
    lateinit private var tvServiceName: TextView
    lateinit private var tvStatus: TextView
    lateinit private var tvLastUpdate: TextView
    lateinit private var ivLogo: ImageView
    lateinit private var cardService: CardView
    private var cardClickedObservable: PublishSubject<Int> = PublishSubject.create()


    /** Override method zone **/
    constructor(context: Context) : super(context) {
        initInflate()
        initInstances()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initInflate()
        initInstances()
        initWithAttrs(attrs, 0, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initInflate()
        initInstances()
        initWithAttrs(attrs, defStyleAttr, 0)
    }

    @TargetApi(21)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        initInflate()
        initInstances()
        initWithAttrs(attrs, defStyleAttr, defStyleRes)
    }

    private fun initInflate() {
        viewContainer = inflate(context, R.layout.viewgroup_service, this)
    }

    private fun initInstances() {
        // findViewById here
        tvServiceName = viewContainer.findViewById(R.id.tvServiceName) as TextView
        tvStatus = viewContainer.findViewById(R.id.tvStatus) as TextView
        tvLastUpdate = viewContainer.findViewById(R.id.tvLastUpdate) as TextView
        ivLogo = viewContainer.findViewById(R.id.ivLogo) as ImageView
        cardService = viewContainer.findViewById(R.id.cardService) as CardView

        cardService.setOnClickListener {
            cardClickedObservable.onNext(1)
        }

    }

    fun getCardClickedObservable(): Observable<Int> {
        return cardClickedObservable.subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread())
    }

    private fun initWithAttrs(attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) {
        /*
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.StyleableName,
                defStyleAttr, defStyleRes);

        try {

        } finally {
            a.recycle();
        }
        */
    }

    /** Method zone **/

    fun setModel(model: Model.ServiceTrackingEntity) {

    //  TODO: SetModel for service view group
        tvServiceName.text = model.model
        tvStatus.text = "Status"
        tvLastUpdate.text = model.detail[model.detail.size-1].dateFinish
        tvLicensePlate.text = model.licensePlate

    }
}
